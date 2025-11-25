import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
/*import model.Inventory;
import model.Cart;
import model.Product;
*/
public class ShoppingCartPanel extends JPanel {

    private MainFrame frame;
    private Inventory inventory;
    private Cart cart;
    private JTable table;

    public ShoppingCartPanel(MainFrame frame, Inventory inventory, Cart cart) {
        this.frame = frame;
        this.inventory = inventory;
        this.cart = cart;

        setLayout(new BorderLayout());

        table = new JTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel();

        JButton removeBtn = new JButton("Remove Item");
        JButton updateBtn = new JButton("Update Quantity");
        JButton checkoutBtn = new JButton("Checkout");
        JButton backBtn = new JButton("Back");

        removeBtn.addActionListener(e -> removeItem());
        updateBtn.addActionListener(e -> updateQty());
        checkoutBtn.addActionListener(e -> frame.showPage("checkout"));
        backBtn.addActionListener(e -> frame.showPage("catalog"));

        bottom.add(removeBtn);
        bottom.add(updateBtn);
        bottom.add(checkoutBtn);
        bottom.add(backBtn);

        add(bottom, BorderLayout.SOUTH);

        refreshTable();
    }

    public void refreshTable() {

        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Product", "Qty", "Price", "Total"}, 0
        );

        for (int i = 0; i < cart.getItems().size(); i++) {
            Product p = cart.getItems().get(i);
            int qty = cart.getQuantities().get(i);

            model.addRow(new Object[]{
                    p.getName(),
                    qty,
                    p.getPrice(),
                    p.getPrice() * qty
            });
        }

        table.setModel(model);
    }

    private void removeItem() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        String name = table.getValueAt(row, 0).toString();
        cart.removeItem(name);
        refreshTable();
    }

    private void updateQty() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        String name = table.getValueAt(row, 0).toString();
        String newQtyStr = JOptionPane.showInputDialog("Enter new quantity:");

        if (newQtyStr == null) return;

        try {
            int newQty = Integer.parseInt(newQtyStr);

            if (newQty <= 0) {
                JOptionPane.showMessageDialog(this, "Invalid qty.");
                return;
            }

            cart.updateQuantity(name, newQty);

            refreshTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid qty.");
        }
    }
}
