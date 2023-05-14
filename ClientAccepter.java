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
                System.out.println("A new client was connected!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
