package Client;

import Dto.GetInitDataResponseDto;
import Entities.Destination;
import Entities.Student;
import Utils.Config;
import Utils.Helpers;

import java.net.Socket;
import java.util.ArrayList;

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
                Thread.sleep(Config.SLEEP_DELAY);

                // if there is no message, continue.
                if (this.socket.getInputStream().available() <= 0) continue;

                Object dto = Helpers.receiveDto(this.socket);

                if (dto instanceof GetInitDataResponseDto) {
                    this.handleInitDataResponse((GetInitDataResponseDto) dto);
                    continue;
                }

                System.out.println(dto);
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
}
