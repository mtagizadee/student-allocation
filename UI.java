import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UI {
    private DefaultListModel<String> leftListModel;
    private DefaultListModel<String> rightListModel;
    private JTextField searchField;
    private static final int MAX_ITEMS = 6;

    public void setLeftListModel(List<String> leftListModel) {
        this.leftListModel.clear();
        for (String item : leftListModel)
            this.leftListModel.addElement(item);
    }

    public void setRightListModel(List<String> rightListModel) {
        this.rightListModel.clear();
        for (String item : rightListModel)
            this.rightListModel.addElement(item);
    }

    public void render() {
        JFrame frame = new JFrame("Beast App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        // Part 1: Navbar
        JToolBar navbar = new JToolBar();
        navbar.setFloatable(false);
        navbar.add(new JLabel("Student Name - Student Image"));
        navbar.add(Box.createHorizontalGlue());
        navbar.add(new JLabel("Beast App"));
        frame.getContentPane().add(navbar, BorderLayout.NORTH);

        // Part 2: Bottom
        JPanel bottomPanel = new JPanel(new BorderLayout());
        frame.getContentPane().add(bottomPanel, BorderLayout.CENTER);

        // Part 3: Search Input
        searchField = new JTextField();
        bottomPanel.add(searchField, BorderLayout.NORTH);

        // Search functionality
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                search();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                search();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                search();
            }
        });

        // Part 5: First list
        leftListModel = new DefaultListModel<>();
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

        // Part 6: Second list
        rightListModel = new DefaultListModel<>();
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
                System.out.println("Submit");
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

    private void search() {
        String searchQuery = searchField.getText().toLowerCase();
        List<String> leftListItems = new ArrayList<>();
        List<String> rightListItems = new ArrayList<>();

        for (int i = 0; i < leftListModel.size(); i++) {
            leftListItems.add(leftListModel.get(i));
        }
        for (int i = 0; i < rightListModel.size(); i++) {
            rightListItems.add(rightListModel.get(i));
        }

        List<String> filteredLeftListItems = leftListItems.stream()
                .filter(item -> item.toLowerCase().contains(searchQuery))
                .collect(Collectors.toList());

        List<String> filteredRightListItems = rightListItems.stream()
                .filter(item -> item.toLowerCase().contains(searchQuery))
                .collect(Collectors.toList());

        leftListModel.clear();
        rightListModel.clear();

        for (String item : filteredLeftListItems) {
            leftListModel.addElement(item);
        }
        for (String item : filteredRightListItems) {
            rightListModel.addElement(item);
        }
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
