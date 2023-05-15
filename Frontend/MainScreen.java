package Frontend;

import javax.swing.*;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class MainScreen {
    private DefaultListModel<String> leftListModel = new DefaultListModel<>();
    private String studentId;
    private DefaultListModel<String> rightListModel = new DefaultListModel<>();
    private static final int MAX_ITEMS = 6;
    private SubmitPreferencesListener submitPreferencesListener;

    public void setLeftListModel(List<String> leftListModel) {
        for (String item : leftListModel) {
            System.out.println(item);
            this.leftListModel.addElement(item);
        }

    }

    public void setRightListModel(List<String> rightListModel) {
        this.rightListModel.clear();
        for (String item : rightListModel)
            this.rightListModel.addElement(item);
    }

    public List<String> getRightListModel() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < rightListModel.size(); i++) {
            list.add(rightListModel.get(i));
        }
        return list;
    }

    public String getStudentId() {
        return studentId;
    }

    public void render() {
        JFrame frame = new JFrame("Beast App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        // Part 2: Bottom
        JPanel bottomPanel = new JPanel(new BorderLayout());
        frame.getContentPane().add(bottomPanel, BorderLayout.CENTER);

        // Part 5: First list
        JList<String> leftList = new JList<>(leftListModel);
        leftList.setDragEnabled(true);
        leftList.setTransferHandler(new ListItemTransferHandler());
        leftList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int selectedIndex = leftList.getSelectedIndex();
                    if (rightListModel.size() < 6) {
                        rightListModel.addElement(leftListModel.get(selectedIndex));
                        leftListModel.remove(selectedIndex);
                    }
                }
            }
        });

        JList<String> rightList = new JList<>(rightListModel);
        rightList.setDragEnabled(true);
        rightList.setDropMode(DropMode.INSERT);
        rightList.setTransferHandler(new ListItemTransferHandler() {
            @Override
            public boolean canImport(TransferSupport support) {
                if (!support.isDataFlavorSupported(DataFlavor.stringFlavor))
                    return false;

                JList<?> target = (JList<?>) support.getComponent();
                if (target.getModel().getSize() >= 6)
                    return false;

                return true;
            }
        });

        rightList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int selectedIndex = rightList.getSelectedIndex();
                    leftListModel.addElement(rightListModel.get(selectedIndex));
                    rightListModel.remove(selectedIndex);
                }
            }
        });

        // Part 4: Two lists
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(leftList),
                new JScrollPane(rightList));
        bottomPanel.add(splitPane, BorderLayout.CENTER);

        // Part 7: On the right
        JPanel rightPanel = new JPanel(new BorderLayout());
        bottomPanel.add(rightPanel, BorderLayout.EAST);

        // Part 8: Arrow up with a label
        JLabel arrowLabel = new JLabel("↑ Most Priority");
        rightPanel.add(arrowLabel, BorderLayout.NORTH);

        // Part 9: Submit Button

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            if (rightListModel.size() < MAX_ITEMS) {
                JOptionPane.showMessageDialog(null, "You must select at least 6 items");
            } else {
                String studentId = JOptionPane.showInputDialog("Enter student id");
                this.studentId = studentId;
                submitPreferencesListener.onSubmitPreferences();
            }
        });
        rightPanel.add(submitButton, BorderLayout.SOUTH);

        // Apply cool styles
        UIManager.put("Button.font", new Font("Tahoma", Font.BOLD, 12));
        UIManager.put("Label.font", new Font("Tahoma", Font.BOLD, 12));
        UIManager.put("TextField.font", new Font("Tahoma", Font.PLAIN, 12));
        UIManager.put("List.font", new Font("Tahoma", Font.PLAIN, 12));

        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void renderLater(Runnable runnable) {
        SwingUtilities.invokeLater(() -> {
            render();
            runnable.run();
        });
    }

    public void renderLater() {
        SwingUtilities.invokeLater(() -> render());
    }

    public void addOnSubmitListener(Runnable runner) {
        submitPreferencesListener = new SubmitPreferencesListener() {
            @Override
            public void onSubmitPreferences() {
                runner.run();
            }
        };
    }

    public String[] getPreferences() {
        return (String[]) leftListModel.toArray();
    }

    private static class ListItemTransferHandler extends TransferHandler {
        @Override
        public int getSourceActions(JComponent c) {
            return MOVE;
        }

        @Override
        protected Transferable createTransferable(JComponent c) {
            JList<?> list = (JList<?>) c;
            return new StringSelection(list.getSelectedValue().toString());
        }

        @Override
        public boolean canImport(TransferSupport support) {
            if (!support.isDataFlavorSupported(DataFlavor.stringFlavor))
                return false;
            return true;
        }

        @Override
        public boolean importData(TransferSupport support) {
            if (!canImport(support)) {
                return false;
            }

            JList<?> target = (JList<?>) support.getComponent();
            JList.DropLocation dropLocation = (JList.DropLocation) support.getDropLocation();
            DefaultListModel<String> listModel = (DefaultListModel<String>) target.getModel();
            int index = dropLocation.getIndex();

            try {
                String data = (String) support.getTransferable().getTransferData(DataFlavor.stringFlavor);
                if (index == -1) {
                    listModel.addElement(data);
                } else {
                    listModel.add(index, data);
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void exportDone(JComponent source, Transferable data, int action) {
            if (action == MOVE) {
                JList<?> list = (JList<?>) source;
                int selectedIndex = list.getSelectedIndex();
                DefaultListModel<?> listModel = (DefaultListModel<?>) list.getModel();
                listModel.remove(selectedIndex != -1 ? selectedIndex : listModel.size() - 1);
            }
        }
    }
}
