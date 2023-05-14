import Utils.Config;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    /**
     * The server of the configurations.
     */
    private InetAddress address;
    private int port;

    private ServerSocket socket;
    /**
     * The maximum number of clients that can connect to the server at once.
     */
    private int capacity;
    private ArrayList<Socket> clients;

    public Server(int port, int maxClients) {
        try {
            this.socket = new ServerSocket(port);
            this.address = InetAddress.getLocalHost();
            this.capacity = maxClients;
            this.port = port;
            this.clients = new ArrayList<Socket>();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getFullAddress() {
        return this.address.getHostAddress() + ":" + this.port;
    }

    public ArrayList<Socket> getClients() {
        return this.clients;
    }

    public ServerSocket getSocket() {
        return this.socket;
    }

    public boolean isFull() {
        return this.clients.size() >= this.capacity;
    }

    public void shutdown() {
        try {
            this.socket.close();

            // close all clients.
            for (Socket client : this.clients) {
                client.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listen() {
        Thread thread = new Thread(new ClientAccepter(this));
        thread.start();

        MessageListener messageListener = new MessageListener(this);
        messageListener.start();
    }

    /**
     * Run this main function if you want to start the server.
     */
    public static void main(String[] args) {
        Server server = new Server(Config.PORT, Config.maxClients);
        server.listen();
    }
}
