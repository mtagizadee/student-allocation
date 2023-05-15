package Client;

import Dto.GetOptimizationDto;
import Entities.Destination;
import Entities.Student;
import Utils.Config;
import Utils.DB;
import Utils.Helpers;

import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;

public class Client {
    private Socket socket;
    public List<Student> students;
    public List<Destination> destinations;
    public Map<Integer, Integer> studentToDestination;

    public Client(String addressToConnect, int portToConnect) {
        this.students = new ArrayList<Student>();
        this.destinations = new ArrayList<Destination>();
        this.studentToDestination = new HashMap<Integer, Integer>();

        try {
            this.socket = new Socket(addressToConnect, portToConnect);
            DB db = Helpers.getDb();
            List<Destination> destinations = db.getDestinations();
            List<Student> students = db.getStudents();

            Helpers.sendDto(this.socket, new GetOptimizationDto(students.get(1).getId(), new int[] {
                    destinations.get(0).getId(),
                    destinations.get(3).getId(),
                    destinations.get(4).getId()
            }));
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

    public static void main(String argv[]) {
        try {
            String serverAddress = InetAddress.getLocalHost().getHostAddress(); // <-- Change this to the server address if you want to connect to a remote server.
            Client client = new Client(serverAddress, Config.PORT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
