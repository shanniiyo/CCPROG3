import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class CheckoutPanel extends JPanel {

    private MainFrame frame;
    private Inventory inventory;
    private Cart cart;
    private Map<String, LoyaltyCard> loyaltyCards;

    private JTextField nameField;
    private JCheckBox seniorCheckbox;
    private JLabel subtotalLabel;
    private JLabel discountLabel;
    private JLabel totalLabel;
    private JTextField cashField;

    public CheckoutPanel(MainFrame frame, Inventory inventory, Cart cart, Map<String, LoyaltyCard> loyaltyCards) {
        this.frame = frame;
        this.inventory = inventory;
        this.cart = cart;
        this.loyaltyCards = loyaltyCards;

        setLayout(new BorderLayout());

        JPanel form = new JPanel();
        form.setLayout(new GridLayout(10, 2, 5, 5));

        nameField = new JTextField();
        seniorCheckbox = new JCheckBox("Senior Citizen Discount?");
        subtotalLabel = new JLabel("Subtotal: ₱0.00");
        discountLabel = new JLabel("Discount: ₱0.00");
        totalLabel = new JLabel("Total: ₱0.00");
        cashField = new JTextField();

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

        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> frame.showPage("cart"));

        JPanel bottom = new JPanel();
        bottom.add(confirmBtn);
        bottom.add(backBtn);

        add(form, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        // Update totals when senior checkbox is clicked
        seniorCheckbox.addActionListener(e -> refreshTotals());
    }

    private void refreshTotals() {
        double subtotal = cart.computeSubtotal();
        double discount = seniorCheckbox.isSelected() ? subtotal * 0.20 : 0.00;
        double total = subtotal - discount;

        subtotalLabel.setText(String.format("Subtotal: ₱%.2f", subtotal));
        discountLabel.setText(String.format("Discount: ₱%.2f", discount));
        totalLabel.setText(String.format("Total: ₱%.2f", total));
    }

    private void processPurchase() {
        try {
            String name = nameField.getText();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Customer name required.");
                return;
            }

            boolean isSenior = seniorCheckbox.isSelected();

            double subtotal = cart.computeSubtotal();
            double discount = isSenior ? subtotal * 0.20 : 0.00;
            double total = subtotal - discount;

            double cash = Double.parseDouble(cashField.getText());

            if (cash < total) {
                JOptionPane.showMessageDialog(this, "Insufficient payment.");
                return;
            }

            // Create customer (no loyalty for now)
            Customer customer = new Customer(name, isSenior, null);

            // Process transaction
            Transaction t = new Transaction(customer, cart);
            t.processTransaction();

            double change = cash - t.getTotalAmount();
            Receipt receipt = t.finalizePayment(cash);

            // Update inventory stock
            inventory.updateStockFromCart(cart);

            JOptionPane.showMessageDialog(this, "Purchase complete.");

            // Show receipt panel
            ReceiptPanel rp = new ReceiptPanel(frame, receipt, change);
            frame.add(rp, "receipt");
            frame.showPage("receipt");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}
