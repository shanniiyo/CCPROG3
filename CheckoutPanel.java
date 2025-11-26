
import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * CheckoutPanel.java
 * Represents the GUI panel for the checkout process.
 * This panel allows the user to enter customer details (name, senior status, loyalty card),
 * view the final purchase summary (subtotal, discounts, VAT, total),
 * and enter the cash amount to finalize the transaction. It orchestrates the final
 * steps of a purchase before generating a receipt.
 *
 * @author Alfonzo Regaspi 
 * @version 2.0
 */


public class CheckoutPanel extends JPanel {

    private StoreGUI frame;
    private Inventory inventory;
    private Cart cart;
    private Map<String, LoyaltyCard> loyaltyCards; 

    private JTextField nameField;
    private JCheckBox seniorCheckbox;
    private JLabel subtotalLabel;
    private JLabel discountLabel;
    private JLabel vatLabel; 
    private JLabel totalLabel;
    private JTextField cashField;
    private JTextField loyaltyCardField; 

    public CheckoutPanel(StoreGUI frame, Inventory inventory, Cart cart, Map<String, LoyaltyCard> loyaltyCards) {
        this.frame = frame;
        this.inventory = inventory;
        this.cart = cart;
        this.loyaltyCards = loyaltyCards; // Initialize loyalty cards map

        setLayout(new BorderLayout());
        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));

        nameField = new JTextField();
        seniorCheckbox = new JCheckBox("Senior Citizen Discount (20%)");
        subtotalLabel = new JLabel("Subtotal: ₱0.00");
        discountLabel = new JLabel("Discount: ₱0.00");
        vatLabel = new JLabel("VAT (12%): ₱0.00"); 
        totalLabel = new JLabel("Total: ₱0.00");
        cashField = new JTextField();
        loyaltyCardField = new JTextField(); 

        // Update labels initially
        refreshTotals();

        form.add(new JLabel("Customer Name:"));
        form.add(nameField);

        form.add(new JLabel(""));
        form.add(seniorCheckbox);

        form.add(new JLabel("Loyalty Card Number:")); 
        form.add(loyaltyCardField); 
        form.add(subtotalLabel);
        form.add(new JLabel(""));

        form.add(discountLabel);
        form.add(new JLabel(""));

        form.add(vatLabel);
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

    public void refreshTotals() { 
        double subtotal = cart.computeSubtotal(); // This is VAT exclusive
        double total;
        double seniorDiscount = 0;
        double vat = 0;

        subtotalLabel.setText(String.format("Subtotal: ₱%.2f", subtotal));

        if (seniorCheckbox.isSelected()) {
            // For seniors, apply 20% discount, and they are VAT exempt.
            seniorDiscount = Discount.computeSeniorDiscount(subtotal);
            total = subtotal - seniorDiscount;
            vat = 0; // No VAT for seniors
        } else {
            // For non seniors, add 12% VAT.
            vat = Discount.computeVAT(subtotal);
            total = subtotal + vat;
            seniorDiscount = 0; // No senior discount
        }

        discountLabel.setText(String.format("Discount: -₱%.2f", seniorDiscount));
        vatLabel.setText(String.format("VAT (12%%): +₱%.2f", vat));
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

            // Loyalty Card Handling 
            String loyaltyCardNumber = loyaltyCardField.getText().trim();
            LoyaltyCard loyaltyCard = null;
            if (!loyaltyCardNumber.isEmpty()) {
                loyaltyCard = loyaltyCards.get(loyaltyCardNumber);
                if (loyaltyCard == null) {
                    JOptionPane.showMessageDialog(this, "Loyalty card not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            Customer customer = new Customer(name, isSenior, loyaltyCard);
            Transaction t = new Transaction(customer, cart);
            t.processTransaction(); // Calculates subtotal, VAT, and senior discount

            // Redeem Points 
            if (loyaltyCard != null && loyaltyCard.getPoints() > 0) {
                String pointsStr = JOptionPane.showInputDialog(this,
                        "You have " + loyaltyCard.getPoints() + " points. How many to redeem? (1 point = ₱1)",
                        "Redeem Points", JOptionPane.QUESTION_MESSAGE);
                if (pointsStr != null && !pointsStr.isEmpty()) {
                    try {
                        int pointsToRedeem = Integer.parseInt(pointsStr);
                        if (pointsToRedeem > 0) {
                            double discount = loyaltyCard.redeemPoints(pointsToRedeem);
                            t.applyLoyaltyDiscount(discount);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Invalid number for points.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

            double totalAmount = t.getTotalAmount();
            if (cash < totalAmount) {
                JOptionPane.showMessageDialog(this, "Insufficient payment. Amount due: ₱" + String.format("%.2f", totalAmount));
                // If payment fails, refund the points that were just redeemed
                if (loyaltyCard != null && t.getLoyaltyDiscount() > 0) {
                    loyaltyCard.setPoints(loyaltyCard.getPoints() + (int) t.getLoyaltyDiscount());
                }
                return;
            }

            // Finalize payment and get receipt
            Receipt receipt = t.finalizePayment(cash);
            if (receipt == null) {
                JOptionPane.showMessageDialog(this, "Payment failed.");
                return;
            }

            // Add Points for Purchase
            if (loyaltyCard != null) {
                loyaltyCard.addPoints(t.getTotalAmount()); // Add points based on final amount paid
                LoyaltyCard.saveLoyaltyCards("loyalty_cards.txt", loyaltyCards); // Save updated points
            }

            // Update inventory stock from cart
            inventory.updateStockFromCart(cart);

            // Clear cart for next transaction
            cart.clear();

            // Show receipt panel via StoreGUI
            frame.showReceiptPanel(receipt, t.getChange());

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid cash amount.");
        }
    }
}
