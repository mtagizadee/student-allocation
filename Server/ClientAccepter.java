package Server;

import Dto.GetInitDataResponseDto;
import Utils.DB;
import Utils.Helpers;

import java.net.Socket;

public class ClientAccepter implements Runnable {
    private final Server server;

    public ClientAccepter(Server server) {
        this.server = server;
    }

    public void run() {
        while (true) {
            try {
                Socket socket = this.server.getSocket().accept();

                // If the server is full, close the socket and continue.
                if (this.server.isFull()) {
                    socket.close();
                    continue;
                }
                this.server.getClients().add(socket);

                // send all data needed data from database to client
                DB db = Helpers.getDb();
                Helpers.sendDto(socket, new GetInitDataResponseDto(db.getStudents(), db.getDestinations()));

                System.out.println("A new client was connected!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
