import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class UI {
    private static final int MAX_ITEMS = 6;
    private static JList<String> leftList, rightList;
    private static DefaultListModel<String> leftModel, rightModel;
    private static JTextField searchTextField;

    public static void start() {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Beast App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Part 1: Navbar
        JPanel navbar = new JPanel();
        navbar.setLayout(new BorderLayout());
        JLabel appTitle = new JLabel("Beast App");
        appTitle.setHorizontalAlignment(SwingConstants.RIGHT);
        navbar.add(appTitle, BorderLayout.EAST);
        // Add student name and profile image here
        frame.add(navbar, BorderLayout.NORTH);

        // Part 2: Bottom Panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());

        // Part 3: Search Input
        searchTextField = new JTextField();
        bottomPanel.add(searchTextField, BorderLayout.NORTH);

        // Part 4: Two lists in the middle
        leftModel = new DefaultListModel<>();
        leftModel.addElement("Item 1");
        leftModel.addElement("Item 2");
        leftModel.addElement("Item 3");
        leftModel.addElement("Item 4");
        leftList = new JList<>(leftModel);
        leftList.setDragEnabled(true);
        leftList.setTransferHandler(new ListItemTransferHandler());
        leftList.setCellRenderer(new ListItemRenderer());
        JScrollPane leftScrollPane = new JScrollPane(leftList);

        rightModel = new DefaultListModel<>();
        rightList = new JList<>(rightModel);
        rightList.setDragEnabled(true);
        rightList.setTransferHandler(new ListItemTransferHandler());
        rightList.setCellRenderer(new ListItemRenderer());
        JScrollPane rightScrollPane = new JScrollPane(rightList);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftScrollPane, rightScrollPane);
        splitPane.setDividerLocation(200);
        bottomPanel.add(splitPane, BorderLayout.CENTER);

        // Part 7: Right Panel
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());

        // Part 8: Arrow up with a label
        JPanel arrowPanel = new JPanel();
        arrowPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel arrowLabel = new JLabel("↑ Most Priority");
        arrowPanel.add(arrowLabel);
        rightPanel.add(arrowPanel, BorderLayout.NORTH);

        // Part 9: Submit Button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> System.out.println("Submit"));
        rightPanel.add(submitButton, BorderLayout.SOUTH);

        bottomPanel.add(rightPanel, BorderLayout.EAST);

        frame.add(bottomPanel, BorderLayout.CENTER);

        // Search functionality
        searchTextField.getDocument().addDocumentListener(new DocumentListener() {
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

            private void search() {
                String searchText = searchTextField.getText().toLowerCase();
                if (searchText.isEmpty()) {
                    leftList.setModel(leftModel);
                } else {
                    DefaultListModel<String> filteredModel = new DefaultListModel<>();
                    for (int i = 0; i < leftModel.getSize(); i++) {
                        String item = leftModel.getElementAt(i);
                        if (item.toLowerCase().contains(searchText)) {
                            filteredModel.addElement(item);
                        }
                    }
                    leftList.setModel(filteredModel);
                }
            }
        });

        // Double-click event for rightList
        rightList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = rightList.locationToIndex(e.getPoint());
                    leftModel.addElement(rightModel.getElementAt(index));
                    rightModel.remove(index);
                }
            }
        });

        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static class ListItemTransferHandler extends TransferHandler {
        @Override
        public boolean canImport(TransferHandler.TransferSupport info) {
            return info.isDataFlavorSupported(DataFlavor.stringFlavor);
        }

        @Override
        public boolean importData(TransferHandler.TransferSupport info) {
            if (!info.isDrop()) {
                return false;
            }

            if (!info.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                return false;
            }

            JList.DropLocation dl = (JList.DropLocation) info.getDropLocation();
            int index = dl.getIndex();
            String data;

            try {
                data = (String) info.getTransferable().getTransferData(DataFlavor.stringFlavor);
            } catch (Exception e) {
                return false;
            }

            JList<String> list = (JList<String>) info.getComponent();
            DefaultListModel<String> model = (DefaultListModel<String>) list.getModel();

            if (list == rightList) {
                if (model.size() >= MAX_ITEMS) {
                    JOptionPane.showMessageDialog(null, "Can't add more than 6 items");
                    return false;
                }

                if (index == -1) {
                    model.addElement(data);
                } else {
                    model.add(index, data);
                }

                leftModel.removeElement(data);
            } else if (list == leftList) {
                if (index == -1) {
                    model.addElement(data);
                } else {
                    model.add(index, data);
                }

                rightModel.removeElement(data);
            }

            return true;
        }

        @Override
        protected Transferable createTransferable(JComponent c) {
            JList<String> list = (JList<String>) c;
            String value = list.getSelectedValue();
            return new StringSelection(value);
        }

        @Override
        public int getSourceActions(JComponent c) {
            return TransferHandler.MOVE;
        }
    }

    private static class ListItemRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                                                      boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (list == rightList && rightModel.size() == MAX_ITEMS) {
                label.setForeground(Color.GRAY);
            }
            return label;
        }
    }
}
