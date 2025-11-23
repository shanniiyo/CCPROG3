/**
 * Payment.java
 * Represents a customer's payment in a transaction.
 * Stores and retrieves the payment amount.
 * 
 * author Shan Dipatuan
 * version 1.0
 */

public class Payment {
    private double amount;

    /**
     * Initializes a payment with a given amount.
     * 
     * @param amount the amount paid by the customer
     */
    public Payment(double amount) {
        this.amount = amount;
    }

    public double getAmount() { return amount; }

    public void setAmount(double amount) { this.amount = amount; }
}
