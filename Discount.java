/**
 * Discount.java
 * Handles computation of applicable discounts such as senior discount and loyalty points.
 * @author Alfonzo Regaspi 
 * @version 1.0
 */
public class Discount {
    private static final double SENIOR_DISCOUNT_RATE = 0.20;
    private static final double VAT_RATE = 0.12;

    public static double computeSeniorDiscount(double subtotal) {
        // Subtotal is VAT exclusive, so the discount is a direct calculation
        return subtotal * SENIOR_DISCOUNT_RATE;
    }

    public static double computeVAT(double amount) {
        return amount * VAT_RATE;
    }

    public static double computeTotal(double subtotal, boolean isSenior) {
        System.out.printf("Subtotal (VAT-Exclusive): ₱%.2f%n", subtotal);
        
        if (isSenior) {
            // For seniors, apply a 20% discount on the VAT exclusive subtotal. The purchase is VAT exempt
            double seniorDiscount = computeSeniorDiscount(subtotal);
            double total = subtotal - seniorDiscount;
            System.out.printf("Senior Discount (20%%): -₱%.2f%n", seniorDiscount);
            System.out.println("Purchase is VAT-Exempt.");
            return total;
        } else {
            // For non seniors, add 12% VAT to the subtotal
            double vat = computeVAT(subtotal);
            double total = subtotal + vat;
            System.out.printf("VAT (12%%): +₱%.2f%n", vat);
            return total;
        }
    }
}
