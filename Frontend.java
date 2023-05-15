import java.net.InetAddress;
import java.util.stream.Collectors;

import Client.Client;
import Entities.Destination;
import Utils.Config;

public class Frontend {
    public static void main(String[] args) {
        UI ui = new UI();


        try {
            String serverAddress = InetAddress.getLocalHost().getHostAddress(); // <-- Change this to the server address if you want to connect to a remote server.
            Client client = new Client(serverAddress, Config.PORT);

            // add async listener so when the client will have the data, it will update the UI
            client.addOnDataReceivedListener(() -> {
                ui.setLeftListModel(client.destinations.stream().map(Destination::toString).collect(Collectors.toList()));
            });

            client.addOnOptimizationReceivedListener(() -> {

            });

//            ui.setLeftListModel(client.destinations.stream().map(Destination::toString).collect(Collectors.toList()));

            ui.render();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
