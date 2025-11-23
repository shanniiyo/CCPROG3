public class Transaction {
    private Customer customer;
    private Cart cart;
    private double subtotal;
    private double vat;
    private double seniorDiscount;
    private double loyaltyDiscount;
    private double totalAmount;
    private double change;
    private Receipt receipt;

    public Transaction(Customer customer, Cart cart) {
        this.customer = customer;
        this.cart = cart;
        this.loyaltyDiscount = 0; // Initialize loyalty discount
    }

    public void processTransaction() {
        this.subtotal = cart.computeSubtotal(); // This subtotal is VAT-exclusive.
        
        if (customer.isSenior()) {
            // For seniors, discount is on the VAT-exclusive price, and the transaction is VAT-exempt.
            this.seniorDiscount = Discount.computeSeniorDiscount(this.subtotal);
            this.vat = 0; // Seniors are VAT-exempt
            this.totalAmount = this.subtotal - this.seniorDiscount;
        } else {
            // For non-seniors, add VAT.
            this.seniorDiscount = 0;
            this.vat = Discount.computeVAT(this.subtotal);
            this.totalAmount = this.subtotal + this.vat;
        }
    }

    public void applyLoyaltyDiscount(double discountAmount) {
        if (discountAmount > 0) {
            this.loyaltyDiscount = discountAmount;
            this.totalAmount -= this.loyaltyDiscount;
            System.out.printf("Applied ₱%.2f from loyalty points.%n", discountAmount);
        }
    }

    public void displaySummary() {
        System.out.printf("Subtotal (VAT-Exclusive): ₱%.2f%n", this.subtotal);

        if (customer.isSenior()) {
            System.out.printf("Senior Discount (20%%): -₱%.2f%n", this.seniorDiscount);
            System.out.println("Purchase is VAT-Exempt.");
        } else {
            System.out.printf("VAT (12%%): +₱%.2f%n", this.vat);
        }

        if (this.loyaltyDiscount > 0) {
            System.out.printf("Loyalty Points Redeemed: -₱%.2f%n", this.loyaltyDiscount);
        }

        System.out.printf("Total amount due: ₱%.2f%n", this.totalAmount);
    }

    public Receipt finalizePayment(double paymentAmount) {
        if (paymentAmount < totalAmount) {
            System.out.println("Insufficient payment. Transaction failed.");
            return null; // Insufficient payment
        }
        change = paymentAmount - totalAmount;
        this.receipt = new Receipt(customer, cart, paymentAmount, totalAmount, change, subtotal, vat, seniorDiscount, loyaltyDiscount);
        return this.receipt;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}