import java.util.ArrayList;

/**
 * Cart.java
 * Represents a customer's shopping cart.
 * Holds a list of products and their corresponding quantities.
 * Provides methods to add items, compute subtotal, and finalize purchases.
 * 
 * @author Alfonzo Regaspi
 * @version 2.0
 */

public class Cart {
    private ArrayList<Product> items;
    private ArrayList<Integer> quantities;

    public Cart() {
        items = new ArrayList<>();
        quantities = new ArrayList<>();
    }

 /**
 * Adds a product to the cart if sufficient stock is available.
 * 
 * @param product  the product to add
 * @param quantity the quantity to be added
 */
    public void addItem(Product product, int quantity) {
        if (product.getQuantity() >= quantity) {
            items.add(product);
            quantities.add(quantity);
            System.out.println(quantity + " x " + product.getName() + " added to cart.");
        } else {
            System.out.println("Insufficient stock for " + product.getName());
        }
    }

    public double computeSubtotal() {
        double total = 0;
        for (int i = 0; i < items.size(); i++) {
            total += items.get(i).getPrice() * quantities.get(i);
        }
        return total;
    }

    public void displayCart() {
        System.out.println("\n--- Cart Items ---");
        for (int i = 0; i < items.size(); i++) {
            Product p = items.get(i);
            System.out.printf("%d x %s - ₱%.2f each%n", quantities.get(i), p.getName(), p.getPrice());
        }
        System.out.printf("Subtotal: ₱%.2f%n", computeSubtotal());
    }

    // reduce products
    public void finalizePurchase() {
        for (int i = 0; i < items.size(); i++) {
            Product product = items.get(i);
            int quantity = quantities.get(i);
            product.reduceStock(quantity);
        }
    }

    public int getTotalItems() { return items.size(); }

    // Getters for Receipt - Shan
    public ArrayList<Product> getItems()
    { 
        return items; 
    }
    public ArrayList<Integer> getQuantities() 
    { 
        return quantities;
    }
}
