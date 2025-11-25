/**
 * Customer.java
 * Represents a customer in the convenience store.
 * @author Alfonzo Regaspi 
 * @version 1.0
 */
public class Customer {
    private String name; //customer name   
    private boolean isSenior; //Indicates if the customer is a senior citizer
    private LoyaltyCard loyaltyCard;//Loyalty car associated w/ the customer (can be null)
    //Constructors for initializing customer details
    public Customer(String name, boolean isSenior, LoyaltyCard loyaltyCard) {
        this.name = name;
        this.isSenior = isSenior;
        this.loyaltyCard = loyaltyCard;
    }
    //returns customer's name
    public String getName() { return name; }
    //returns true is customer is a senior citizen
    public boolean isSenior() { return isSenior; }
    //Return the customer's loyalty card (can be null)
    public LoyaltyCard getLoyaltyCard() { return loyaltyCard; }

    public void displayCustomerInfo() {
        System.out.println("\n--- Customer Information ---");
        System.out.println("Name: " + name);
        System.out.println("Senior Citizen: " + (isSenior ? "Yes" : "No"));
        // Shows loyalty card details if the customer has one
        if (loyaltyCard != null) {
            System.out.println("Loyalty Card: " + loyaltyCard.getCardNumber());
            System.out.println("Loyalty Points: " + loyaltyCard.getPoints());
        } else {
            System.out.println("No Loyalty Card");
        }
    }
}

