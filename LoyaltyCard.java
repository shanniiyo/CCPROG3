import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * LoyaltyCard.java
 * Represents a membership card that allows point accumulation and redemption.
*/
public class LoyaltyCard {
   private String cardNumber;//Unique membership card number
   private int points;  //Total points stored in the card
   //Constructor for new loyalty cards (starts w/ 0 points)
   public LoyaltyCard(String cardNumber) {
       this.cardNumber = cardNumber;
       this.points = 0;
   }
   //Constructor for loading cards from file (w/ existing points)
   public LoyaltyCard(String cardNumber, int points) {
       this.cardNumber = cardNumber;
       this.points = points;
   }
  
   public String getCardNumber() { return cardNumber; }
   public int getPoints() { return points; }
   public void setPoints(int points) { this.points = points; }
    /**
     * Adds points based on total amount spent.
     * Rule: 1 point per ₱50 spent.
     */
   public void addPoints(double totalAmount) {
       int earned = (int)(totalAmount / 50);
       points += earned;
       System.out.println("Earned " + earned + " point(s). Total points: " + points);
   }
   /**
     * Redeems points for a discount.
     * Returns the amount of points actually redeemed.
     */
   public double redeemPoints(int amountToUse) {
       if (amountToUse <= points) {
           points -= amountToUse;
           System.out.println("Redeemed " + amountToUse + " point(s). Remaining points: " + points);
           return amountToUse;
       } else {
           System.out.println("Insufficient points to redeem.");
           return 0;
       }
   }

   /**
    * Loads loyalty cards from a file into a Map for easy lookup.
    */
   public static Map<String, LoyaltyCard> loadLoyaltyCards(String filePath) {
       List<LoyaltyCard> cards = new ArrayList<>();
       try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
           String line;
           while ((line = br.readLine()) != null) {
               String[] parts = line.split(",");
              //Ensures correct format
               if (parts.length == 2) {
                   String cardNumber = parts[0].trim();
                   int points = Integer.parseInt(parts[1].trim());
                   cards.add(new LoyaltyCard(cardNumber, points));//creates a card and stores it
               }
           }
       } catch (IOException | NumberFormatException e) {
           System.err.println("Error loading loyalty cards: " + e.getMessage());
       }
       // Convert the List to a Map for efficient lookup by card number
       return cards.stream().collect(Collectors.toMap(LoyaltyCard::getCardNumber, card -> card));
   }

   /**
    * Saves the state of all loyalty cards back to the file.
    */
   public static void saveLoyaltyCards(String filePath, Map<String, LoyaltyCard> cards) {
       try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
           for (LoyaltyCard card : cards.values()) {
               bw.write(card.getCardNumber() + "," + card.getPoints());
               bw.newLine();
           }
       } catch (IOException e) {
           System.err.println("Error saving loyalty cards: " + e.getMessage());
       }
   }
}
