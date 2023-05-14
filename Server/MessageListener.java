package Server;

import Utils.Config;
import Utils.Helpers;

import java.net.Socket;

public class MessageListener extends Thread {
    private final Server server;

    public MessageListener(Server server) {
        this.server = server;
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
                        Object dto =  Helpers.receiveDto(client);

                        // optimize the message

                        // Send the message to all clients
                        for (Socket otherClient : this.server.getClients()) {
                            if (client.equals(otherClient)) continue;
                            Helpers.sendDto(otherClient, dto);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
