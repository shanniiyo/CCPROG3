
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * ProductCatalogPanel.java
 * A GUI panel that displays the store's product catalog to the customer.
 * It features a searchable and scrollable table of all available products,
 * showing details like name, price, and stock. From this panel, users can
 * select products to add to their shopping cart or navigate to the cart view.
 *
 * @author Alfonzo Regaspi
 * @version 2.0
 */

public class ProductCatalogPanel extends JPanel {

    private StoreGUI frame;
    private Inventory inventory;
    private Cart cart;

    private JTable table;
    private JTextField searchField;

    public ProductCatalogPanel(StoreGUI frame, Inventory inventory, Cart cart) {
        this.frame = frame;
        this.inventory = inventory;
        this.cart = cart;

        setLayout(new BorderLayout());

        // Search bar
        JPanel top = new JPanel(new BorderLayout());
        searchField = new JTextField();
        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> refreshTable());

        top.add(searchField, BorderLayout.CENTER);
        top.add(searchBtn, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);

        // Table
        table = new JTable();
        refreshTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Bottom buttons
        JPanel bottom = new JPanel();
        JButton addBtn = new JButton("Add to Cart");
        JButton cartBtn = new JButton("Go to Cart");
        JButton backBtn = new JButton("Back");

        addBtn.addActionListener(e -> addToCart());
        cartBtn.addActionListener(e -> frame.showPage("Cart"));
        backBtn.addActionListener(e -> frame.showPage("MainMenu"));

        bottom.add(addBtn);
        bottom.add(cartBtn);
        bottom.add(backBtn);

        add(bottom, BorderLayout.SOUTH);
    }

    public void refreshTable() {
        String search = searchField.getText().trim().toLowerCase();

        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Name", "Price", "Category", "Stock", "Brand", "Expiry Date"}, 0
        );

        for (Product p : inventory.getProducts()) {
            if (search.isEmpty() || p.getName().toLowerCase().contains(search)) {
                model.addRow(new Object[]{
                        p.getName(),
                        String.format("%.2f", p.getPrice()),
                        p.getCategory().getName(),
                        p.getQuantity(),
                        p.getBrand(),
                        p.getExpiryDate()
                });
            }
        }

        table.setModel(model);
    }

    private void addToCart() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a product first.");
            return;
        }

        String name = table.getValueAt(row, 0).toString();
        Product p = inventory.findProductByName(name).orElse(null);
        if (p == null) return;

        String qtyStr = JOptionPane.showInputDialog("Enter quantity:");
        try {
            int qty = Integer.parseInt(qtyStr);
            if (qty <= 0 || qty > p.getQuantity()) {
                JOptionPane.showMessageDialog(this, "Invalid quantity.");
                return;
            }

            cart.addItem(p, qty);
            JOptionPane.showMessageDialog(this, "Added to cart!");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid quantity.");
        }
    }
}
