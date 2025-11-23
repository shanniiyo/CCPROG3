
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Receipt.java
 * Generates and prints a receipt after a transaction.
 * Includes transaction details such as customer, cart, total, and payment.
 * 
 * @author Shan Dipatuan
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

    public Receipt(Customer customer, Cart cart, double paymentAmount, double totalAmount, double change, double subtotal, double vat, double seniorDiscount, double loyaltyDiscount) {
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
    }

    public void printReceipt() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String filename = "receipt_" + customer.getName().replaceAll("\\s+", "_") + "_" + transactionDate.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".txt";

        try (FileWriter writer = new FileWriter(filename)) {
            String header = "--- OFFICIAL RECEIPT ---\n";
            writer.write(header);
            System.out.print(header);

            String date = "Date: " + dtf.format(transactionDate) + "\n";
            writer.write(date);
            System.out.print(date);

            String custInfo = "Customer: " + customer.getName() + "\n\n";
            writer.write(custInfo);
            System.out.print(custInfo);

            String itemsHeader = String.format("%-20s %5s %10s %10s\n", "Item", "Qty", "Price", "Total");
            writer.write(itemsHeader);
            System.out.print(itemsHeader);

            // Access cart via getters
            for (int i = 0; i < cart.getTotalItems(); i++) {
                Product p = cart.getItems().get(i);
                int qty = cart.getQuantities().get(i);
                double itemTotal = p.getPrice() * qty;
                String itemLine = String.format("%-20s %5d %10.2f %10.2f\n", p.getName(), qty, p.getPrice(), itemTotal);
                writer.write(itemLine);
                System.out.print(itemLine);
            }

            writer.write("\n");
            System.out.print("\n");

            String subtotalLine = String.format("%-35s %10.2f\n", "Subtotal:", this.subtotal);
            writer.write(subtotalLine);
            System.out.print(subtotalLine);

            if (this.vat > 0) {
                String vatLine = String.format("%-35s %10.2f\n", "VAT (12%):", this.vat);
                writer.write(vatLine);
                System.out.print(vatLine);
            }

            if (this.seniorDiscount > 0) {
                String seniorLine = String.format("%-35s %10.2f\n", "Senior Citizen Discount (20%):", -this.seniorDiscount);
                writer.write(seniorLine);
                System.out.print(seniorLine);
            }

            if (this.loyaltyDiscount > 0) {
                String loyaltyLine = String.format("%-35s %10.2f\n", "Loyalty Points Redeemed:", -this.loyaltyDiscount);
                writer.write(loyaltyLine);
                System.out.print(loyaltyLine);
            }

            writer.write("----------------------------------------\n");
            System.out.print("----------------------------------------\n");

            String totalLine = String.format("%-35s %10.2f\n", "TOTAL:", totalAmount);
            writer.write(totalLine);
            System.out.print(totalLine);

            String paidLine = String.format("%-35s %10.2f\n", "Paid:", paymentAmount);
            writer.write(paidLine);
            System.out.print(paidLine);

            String changeLine = String.format("%-35s %10.2f\n", "Change:", change);
            writer.write(changeLine);
            System.out.print(changeLine);

            writer.write("----------------------------------------\n");
            System.out.print("----------------------------------------\n");

            String footer = "Thank you for your purchase!\n";
            writer.write(footer);
            System.out.print(footer);

            System.out.println("Receipt saved to file: " + filename);
        } catch (IOException e) {
            System.out.println("Error saving receipt to file.");
            e.printStackTrace();
        }

        // Add loyalty points if available
        if (customer.getLoyaltyCard() != null) {
            customer.getLoyaltyCard().addPoints(totalAmount);
        }
    }
}
