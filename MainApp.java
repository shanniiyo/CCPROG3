import javax.swing.*;
import java.util.*;

public class MainApp {

    public static void main(String[] args) {

        // Load categories (same as StoreApp)
        List<Category> categories = Arrays.asList(
            new Category("Food"),
            new Category("Drinks"),
            new Category("Toiletries"),
            new Category("Cleaning Products"),
            new Category("Medications")
        );

        // File paths
        String productsFilePath = "products.txt";
        String loyaltyCardsFilePath = "loyalty_cards.txt";

        // Load inventory and loyalty cards
        Inventory inventory = new Inventory(productsFilePath, categories);
        Map<String, LoyaltyCard> loyaltyCards = LoyaltyCard.loadLoyaltyCards(loyaltyCardsFilePath);

        // Create empty cart
        Cart cart = new Cart();

        // 🟦 Start GUI on Swing thread
        SwingUtilities.invokeLater(() -> {
            new MainFrame(inventory, cart, loyaltyCards);
        });
    }
}
