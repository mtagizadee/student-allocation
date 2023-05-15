package Utils;

import Entities.Destination;
import Entities.Student;
import SampleGA.Population;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Random;

public class Helpers {

    public static Object receiveDto(Socket socket) {
        try {
            InputStream inputStream = socket.getInputStream();
            byte[] receivedBytes = new byte[Config.BUFFER_SIZE * 10];
            int bytesRead = inputStream.read(receivedBytes);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(receivedBytes, 0, bytesRead);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            return objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // if something goes wrong
    }

    public static void sendDto(Socket socket, Object dto) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(dto);
            objectOutputStream.flush();
            byte[] objectBytes = byteArrayOutputStream.toByteArray();

            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(objectBytes);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final DB db = new DB(); // there are a lot of classes that require Utils.DB, so we will use it from Utils.Helpers
    public static int rand(int min,int max) {
        return new Random().nextInt(max - min + 1) + min;
    }

    public static DB getDb() { // encapsulate Utils.DB
        return db;
    }

    public static Destination result(Population population, Student student) {
        List<Destination> preferences = student.getPreferences();
        for (Destination preference : preferences) {
            if (!preference.isFull()) {
                preference.addStudent();

                // save destination in the database for the next requests
                db.saveDestination(preference);

                return preference;
            }
        }

        return null;
    }
}
