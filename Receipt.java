
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Receipt.java
 * Generates and prints a receipt after a transaction.
 * Includes transaction details such as customer, cart, total, and payment.
 * 
 * @author Alfonzo Regaspi
 * @version 4.0
 */
public class Receipt {
    private Customer customer;
    private Cart cart;
    private double paymentAmount;
    private double totalAmount;
    private double change;
    private double subtotal;
    private double vat;
    private double seniorDiscount;
    private double loyaltyDiscount;
    private LocalDateTime transactionDate;
    private String fileName;

    public String getFileName() { return fileName; }
    
    public Customer getCustomer() { return customer; }

    public Receipt(Customer customer, Cart cart, double paymentAmount, double totalAmount,
                   double change, double subtotal, double vat,
                   double seniorDiscount, double loyaltyDiscount) {

        this.customer = customer;
        this.cart = cart;
        this.paymentAmount = paymentAmount;
        this.totalAmount = totalAmount;
        this.change = change;
        this.subtotal = subtotal;
        this.vat = vat;
        this.seniorDiscount = seniorDiscount;
        this.loyaltyDiscount = loyaltyDiscount;
        this.transactionDate = LocalDateTime.now();

        this.fileName = "receipt_" +
                customer.getName().replaceAll("\\s+", "_") + "_" +
                transactionDate.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) +
                ".txt";
    }

    /**
     * Return a nicely formatted receipt string (for display in GUI).
     */
    public String getFormattedReceipt() {
        StringBuilder sb = new StringBuilder();

        sb.append("--- OFFICIAL RECEIPT ---\n");
        sb.append("Date: ").append(transactionDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))).append("\n");
        sb.append("Customer: ").append(customer.getName()).append("\n\n");
        sb.append(String.format("%-20s %5s %10s %10s\n", "Item", "Qty", "Price", "Total"));

        for (int i = 0; i < cart.getItems().size(); i++) {
            Product p = cart.getItems().get(i);
            int qty = cart.getQuantities().get(i);
            double itemTotal = p.getPrice() * qty;

            sb.append(String.format("%-20s %5d %10.2f %10.2f\n", p.getName(), qty, p.getPrice(), itemTotal));
        }

        sb.append("\n");
        sb.append(String.format("%-35s %10.2f\n", "Subtotal:", subtotal));
        if (vat > 0) sb.append(String.format("%-35s %10.2f\n", "VAT:", vat));
        if (seniorDiscount > 0) sb.append(String.format("%-35s %10.2f\n", "Senior Discount:", -seniorDiscount));
        if (loyaltyDiscount > 0) sb.append(String.format("%-35s %10.2f\n", "Loyalty Discount:", -loyaltyDiscount));
        sb.append("----------------------------------------\n");
        sb.append(String.format("%-35s %10.2f\n", "TOTAL:", totalAmount));
        sb.append(String.format("%-35s %10.2f\n", "Paid:", paymentAmount));
        sb.append(String.format("%-35s %10.2f\n", "Change:", change));
        sb.append("----------------------------------------\n");
        sb.append("Thank you for your purchase!\n");

        return sb.toString();
    }

    /**
     * Save receipt text to a file with timestamped fileName.
     */
    public void saveToFile() {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(getFormattedReceipt());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
