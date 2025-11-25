import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ShoppingCartPanel extends JPanel {

    private StoreGUI frame;
    private Inventory inventory;
    private Cart cart;
    private JTable table;

    public ShoppingCartPanel(StoreGUI frame, Inventory inventory, Cart cart) {
        this.frame = frame;
        this.inventory = inventory;
        this.cart = cart;

        setLayout(new BorderLayout());

        table = new JTable();
        refreshTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel();

        JButton removeBtn = new JButton("Remove Item");
        JButton updateBtn = new JButton("Update Quantity");
        JButton checkoutBtn = new JButton("Checkout");
        JButton backBtn = new JButton("Back");

        removeBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) return;
            String name = table.getValueAt(row, 0).toString();
            cart.removeItem(name);
            refreshTable();
        });

        updateBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) return;
            String name = table.getValueAt(row, 0).toString();
            String qtyStr = JOptionPane.showInputDialog(this, "Enter new quantity for " + name + ":");
            if (qtyStr == null) return;
            try {
                int newQty = Integer.parseInt(qtyStr);
                cart.updateQuantity(name, newQty);
                refreshTable();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid quantity.");
            }
        });

        checkoutBtn.addActionListener(e -> frame.showPage("Checkout"));
        backBtn.addActionListener(e -> frame.showPage("Catalog"));

        bottom.add(removeBtn);
        bottom.add(updateBtn);
        bottom.add(checkoutBtn);
        bottom.add(backBtn);

        add(bottom, BorderLayout.SOUTH);
    }

    public void refreshTable() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Product", "Qty", "Price", "Total"}, 0);
        for (int i = 0; i < cart.getItems().size(); i++) {
            Product p = cart.getItems().get(i);
            int qty = cart.getQuantities().get(i);
            model.addRow(new Object[]{p.getName(), qty, p.getPrice(), p.getPrice() * qty});
        }
        table.setModel(model);
    }
}
