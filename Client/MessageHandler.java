package Client;

import Dto.GetInitDataResponseDto;
import Dto.GetOptimizationResponseDto;
import Entities.Destination;
import Entities.Student;
import Utils.Config;
import Utils.DB;
import Utils.Helpers;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MessageHandler extends Thread {
    private final Client client;
    private final Socket socket;

    public MessageHandler(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;
    }

    public void run() {
        while (true) {
            try {
                // if there is no message, continue.
                if (this.socket.getInputStream().available() <= 0) continue;
                Object dto = Helpers.receiveDto(this.socket);
                System.out.println("Message is received from the server.");
                System.out.println(dto);

                if (dto instanceof GetInitDataResponseDto)
                    this.handleInitDataResponse((GetInitDataResponseDto) dto);
                else if (dto instanceof GetOptimizationResponseDto)
                    this.handleOptimizationResponse((GetOptimizationResponseDto) dto);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handleInitDataResponse(GetInitDataResponseDto getInitDataResponseDto) {
        // copy students
        this.client.students = new ArrayList<Student>();
        this.client.students.addAll(getInitDataResponseDto.students);

        // copy destinations
        this.client.destinations = new ArrayList<Destination>();
        this.client.destinations.addAll(getInitDataResponseDto.destinations);
    }

    private void handleOptimizationResponse(GetOptimizationResponseDto getOptimizationResponseDto) {
        this.client.studentToDestination = getOptimizationResponseDto.studentToDestination;
        // display studentToDestination
        DB db = Helpers.getDb();

        for (int studentId : this.client.studentToDestination.keySet()) {
            System.out.println(db.getStudent(studentId));
            System.out.println(db.getDestination(this.client.studentToDestination.get(studentId)));
        }
    }
}
