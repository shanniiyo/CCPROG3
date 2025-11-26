import javax.swing.*;
import java.awt.*;
import java.util.Map;

import model.Inventory;
import model.Cart;
import model.LoyaltyCard;


public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    private Inventory inventory;
    private Cart cart;
    private Map<String, LoyaltyCard> loyaltyCards;


    public MainFrame(Inventory inventory, Cart cart, Map<String, LoyaltyCard> loyaltyCards) {

        this.inventory = inventory;
        this.cart = cart;
        this.loyaltyCards = loyaltyCards;

        setTitle("Convenience Store POS System");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(new ProductCatalogPanel(this, inventory, cart), "catalog");
        mainPanel.add(new ShoppingCartPanel(this, inventory, cart), "cart");
        mainPanel.add(new CheckoutPanel(this, inventory, cart, loyaltyCards), "checkout");
        mainPanel.add(new InventoryManagementPanel(this, inventory), "inventory");

        add(mainPanel);
        setLocationRelativeTo(null);
        setVisible(true);
    }
       
    public void showPage(String name) {
        cardLayout.show(mainPanel, name);
    }

}