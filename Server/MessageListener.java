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
        preferences.stream().forEach(System.out::println);
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

                        // optimize the students preferences
                        Student student = this.proceedDto(dto);
                        System.out.println(student);

//                        i = 0
//                       [
//                        [1, 2,6,7,4]
//                        [1,2,3, 4, 5],
//                        [1,2,3],
//                                ]
//
//                        [
//                                "student1",
//                        "student5",
//                        "student3",
//                                ]
//
//                        [0, 2, 5, 4, ..., ]
//
//                         [
//                        "dest1",
//                                "dest2",
//                                "dest3",
//                        ...
//                                ]
//
//                    [
//                        1,
//                        1,
//                        2,]
//
                        OptimizationData optimizationData = db.getOptimizationData();
                        System.out.println(optimizationData);
                        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(optimizationData.studentToDestinationPreference, optimizationData.destinationToCapacity);
//                        System.out.println(geneticAlgorithm.optimize());


//                        // Send the message to all clients
//                        for (Socket otherClient : this.server.getClients())
//                            Helpers.sendDto(otherClient, new GetOptimizationResponseDto(dto.studentId,  optimizedPreference.getId()));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
