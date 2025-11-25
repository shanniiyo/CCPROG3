import java.util.ArrayList;

/**
 * Cart.java
 * Represents a customer's shopping cart.
 */
public class Cart {
    private ArrayList<Product> items; //List of products in the cart
    private ArrayList<Integer> quantities; //Corresponding quantities for each product

    public Cart() {
        items = new ArrayList<>();
        quantities = new ArrayList<>();
    }

    /**
     * Adds a product to the cart. If product already present, increases the quantity.
     */
    public void addItem(Product product, int quantity) {
        if (quantity <= 0) return;
        //Checks if product already exists
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getName().equalsIgnoreCase(product.getName())) {
                quantities.set(i, quantities.get(i) + quantity);//increases existing quantity
                return;
            }
        }
        items.add(product);//Adds new product and quantity
        quantities.add(quantity);
    }

    /**
     * Remove item by product name (first match).
     */
    public void removeItem(String productName) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getName().equalsIgnoreCase(productName)) {
                items.remove(i);
                quantities.remove(i);
                return;
            }
        }
    }

    /**
     * Update quantity of a product already in the cart.
     */
    public void updateQuantity(String productName, int newQty) {
        if (newQty < 0) return; //ignores invalid quantities
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getName().equalsIgnoreCase(productName)) {
                if (newQty == 0) {
                    // remove item
                    items.remove(i);
                    quantities.remove(i);
                } else {
                    quantities.set(i, newQty); //updates quantity
                }
                return;
            }
        }
    }

    /**
     * Compute subtotal (sum price * qty).
     */
    public double computeSubtotal() {
        double total = 0;
        for (int i = 0; i < items.size(); i++) {
            total += items.get(i).getPrice() * quantities.get(i);
        }
        return total;
    }
    //Returns number of different products in the cart
    public int getTotalItems() {
        return items.size();
    }
    //Return list of all products
    public ArrayList<Product> getItems() {
        return items;
    }
    //Return list of all products
    public ArrayList<Integer> getQuantities() {
        return quantities;
    }
    //clears the cart
    public void clear() {
        items.clear();
        quantities.clear();
    }
}
