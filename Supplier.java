import java.util.ArrayList;

/**
 * Supplier.java
 * Represents a supplier who provides products to the store.
 * A supplier can restock products and display the products they supply.
 *  
 * @author Shan Dipatuan + Alfonzo Regaspi
 * @version 4
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
    public void addProduct(String productName) {
        if(!suppliedProducts.contains(productName)) {
            suppliedProducts.add(productName);
        }
        //suppliedProducts.add(product);
    }

    // Restock a product in the inventory
    public boolean restockProduct(Inventory inventory, String productName, int quantity) {
        if(!suppliedProducts.contains(productName)) {
            System.out.println("Supplier does not provide this product: " + productName);
            return false;
        }
        //product must already exist in inventory
        Product product = inventory.getProductByName(productName);
        if(product != null) {
            return false;
        }
        inventory.restockProduct(productName, quantity);
        return true; //restock successful
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
