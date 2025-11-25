import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class InventoryManagementPanel extends JPanel {

    private MainFrame frame;
    private Inventory inventory;
    private JTable table;

    public InventoryManagementPanel(MainFrame frame, Inventory inventory) {
        this.frame = frame;
        this.inventory = inventory;

        setLayout(new BorderLayout());

        table = new JTable();
        refreshTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel();

        JButton addBtn = new JButton("Add Product");
        JButton restockBtn = new JButton("Restock");
        JButton deleteBtn = new JButton("Delete");
        JButton backBtn = new JButton("Back");

        addBtn.addActionListener(e -> addProduct());
        restockBtn.addActionListener(e -> restock());
        deleteBtn.addActionListener(e -> delete());
        backBtn.addActionListener(e -> frame.showPage("catalog"));

        bottom.add(addBtn);
        bottom.add(restockBtn);
        bottom.add(deleteBtn);
        bottom.add(backBtn);

        add(bottom, BorderLayout.SOUTH);
    }

    public void refreshTable() {

        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Name", "Price", "Qty", "Category", "Brand", "Expiry"}, 0);

        for (Product p : inventory.getProducts()) {
            model.addRow(new Object[]{
                    p.getName(),
                    p.getPrice(),
                    p.getQuantity(),
                    p.getCategory().getName(),
                    p.getBrand(),
                    p.getExpiryDate()
            });
        }

        table.setModel(model);
    }

    private void addProduct() {
        try {
            String name = JOptionPane.showInputDialog("Enter Product Name:");
            if (name == null || name.isEmpty()) return;

            double price = Double.parseDouble(JOptionPane.showInputDialog("Enter Price:"));
            int qty = Integer.parseInt(JOptionPane.showInputDialog("Enter Initial Quantity:"));
            String category = JOptionPane.showInputDialog("Enter Category:");
            String brand = JOptionPane.showInputDialog("Enter Brand:");
            String expiry = JOptionPane.showInputDialog("Enter Expiry Date (YYYY-MM-DD):");

            Product p = new Product(name, price, qty, new Category(category), brand, expiry);
            inventory.addProduct(p);
            inventory.saveProductsToFile();
            refreshTable();

            JOptionPane.showMessageDialog(this, "Product added successfully!");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid input.");
        }
    }

    private void restock() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a product to restock.");
            return;
        }

        String name = table.getValueAt(row, 0).toString();
        String qtyStr = JOptionPane.showInputDialog("Enter quantity to add:");
        if (qtyStr == null) return;

        try {
            int qty = Integer.parseInt(qtyStr);
            if (qty <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be positive.");
                return;
            }

            inventory.restockProduct(name, qty);
            inventory.saveProductsToFile();
            refreshTable();

            JOptionPane.showMessageDialog(this, "Restocked successfully!");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid quantity.");
        }
    }

    private void delete() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a product to delete.");
            return;
        }

        String name = table.getValueAt(row, 0).toString();

        int confirm = JOptionPane.showConfirmDialog(
                this, "Delete " + name + " ?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        inventory.getProducts().removeIf(p -> p.getName().equalsIgnoreCase(name));
        inventory.saveProductsToFile();
        refreshTable();

        JOptionPane.showMessageDialog(this, "Product deleted.");
    }
}
