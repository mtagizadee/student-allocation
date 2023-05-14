package Client;

import Dto.GetOptimizationDto;
import Entities.Destination;
import Entities.Student;
import Utils.Helpers;
import java.net.Socket;
import java.util.List;

public class Client {
    private Socket socket;
    public List<Student> students;
    public List<Destination> destinations;

    public Client(String addressToConnect, int portToConnect) {
        try {
            this.socket = new Socket(addressToConnect, portToConnect);

            MessageHandler messageHandler = new MessageHandler(this.socket, this);
            messageHandler.start();
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

    public void sendGetOptimizationDto(GetOptimizationDto getOptimizationDto) {
        try {
            Helpers.sendDto(this.socket, getOptimizationDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
