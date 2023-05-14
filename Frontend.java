import java.net.InetAddress;

import Client.Client;
import Utils.Config;

public class Frontend {
    public static void main(String[] args) {
        try {
            String serverAddress = InetAddress.getLocalHost().getHostAddress(); // <-- Change this to the server address if you want to connect to a remote server.
            Client client = new Client(serverAddress, Config.PORT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
