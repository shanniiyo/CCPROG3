import java.util.ArrayList;

/**
 * Supplier.java
 * Represents a supplier who provides products to the store.
 * A supplier can restock products and display the products they supply.
 *  
 * @author Shan Dipatuan
 * @version 3
 */

public class Supplier {
    private String name;
    private String contactNumber;
    private ArrayList<Product> suppliedProducts;

    public Supplier(String name, String contactNumber) {
        this.name = name;
        this.contactNumber = contactNumber;
        this.suppliedProducts = new ArrayList<>();
    }

    public String getName() { return name; }
    public String getContactNumber() { return contactNumber; }
    public ArrayList<Product> getSuppliedProducts() { return suppliedProducts; }

    // Add a product that the supplier provides
    public void addProduct(Product product) {
        suppliedProducts.add(product);
    }

    // Restock a product in the inventory
    public void restockProduct(Inventory inventory, String productName, int quantity) {
        Product product = inventory.getName(productName);
        if (product != null && suppliedProducts.contains(product)) {
            inventory.restockProduct(productName, quantity);
            System.out.println("Supplier " + name + " restocked " + quantity + " units of " + productName);
        } else {
            System.out.println("Cannot restock. Either product not in inventory or not supplied by " + name);
        }
    }

    public void displaySupplierInfo() {
        System.out.println("\n--- Supplier Info ---");
        System.out.println("Name: " + name);
        System.out.println("Contact: " + contactNumber);
        System.out.println("Supplied Products:");
        for (Product p : suppliedProducts) {
            System.out.println("- " + p.getName());
        }
    }
}  
