import java.util.ArrayList;

/**
 * Cart.java
 * Represents a customer's shopping cart.
 * Holds a list of products and their corresponding quantities.
 * Provides methods to add items, compute subtotal, and finalize purchases.
 * 
 * @author Alfonzo Regaspi
 * @version 2.0
 * @date November 25, 2025 - removed display methods, changed addItem/removeItem/getTotalItems
 */
public class Cart {
    private ArrayList<Product> items;
    private ArrayList<Integer> quantities;

    public Cart() {
        items = new ArrayList<>();
        quantities = new ArrayList<>();
    }

    
     // Adds a product to the cart
    public void addItem(Product product, int quantity) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getName().equalsIgnoreCase(product.getName())) {
                quantities.set(i, quantities.get(i) + quantity);
                return;
            }
        }
        items.add(product);
        quantities.add(quantity);
    }

    public void removeItem(String productName) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getName().equalsIgnoreCase(productName)) {
                items.remove(i);
                quantities.remove(i);
                return;
            }
        }
    }

    public void updateQuantity(String productName, int newQty) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getName().equalsIgnoreCase(productName)) {
                quantities.set(i, newQty);
                return;
            }
        }
    }

    
     //Computes subtotal of the cart.
  
    public double computeSubtotal() {
        double subtotal = 0;   // FIXED
        for (int i = 0; i < items.size(); i++) {
            subtotal += items.get(i).getPrice() * quantities.get(i);
        }
        return subtotal;
    }

    public int getTotalItems() {
        int total = 0;
        for (int qty : quantities) {
            total += qty;
        }
        return total;
    }

    public ArrayList<Product> getItems() {
        return items;
    }

    public ArrayList<Integer> getQuantities() {
        return quantities;
    }

    public void clear() {
        items.clear();
        quantities.clear();
    }
}
