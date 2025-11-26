
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Inventory.java
 * Manages the collection of all products available in the store.
 * This class is responsible for loading product data from a file,
 * storing them in a list, and providing methods to access, search,
 * and manage the products. It acts as the central repository for all
 * items that can be sold.
 *
 * @author Shan Dipatuan + Alfonzo Regaspi
 * @version 3.0
 */


public class Inventory {
    private List<Product> products;
    private String productsFilePath;

    public Inventory(String productsFilePath, List<Category> categories) {
        this.productsFilePath = productsFilePath;
        this.products = loadProductsFromFile(categories);
    }

    private List<Product> loadProductsFromFile(List<Category> categories) {
        List<Product> loadedProducts = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(productsFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    String name = parts[0].trim();
                    double price = Double.parseDouble(parts[1].trim());
                    int quantity = Integer.parseInt(parts[2].trim());
                    String categoryName = parts[3].trim();
                    String brand = parts[4].trim();
                    String expiryDate = parts[5].trim();

                    Category category = categories.stream()
                        .filter(c -> c.getName().equalsIgnoreCase(categoryName))
                        .findFirst()
                        .orElse(new Category(categoryName)); // Or handle as an error

                    loadedProducts.add(new Product(name, price, quantity, category, brand, expiryDate));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading products from file: " + e.getMessage());
        }
        return loadedProducts;
    }

    public void saveProductsToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(productsFilePath))) {
            for (Product product : products) {
                String line = String.join(",",
                    product.getName(),
                    String.valueOf(product.getPrice()),
                    String.valueOf(product.getQuantity()),
                    product.getCategory().getName(),
                    product.getBrand(),
                    product.getExpiryDate()
                );
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving products to file: " + e.getMessage());
        }
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void displayAllProducts() {
        System.out.println("--------------------------------------------------------------------------------");
        System.out.printf("%-20s | %-10s | %-8s | %-15s | %-15s%n", "Product", "Price", "Stock", "Category", "Brand");
        System.out.println("--------------------------------------------------------------------------------");

        // Group products by their category
        Map<String, List<Product>> productsByCategory = products.stream()
                .collect(Collectors.groupingBy(p -> p.getCategory().getName()));

        // Iterate over each category and print its products
        productsByCategory.forEach((categoryName, productList) -> {
            System.out.println(); // Add a blank line for spacing
            for (Product product : productList) {
                System.out.printf("%-20s | ₱%-9.2f | %-8d | %-15s | %-15s%n",
                        product.getName(),
                        product.getPrice(),
                        product.getQuantity(),
                        product.getCategory().getName(),
                        product.getBrand());
            }
        });

        System.out.println("--------------------------------------------------------------------------------");
    }

    public Optional<Product> findProductByName(String name) {
        return products.stream()
                .filter(p -> p.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    public void restockProduct(String name, int quantity) {
        Optional<Product> productOpt = findProductByName(name);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setQuantity(product.getQuantity() + quantity);
            System.out.println(name + " restocked. New quantity: " + product.getQuantity());
        } else {
            System.out.println("Product not found in inventory.");
        }
    }

    public void updateStockFromCart(Cart cart) {
        for (int i = 0; i < cart.getItems().size(); i++) {
            Product cartProduct = cart.getItems().get(i);
            int quantityPurchased = cart.getQuantities().get(i);

            // Find the product in inventory by name and reduce its stock
            findProductByName(cartProduct.getName()).ifPresent(inventoryProduct -> {
                inventoryProduct.reduceStock(quantityPurchased);
            });
        }
        saveProductsToFile(); // Save the updated stock levels
    }

    public List<Product> getProducts() {
        return products;
    }

    public Product getName(String productName) {
        for (Product product : products) {
            if (product.getName().equalsIgnoreCase(productName)) {
                return product;
            }
        }
        return null;
    }
}
