import Dto.GetOptimizationDto;
import Entities.Destination;
import Utils.Config;
import Utils.Helpers;

import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class Client {
    private Socket socket;

    public Client(String addressToConnect, int portToConnect) {
        try {
            this.socket = new Socket(addressToConnect, portToConnect);

            GetOptimizationDto getOptimizationDto = new GetOptimizationDto(1, new ArrayList<Destination>(
                Arrays.asList(
                    new Destination(1, "Destination 1", 3),
                    new Destination(2, "Destination 2",4))
                )
            );

            this.sendMessage(getOptimizationDto);
            this.receiveMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            this.socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(GetOptimizationDto getOptimizationDto) {
        try {
            Helpers.sendDto(this.socket, getOptimizationDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void receiveMessage() {
        while (true) {
            try {
                // if there is no message, continue.
                if (this.socket.getInputStream().available() <= 0) continue;

                Object dto = Helpers.receiveDto(this.socket);
                System.out.println(dto);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Run this command if you want to start join the chat as a client.
     */
    public static void main(String[] args) {
        try {
            String serverAddress = InetAddress.getLocalHost().getHostAddress(); // <-- Change this to the server address if you want to connect to a remote server.

            Client client = new Client(serverAddress, Config.PORT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
