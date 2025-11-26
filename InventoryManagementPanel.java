import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import model.Inventory;
import model.Product;
import model.Category;

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
        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");
        JButton backBtn = new JButton("Back");

        addBtn.addActionListener(e -> addProduct());
        restockBtn.addActionListener(e -> restock());
        editBtn.addActionListener(e -> edit());
        deleteBtn.addActionListener(e -> delete());
        backBtn.addActionListener(e -> frame.showPage("catalog"));

        bottom.add(addBtn);
        bottom.add(restockBtn);
        bottom.add(editBtn);
        bottom.add(deleteBtn);
        bottom.add(backBtn);

        add(bottom, BorderLayout.SOUTH);
    }

    private void refreshTable() {

        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Name", "Price", "Qty", "Category", "Brand"}, 0);

        
        for (Product p : inventory.getProducts()) {
            model.addRow(new Object[]{
                    p.getName(),
                    p.getPrice(),
                    p.getQuantity(),
                    p.getCategory().getName(),
                    p.getBrand()
            });
        }

        table.setModel(model);
    }

    private void addProduct() {
        try {
            String name = JOptionPane.showInputDialog("Name:");
            double price = Double.parseDouble(JOptionPane.showInputDialog("Price:"));
            int qty = Integer.parseInt(JOptionPane.showInputDialog("Quantity:"));
            String category = JOptionPane.showInputDialog("Category:");
            String brand = JOptionPane.showInputDialog("Brand:");
            String expiry = JOptionPane.showInputDialog("Expiry Date (YYYY-MM-DD):");

            Product p = new Product(name, price, qty, new Category(category), brand, expiry);
            inventory.addProduct(p);
            inventory.saveProductsToFile();
            refreshTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid input.");
        }
    }

    private void restock() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        String name = table.getValueAt(row, 0).toString();
        String qtyStr = JOptionPane.showInputDialog("Add quantity:");

        try {
            int qty = Integer.parseInt(qtyStr);
            inventory.restockProduct(name, qty);
            inventory.saveProductsToFile();
            refreshTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid qty.");
        }
    }

    private void edit() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        String name = table.getValueAt(row, 0).toString();
        Product p = inventory.findProductByName(name).orElse(null);

        if (p == null) return;

        String newPriceStr = JOptionPane.showInputDialog("New price:", p.getPrice());
        double newPrice = Double.parseDouble(newPriceStr);

        p.setPrice(newPrice);
        inventory.saveProductsToFile();
        refreshTable();
    }

    private void delete() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        String name = table.getValueAt(row, 0).toString();
        inventory.getProducts().removeIf(p -> p.getName().equalsIgnoreCase(name));
        inventory.saveProductsToFile();
        refreshTable();
    }
}
