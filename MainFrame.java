import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    private Inventory inventory;
    private Cart cart;
    private Map<String, LoyaltyCard> loyaltyCards;

    // Stores panels by name for fast lookup
    private Map<String, JPanel> panelMap = new HashMap<>();

    public MainFrame(Inventory inventory, Cart cart, Map<String, LoyaltyCard> loyaltyCards) {

        this.inventory = inventory;
        this.cart = cart;
        this.loyaltyCards = loyaltyCards;

        setTitle("Convenience Store POS System");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Create panels
        ProductCatalogPanel catalogPanel = new ProductCatalogPanel(this, inventory, cart);
        ShoppingCartPanel cartPanel = new ShoppingCartPanel(this, inventory, cart);
        CheckoutPanel checkoutPanel = new CheckoutPanel(this, inventory, cart, loyaltyCards);
        InventoryManagementPanel inventoryPanel = new InventoryManagementPanel(this, inventory);

        // Store panels in map
        panelMap.put("catalog", catalogPanel);
        panelMap.put("cart", cartPanel);
        panelMap.put("checkout", checkoutPanel);
        panelMap.put("inventory", inventoryPanel);

        // Add panels to card layout
        mainPanel.add(catalogPanel, "catalog");
        mainPanel.add(cartPanel, "cart");
        mainPanel.add(checkoutPanel, "checkout");
        mainPanel.add(inventoryPanel, "inventory");

        add(mainPanel);
        setVisible(true);
    }

   
     //Safe panel lookup
    private JPanel findPanel(String name) {
        return panelMap.get(name);
    }

    
     // Switches pages & auto-refreshes the correct panels
    public void showPage(String name) {

        // Refresh dynamic panels before showing them
        switch (name) {
            case "catalog":
                ((ProductCatalogPanel) findPanel("catalog")).refreshTable();
                break;

            case "cart":
                ((ShoppingCartPanel) findPanel("cart")).refreshTable();
                break;

            case "inventory":
                ((InventoryManagementPanel) findPanel("inventory")).refreshTable();
                break;

            case "checkout":
                // checkout recalculates totals dynamically if needed
                break;
        }

        cardLayout.show(mainPanel, name);
    }
}
