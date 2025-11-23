import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class StoreApp {

    public static void main(String[] args) {
        List<Category> categories = new ArrayList<>(Arrays.asList(
            new Category("Food"), new Category("Drinks"), new Category("Toiletries"),
            new Category("Cleaning Products"), new Category("Medications")
        ));

        String productsFilePath = "products.txt";
        String loyaltyCardsFilePath = "loyalty_cards.txt";

        Inventory inventory = new Inventory(productsFilePath, categories);
        Map<String, LoyaltyCard> loyaltyCards = LoyaltyCard.loadLoyaltyCards(loyaltyCardsFilePath);

        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                System.out.println("\n--- Welcome to the Convenience Store ---");
                System.out.println("1. Inventory Management");
                System.out.println("2. Customer Transaction");
                System.out.println("3. Exit");
                System.out.print("Please select an option: ");

                String choice = sc.nextLine();

                switch (choice) {
                    case "1":
                        handleInventoryManagement(sc, inventory, categories);
                        break;
                    case "2":
                        System.out.println("\nStarting Customer Transaction...");
                        handleCustomerTransaction(sc, inventory, loyaltyCards);
                        break;
                    case "3":
                        System.out.println("Exiting the application. Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        }
    }

    private static void handleInventoryManagement(Scanner sc, Inventory inventory, List<Category> categories) {
        while (true) {
            System.out.println("\n--- Inventory Management ---");
            System.out.println("1. Add New Product");
            System.out.println("2. Restock Existing Product");
            System.out.println("3. Update Product Information");
            System.out.println("4. View All Products");
            System.out.println("5. Return to Main Menu");
            System.out.print("Select an option: ");

            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    handleAddProduct(sc, inventory, categories);
                    break;
                case "2":
                    handleRestockProduct(sc, inventory);
                    break;
                case "3":
                    handleUpdateProduct(sc, inventory, categories);
                    break;
                case "4":
                    inventory.displayAllProducts();
                    break;
                case "5":
                    return; // Return to main menu
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void handleAddProduct(Scanner sc, Inventory inventory, List<Category> categories) {
        System.out.println("\n--- Add New Product ---");
        System.out.print("Enter product name: ");
        String name = sc.nextLine();
        System.out.print("Enter price: ");
        double price = Double.parseDouble(sc.nextLine());
        System.out.print("Enter quantity: ");
        int quantity = Integer.parseInt(sc.nextLine());
        System.out.print("Enter category (e.g., Food, Drinks): ");
        String categoryName = sc.nextLine();
        System.out.print("Enter brand: ");
        String brand = sc.nextLine();
        System.out.print("Enter expiry date (YYYY-MM-DD): ");
        String expiryDate = sc.nextLine();

        Category category = categories.stream()
            .filter(c -> c.getName().equalsIgnoreCase(categoryName))
            .findFirst()
            .orElse(new Category(categoryName)); // Creates a new category if not found

        Product newProduct = new Product(name, price, quantity, category, brand, expiryDate);
        inventory.addProduct(newProduct);
        inventory.saveProductsToFile();
        System.out.println("Product added successfully!");
    }

    private static void handleRestockProduct(Scanner sc, Inventory inventory) {
        System.out.println("\n--- Restock Product ---");
        System.out.print("Enter the name of the product to restock: ");
        String name = sc.nextLine();
        System.out.print("Enter the quantity to add: ");
        int quantity = Integer.parseInt(sc.nextLine());

        inventory.restockProduct(name, quantity);
        inventory.saveProductsToFile();
    }

    private static void handleUpdateProduct(Scanner sc, Inventory inventory, List<Category> categories) {
        System.out.println("\n--- Update Product Information ---");
        System.out.print("Enter the name of the product to update: ");
        String name = sc.nextLine();

        Optional<Product> productOpt = inventory.findProductByName(name);
        if (productOpt.isEmpty()) {
            System.out.println("Product not found.");
            return;
        }

        Product product = productOpt.get();
        System.out.println("Found product: " + product.getName());
        System.out.println("What do you want to update?");
        System.out.println("1. Name\n2. Price\n3. Brand\n4. Expiry Date\n5. Category");
        System.out.print("Select an option: ");
        String choice = sc.nextLine();

        switch (choice) {
            case "1":
                System.out.print("Enter new name: ");
                product.setName(sc.nextLine());
                break;
            case "2":
                System.out.print("Enter new price: ");
                product.setPrice(Double.parseDouble(sc.nextLine()));
                break;
            case "3":
                System.out.print("Enter new brand: ");
                product.setBrand(sc.nextLine());
                break;
            case "4":
                System.out.print("Enter new expiry date (YYYY-MM-DD): ");
                product.setExpiryDate(sc.nextLine());
                break;
            case "5":
                System.out.print("Enter new category: ");
                String categoryName = sc.nextLine();
                Category category = categories.stream()
                    .filter(c -> c.getName().equalsIgnoreCase(categoryName))
                    .findFirst()
                    .orElse(new Category(categoryName)); // Creates a new category if not found
                product.setCategory(category);
                break;
            default:
                System.out.println("Invalid option. No changes made.");
                return;
        }

        inventory.saveProductsToFile();
        System.out.println("Product updated successfully!");
    }

    private static void handleCustomerTransaction(Scanner sc, Inventory inventory, Map<String, LoyaltyCard> loyaltyCards) {
        System.out.print("Enter customer name: ");
        String customerName = sc.nextLine();

        System.out.print("Is the customer a senior citizen? (yes/no): ");
        boolean isSenior = sc.nextLine().trim().equalsIgnoreCase("yes");

        LoyaltyCard loyaltyCard = null;
        System.out.print("Does the customer have a loyalty card? (yes/no): ");
        if (sc.nextLine().trim().equalsIgnoreCase("yes")) {
            System.out.print("Enter loyalty card number: ");
            String cardNumber = sc.nextLine().trim();
            if (loyaltyCards.containsKey(cardNumber)) {
                loyaltyCard = loyaltyCards.get(cardNumber);
                System.out.println("Loyalty card found. Points available: " + loyaltyCard.getPoints());
            } else {
                System.out.println("Loyalty card not found.");
            }
        }

        Customer customer = new Customer(customerName, isSenior, loyaltyCard);
        Cart cart = new Cart();

        while (true) {
            System.out.println("\n--- Available Products ---");
            inventory.displayAllProducts();
            System.out.print("Enter product name to add to cart (or type 'done' to finish): ");
            String productName = sc.nextLine();

            if (productName.equalsIgnoreCase("done")) {
                break;
            }

            Optional<Product> productOpt = inventory.findProductByName(productName);
            if (productOpt.isEmpty()) {
                System.out.println("Product not found. Please try again.");
                continue;
            }

            System.out.print("Enter quantity: ");
            int quantity = Integer.parseInt(sc.nextLine());

            Product product = productOpt.get();
            if (product.getQuantity() >= quantity) {
                cart.addItem(product, quantity);
                System.out.println(quantity + "x " + product.getName() + " added to cart.");
            } else {
                System.out.println("Insufficient stock. Available: " + product.getQuantity());
            }
        }

        if (cart.getItems().isEmpty()) {
            System.out.println("Cart is empty. Transaction cancelled.");
            return;
        }

        Transaction transaction = new Transaction(customer, cart);
        transaction.processTransaction(); // Calculates totals and discounts

        // Handle point redemption before final payment
        if (loyaltyCard != null && loyaltyCard.getPoints() > 0) {
            System.out.print("Would you like to redeem points? (yes/no): ");
            if (sc.nextLine().trim().equalsIgnoreCase("yes")) {
                System.out.print("How many points to redeem? (1 point = ₱1): ");
                int pointsToRedeem = Integer.parseInt(sc.nextLine());
                double discountFromPoints = loyaltyCard.redeemPoints(pointsToRedeem);
                transaction.applyLoyaltyDiscount(discountFromPoints);
            }
        }

        System.out.println("\n--- Transaction Summary ---");
        transaction.displaySummary();

        System.out.print("Enter amount paid: ");
        double amountPaid = Double.parseDouble(sc.nextLine());

        Receipt receipt = transaction.finalizePayment(amountPaid);
        if (receipt != null) {
            receipt.printReceipt();
            inventory.updateStockFromCart(cart); // Update stock after successful transaction

            // Add points for the current purchase
            if (loyaltyCard != null) {
                loyaltyCard.addPoints(transaction.getTotalAmount());
                // Save the updated loyalty card data
                LoyaltyCard.saveLoyaltyCards("loyalty_cards.txt", loyaltyCards);
                System.out.println("Loyalty card data saved.");
            }
        }
    }
}