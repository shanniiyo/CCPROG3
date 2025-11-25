import java.util.*;

public class Main {
    public static void main(String[] args) {

        // File paths
        String productsFile = "products.txt";
        String loyaltyFile = "loyalty_cards.txt";

        // Sample categories (same as StoreApp)
        List<Category> categories = Arrays.asList(
                new Category("Food"),
                new Category("Drinks"),
                new Category("Toiletries"),
                new Category("Cleaning Products"),
                new Category("Medications")
        );

        // Load inventory and loyalty cards
        Inventory inventory = new Inventory(productsFile, categories);
        Cart cart = new Cart();
        Map<String, LoyaltyCard> loyaltyCards = LoyaltyCard.loadLoyaltyCards(loyaltyFile);

        // Launch GUI
        new MainFrame(inventory, cart, loyaltyCards);
    }
}
