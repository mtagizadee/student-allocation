package Server;

import Dto.GetInitDataResponseDto;
import Dto.GetOptimizationDto;
import Dto.GetOptimizationResponseDto;
import Entities.Destination;
import Entities.Student;
import GA.GeneticAlgorithm;
import Utils.Config;
import Utils.DB;
import Utils.Helpers;

import java.net.Socket;
import java.util.ArrayList;

public class MessageListener extends Thread {
    private final Server server;
    private GeneticAlgorithm ga;

    public MessageListener(Server server) {
        this.server = server;
        this.ga = new GeneticAlgorithm();
    }

    private Student proceedDto(GetOptimizationDto dto) {
        DB db = Helpers.getDb();

        Student student = db.getStudent(dto.studentId);
        ArrayList<Destination> preferences = new ArrayList<Destination>();

        for (int i = 0; i < dto.preferences.size(); i++) preferences.add(db.getDestination(dto.preferences.get(i)));
        student.setPreferences(preferences);

        // save student in the database for the next requests
        db.saveStudent(student);
        return student;
    }

    public void run() {
        System.out.println("Server.Server is listening on " + this.server.getFullAddress());

        while (true) {
            try {
                Thread.sleep(Config.SLEEP_DELAY);

                // Wait for a client to send message.
                for (Socket client : this.server.getClients()) {
                    if (client.getInputStream().available() > 0) {
                        // Receive the message.
                        GetOptimizationDto dto = (GetOptimizationDto) Helpers.receiveDto(client);

                        // optimize the message
                        if (dto == null) continue;

                        // optimize the students preferences
                        Student student = this.proceedDto(dto);
                        Destination optimizedPreference = this.ga.optimize(student);

                        // Send the message to all clients
                        for (Socket otherClient : this.server.getClients())
                            Helpers.sendDto(otherClient, new GetOptimizationResponseDto(dto.studentId,  optimizedPreference.getId()));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
