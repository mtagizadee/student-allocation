package Server;

import Dto.Event;
import Dto.GetOptimizationDto;
import Dto.GetOptimizationResponseDto;
import Entities.Destination;
import Entities.Student;
import GeneticAlgorithm.GeneticAlgorithm;
import Utils.Config;
import Utils.DB;
import Utils.Helpers;
import Utils.OptimizationData;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageListener extends Thread {
    private final Server server;

    public MessageListener(Server server) {
        this.server = server;
    }

    private Student proceedDto(GetOptimizationDto dto) {
        DB db = Helpers.getDb();

        Student student = db.getStudent(dto.studentId);
        ArrayList<Destination> preferences = new ArrayList<Destination>();

        for (int i = 0; i < dto.preferences.length; i++) preferences.add(db.getDestination(dto.preferences[i]));
        student.setPreferences(preferences);

        // save student in the database for the next requests
        db.addStudentToOptimization(student);
        return student;
    }

    public void run() {
        System.out.println("Server.Server is listening on " + this.server.getFullAddress());
        DB db = Helpers.getDb();

        while (true) {
            try {
                Thread.sleep(Config.SLEEP_DELAY);

                // Wait for a client to send message.
                for (Socket client : this.server.getClients()) {
                    if (client.getInputStream().available() > 0) {
                        // Receive the message.
                        GetOptimizationDto dto = (GetOptimizationDto) Helpers.receiveDto(client);
                        if (dto == null || dto.event != Event.GetOptimization) continue;

                        this.proceedDto(dto);
                        OptimizationData optimizationData = db.getOptimizationData();
                        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(optimizationData.studentToDestinationPreference, optimizationData.destinationToCapacity);

                        List<Integer> res = geneticAlgorithm.optimize();
                        List<Student> reference = optimizationData.studentsReferences;
                        List<Destination> destinationReference = db.getDestinations();

                        Map<Integer, Integer> studentToDestination = new HashMap<Integer, Integer>();
                        for (int i = 0; i < res.size(); i++) {
                            System.out.println("Student " + reference.get(i).getId() + " is assigned to destination " + destinationReference.get(res.get(i)).getId());
                            studentToDestination.put(reference.get(i).getId(), destinationReference.get(res.get(i)).getId());
                        }

                        for (Socket otherClient : this.server.getClients())
                            Helpers.sendDto(otherClient, new GetOptimizationResponseDto(studentToDestination));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
