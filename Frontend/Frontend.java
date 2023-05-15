package Frontend;

import java.net.InetAddress;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import Client.Client;
import Dto.GetOptimizationDto;
import Entities.Destination;
import Entities.Student;
import Utils.Config;

import javax.swing.*;

public class Frontend {
    public static void main(String[] args) {
        UI ui = new UI();
        ResultsScreen resultsScreen = new ResultsScreen();


        try {
            String serverAddress = InetAddress.getLocalHost().getHostAddress(); // <-- Change this to the server address if you want to connect to a remote server.
            Client client = new Client(serverAddress, Config.PORT);

            // add async listener so when the client will have the data, it will update the Frontend.UI
            client.addOnDataReceivedListener(() -> {
                ui.setLeftListModel(client.destinations.stream().map(Destination::toString).collect(Collectors.toList()));
            });

            client.addOnOptimizationReceivedListener(() -> {
                Map<Integer, Integer> studentToDestination = client.studentToDestination;

                DefaultListModel<String> defaultListModel = new DefaultListModel<>();
                for (Map.Entry<Integer, Integer> entry : studentToDestination.entrySet()) {
                    // find the student with the id of the entry.
                    Student student = client.students.stream().filter(s -> Objects.equals(s.getId(), entry.getKey())).findFirst().orElse(null);
                    // find the destination with the id of the entry.
                    Destination destination = client.destinations.stream().filter(d -> d.getId() == entry.getValue()).findFirst().orElse(null);

                    // combine the student and destination to a string and add it to the list.
                    assert destination != null;
                    defaultListModel.addElement(student.toString() + " -> " + destination.toString());
                }

                resultsScreen.setListModel(defaultListModel);
                resultsScreen.render();
            });

            ui.addOnSubmitListener(() -> {
                List<String> preferences = ui.getRightListModel();
                int[] nPreferences = new int[preferences.size()];

                for (int i = 0; i < preferences.size(); i++) {
                    for (Destination destination : client.destinations) {
                        if (destination.toString().equals(preferences.get(i))) {
                            nPreferences[i] = destination.getId();
                        }
                    }
                }

                // find the student with the name of the entry.
                Student student = client.students.stream().filter(s -> Objects.equals(s.toString(), ui.getStudentId())).findFirst().orElse(null);
                client.sendGetOptimizationDto(new GetOptimizationDto(student.getId(), nPreferences));
            });

            ui.render();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
