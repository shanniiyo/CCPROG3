/**
 * Customer.java
 * Represents a customer in the convenience store.
 * @author Alfonzo Regaspi 
 * @version 1.0
 */
public class Customer {
    private String name;
    private boolean isSenior;
    private LoyaltyCard loyaltyCard;

    public Customer(String name, boolean isSenior, LoyaltyCard loyaltyCard) {
        this.name = name;
        this.isSenior = isSenior;
        this.loyaltyCard = loyaltyCard;
    }

    public String getName() { return name; }
    public boolean isSenior() { return isSenior; }
    public LoyaltyCard getLoyaltyCard() { return loyaltyCard; }

    public void displayCustomerInfo() {
        System.out.println("\n--- Customer Information ---");
        System.out.println("Name: " + name);
        System.out.println("Senior Citizen: " + (isSenior ? "Yes" : "No"));
        if (loyaltyCard != null) {
            System.out.println("Loyalty Card: " + loyaltyCard.getCardNumber());
            System.out.println("Loyalty Points: " + loyaltyCard.getPoints());
        } else {
            System.out.println("No Loyalty Card");
        }
    }
}

