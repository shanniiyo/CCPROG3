import javax.swing.*;
import java.awt.*;

public class CheckoutPanel extends JPanel {

    private StoreGUI frame;
    private Inventory inventory;
    private Cart cart;

    private JTextField nameField;
    private JCheckBox seniorCheckbox;
    private JLabel subtotalLabel;
    private JLabel discountLabel;
    private JLabel totalLabel;
    private JTextField cashField;

    public CheckoutPanel(StoreGUI frame, Inventory inventory, Cart cart) {
        this.frame = frame;
        this.inventory = inventory;
        this.cart = cart;

        setLayout(new BorderLayout());
        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));

        nameField = new JTextField();
        seniorCheckbox = new JCheckBox("Senior Citizen Discount (20%)");
        subtotalLabel = new JLabel("Subtotal: ₱0.00");
        discountLabel = new JLabel("Discount: ₱0.00");
        totalLabel = new JLabel("Total: ₱0.00");
        cashField = new JTextField();

        // Update labels initially
        refreshTotals();

        form.add(new JLabel("Customer Name:"));
        form.add(nameField);

        form.add(new JLabel(""));
        form.add(seniorCheckbox);

        form.add(subtotalLabel);
        form.add(new JLabel(""));

        form.add(discountLabel);
        form.add(new JLabel(""));

        form.add(totalLabel);
        form.add(new JLabel(""));

        form.add(new JLabel("Cash Amount:"));
        form.add(cashField);

        JButton confirmBtn = new JButton("Confirm Purchase");
        confirmBtn.addActionListener(e -> processPurchase());

        JButton backBtn = new JButton("Back to Cart");
        backBtn.addActionListener(e -> frame.showPage("Cart"));

        JPanel bottom = new JPanel();
        bottom.add(confirmBtn);
        bottom.add(backBtn);

        add(form, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        // Recalculate totals when senior checkbox toggled
        seniorCheckbox.addActionListener(e -> refreshTotals());
    }

    private void refreshTotals() {
        double subtotal = cart.computeSubtotal();
        double discount = seniorCheckbox.isSelected() ? subtotal * 0.20 : 0.0;
        double total = subtotal - discount;

        subtotalLabel.setText(String.format("Subtotal: ₱%.2f", subtotal));
        discountLabel.setText(String.format("Discount: ₱%.2f", discount));
        totalLabel.setText(String.format("Total: ₱%.2f", total));
    }

    private void processPurchase() {
        try {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Customer name required.");
                return;
            }

            boolean isSenior = seniorCheckbox.isSelected();

            double cash = Double.parseDouble(cashField.getText().trim());
            double subtotal = cart.computeSubtotal();
            double discount = isSenior ? subtotal * 0.20 : 0.0;
            double total = subtotal - discount;

            if (cash < total) {
                JOptionPane.showMessageDialog(this, "Insufficient payment.");
                return;
            }

            Customer customer = new Customer(name, isSenior, null);

            Transaction t = new Transaction(customer, cart);
            t.processTransaction();

            // Apply loyalty discount if needed - (optional) keep as-is
            // finalize payment
            Receipt receipt = t.finalizePayment(cash);
            if (receipt == null) {
                JOptionPane.showMessageDialog(this, "Payment failed.");
                return;
            }

            // Update inventory stock from cart
            inventory.updateStockFromCart(cart);

            // Clear cart for next transaction
            cart.clear();

            // Show receipt panel via StoreGUI
            double change = cash - total;
            frame.showReceiptPanel(receipt, change);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid cash amount.");
        }
    }
}
