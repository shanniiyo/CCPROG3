
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
        if (loyaltyCards == null) 
        {
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

    /**
     * Creates and configures the main menu panel for the application.
     * This panel serves as the central navigation point, providing buttons
     * to access different inventory management and customer transactions, 
     * and option to exit the application.
     */
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

    /**
     * Creates the inventory management panel.
     * Displays a list of all products from the inventory.
     * Controls for adding a new product, restocking an existing product,
     * and returning to the main menu. 
     */
    private JPanel createInventoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Title
        JLabel titleLabel = new JLabel("Inventory Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Table to display products
        String[] columnNames = {"Name", "Price", "Category", "Stock", "Brand", "Expiry Date"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable productTable = new JTable(tableModel);

        // Populate the table from the inventory
        if (inventory != null && inventory.getProducts() != null) {
            for (Product product : inventory.getProducts()) {
                Object[] row = {
                    product.getName(),
                    String.format("%.2f", product.getPrice()),
                    product.getCategory().getName(),
                    product.getQuantity(),
                    product.getBrand(),
                    product.getExpiryDate()
                };
                tableModel.addRow(row);
            }
        }

        panel.add(new JScrollPane(productTable), BorderLayout.CENTER);

        // Bottom panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton addButton = new JButton("Add New Product");
        addButton.addActionListener(e -> {
            // Create a panel for the dialog with fields for the new product
            JPanel addProductPanel = new JPanel(new GridLayout(0, 2, 5, 5));
            JTextField nameField = new JTextField();
            JTextField priceField = new JTextField();
            JTextField quantityField = new JTextField();
            JTextField categoryField = new JTextField();
            JTextField brandField = new JTextField();
            JTextField expiryDateField = new JTextField();

            addProductPanel.add(new JLabel("Name:"));
            addProductPanel.add(nameField);
            addProductPanel.add(new JLabel("Price:"));
            addProductPanel.add(priceField);
            addProductPanel.add(new JLabel("Category:"));
            addProductPanel.add(categoryField);
            addProductPanel.add(new JLabel("Brand:"));
            addProductPanel.add(brandField);
            addProductPanel.add(new JLabel("Initial Stock:"));
            addProductPanel.add(quantityField);
            addProductPanel.add(new JLabel("Expiry Date (YYYY-MM-DD):"));
            addProductPanel.add(expiryDateField);

            int result = JOptionPane.showConfirmDialog(panel, addProductPanel, "Add New Product", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    String name = nameField.getText();
                    double price = Double.parseDouble(priceField.getText());
                    String categoryName = categoryField.getText();
                    int quantity = Integer.parseInt(quantityField.getText());
                    String brand = brandField.getText();
                    String expiryDate = expiryDateField.getText();

                    if (name.trim().isEmpty() || categoryName.trim().isEmpty() || brand.trim().isEmpty() || expiryDate.trim().isEmpty() || price <= 0 || quantity < 0) {
                        JOptionPane.showMessageDialog(panel, "Please fill in all fields with valid values.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Find or create category
                    Category category = categories.stream()
                                                  .filter(c -> c.getName().equalsIgnoreCase(categoryName))
                                                  .findFirst()
                                                  .orElseGet(() -> {
                                                      Category newCat = new Category(categoryName);
                                                      categories.add(newCat);
                                                      return newCat;
                                                  });

                    Product newProduct = new Product(name, price, quantity, category, brand, expiryDate);
                    inventory.addProduct(newProduct);
                    inventory.saveProductsToFile();

                    // Add to table model
                    Object[] row = {
                        newProduct.getName(),
                        String.format("%.2f", newProduct.getPrice()),
                        newProduct.getCategory().getName(),
                        newProduct.getQuantity(),
                        newProduct.getBrand(),
                        newProduct.getExpiryDate()
                    };
                    tableModel.addRow(row);

                    JOptionPane.showMessageDialog(panel, "Product added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "Invalid price or quantity. Please enter valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttonPanel.add(addButton);

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
