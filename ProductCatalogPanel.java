import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
/*import model.Inventory;
import model.Cart;
import model.Product;
*/
public class ProductCatalogPanel extends JPanel {

    private MainFrame frame;
    private Inventory inventory;
    private Cart cart;

    private JTable table;
    private JTextField searchField;

    public ProductCatalogPanel(MainFrame frame, Inventory inventory, Cart cart) {
        this.frame = frame;
        this.inventory = inventory;
        this.cart = cart;

        setLayout(new BorderLayout());

        
        // Top Search Bar
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


        // Bottom Buttons
        JPanel bottom = new JPanel();

        JButton addBtn = new JButton("Add to Cart");
        JButton cartBtn = new JButton("View Cart");
        JButton adminBtn = new JButton("Inventory Management");

        addBtn.addActionListener(e -> addToCart());
        cartBtn.addActionListener(e -> frame.showPage("cart"));
        adminBtn.addActionListener(e -> frame.showPage("inventory"));

        bottom.add(addBtn);
        bottom.add(cartBtn);
        bottom.add(adminBtn);

        add(bottom, BorderLayout.SOUTH);
    }

    // Refresh table content
    public void refreshTable() {

        String search = searchField.getText().trim().toLowerCase();

        DefaultTableModel model = new DefaultTableModel(
            new String[]{"Name", "Price", "Stock"}, 0
        );

        for (Product p : inventory.getProducts()) {

            if (search.isEmpty() ||
                p.getName().toLowerCase().contains(search)) {

                model.addRow(new Object[]{
                        p.getName(),
                        p.getPrice(),
                        p.getQuantity()
                });
            }
        }

        table.setModel(model);
    }

    private void addToCart() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product.");
            return;
        }

        String name = table.getValueAt(row, 0).toString();

        String qtyStr = JOptionPane.showInputDialog("Enter quantity:");
        if (qtyStr == null) return;

        try {
            int qty = Integer.parseInt(qtyStr);

            Product p = inventory.findProductByName(name).orElse(null);
            if (p == null) return;

            if (qty <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be positive.");
                return;
            }

            if (p.getQuantity() < qty) {
                JOptionPane.showMessageDialog(this, "Insufficient stock.");
                return;
            }

            cart.addItem(p, qty);
            JOptionPane.showMessageDialog(this, "Added to cart!");
            refreshTable();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid quantity.");
        }
    }
}
