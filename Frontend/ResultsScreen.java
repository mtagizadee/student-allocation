package Frontend;

import javax.swing.*;
import java.awt.*;

public class ResultsScreen {

    private DefaultListModel<String> listModel;

    public void setListModel(DefaultListModel<String> listModel) {
        this.listModel = listModel;
    }

    public void render() {
        JFrame frame2 = new JFrame("Beast App");
        frame2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());

        JList<String> list = new JList<>(listModel);

        JScrollPane scrollPane = new JScrollPane(list);

        panel.add(scrollPane, BorderLayout.CENTER);

        frame2.getContentPane().add(panel);

        frame2.setSize(300, 300);
        frame2.setResizable(false);

        frame2.setLocationRelativeTo(null);
        frame2.setVisible(true);
    }
}
