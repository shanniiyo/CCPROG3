
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreGUI extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    private Inventory inventory;
    private List<Category> categories;
    private Map<String, LoyaltyCard> loyaltyCards;

    public StoreGUI() {
        // 1. Load Data
         categories = new ArrayList<>(); // You might want to load these from a file too
    inventory = new Inventory("products.txt", categories);
    loyaltyCards = LoyaltyCard.loadLoyaltyCards("loyalty_cards.txt");
    if (loyaltyCards == null) {
        loyaltyCards = new HashMap<>();
    }


        // 2. Setup the main frame
        setTitle(" Store Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 3. Setup CardLayout to switch between panels
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // 4. Create the different panels for your application
        JPanel mainMenuPanel = createMainMenuPanel();
        JPanel inventoryPanel = createInventoryPanel(); // Create the new panel
        // JPanel transactionPanel = createTransactionPanel();

        // 5. Add panels to the main container
        mainPanel.add(mainMenuPanel, "MainMenu");
        mainPanel.add(inventoryPanel, "Inventory"); // Add it to the CardLayout
        // mainPanel.add(transactionPanel, "Transaction");


        // 6. Add the main panel to the frame
        add(mainPanel);

        // 7. Show the main menu first
        cardLayout.show(mainPanel, "MainMenu");
    }

    private JPanel createMainMenuPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        JLabel titleLabel = new JLabel("Store Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridy = 0;
        panel.add(titleLabel, gbc);

        JButton inventoryButton = new JButton("Inventory Management");
        inventoryButton.addActionListener(e -> {
            // Switch to the Inventory panel instead of showing a dialog
            cardLayout.show(mainPanel, "Inventory");
        });
        gbc.gridy = 1;
        panel.add(inventoryButton, gbc);

        JButton transactionButton = new JButton("Customer Transaction");
        transactionButton.addActionListener(e -> {
            // This will eventually switch to the transaction panel
            JOptionPane.showMessageDialog(this, "Customer Transaction coming soon!");
            // cardLayout.show(mainPanel, "Transaction");
});
        gbc.gridy = 2;
        panel.add(transactionButton, gbc);

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));
        gbc.gridy = 3;
        panel.add(exitButton, gbc);

        return panel;
    }

    private JPanel createInventoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Title
        JLabel titleLabel = new JLabel("Inventory Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Table to display products
        String[] columnNames = {"Name", "Price", "Category", "Stock"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable productTable = new JTable(tableModel);

        // Populate the table from the inventory
        if (inventory != null && inventory.getProducts() != null) {
            for (Product product : inventory.getProducts()) {
                Object[] row = {
                    product.getName(),
                    String.format("%.2f", product.getPrice()),
                    product.getCategory().getName(),
                    product.getQuantity()
                };
                tableModel.addRow(row);
            }
        }

        panel.add(new JScrollPane(productTable), BorderLayout.CENTER);

        // Bottom panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton restockButton = new JButton("Restock Product");
        restockButton.addActionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(panel, "Please select a product to restock.", "No Product Selected", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // The view row might not match the model row if sorted
            int modelRow = productTable.convertRowIndexToModel(selectedRow);
            String productName = (String) tableModel.getValueAt(modelRow, 0);

            String quantityStr = JOptionPane.showInputDialog(panel, "Enter quantity to add for " + productName + ":", "Restock Product", JOptionPane.QUESTION_MESSAGE);
            if (quantityStr != null && !quantityStr.trim().isEmpty()) {
                try {
                    int quantity = Integer.parseInt(quantityStr);
                    if (quantity <= 0) {
                        JOptionPane.showMessageDialog(panel, "Please enter a positive quantity.", "Invalid Quantity", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Restock in the inventory object
                    inventory.restockProduct(productName, quantity);

                    // Update the table model
                    int currentStock = (int) tableModel.getValueAt(modelRow, 3);
                    tableModel.setValueAt(currentStock + quantity, modelRow, 3);

                    // Save changes to file
                    inventory.saveProductsToFile();

                    JOptionPane.showMessageDialog(panel, productName + " has been restocked successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "Invalid quantity. Please enter a whole number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttonPanel.add(restockButton);

        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "MainMenu"));
        buttonPanel.add(backButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    public static void main(String[] args) {
        // Ensure the GUI is created on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            StoreGUI storeGUI = new StoreGUI();
            storeGUI.setVisible(true);
        });
    }
}
