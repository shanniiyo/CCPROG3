public class Product {
    private String name;
    private double price;
    private int quantity;
    private Category category;
    private String brand;
    private String expiryDate;

    public Product(String name, double price, int quantity, Category category, String brand, String expiryDate) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.brand = brand;
        this.expiryDate = expiryDate;
    }

    // --- Getters ---
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public Category getCategory() { return category; }
    public String getBrand() { return brand; }
    public String getExpiryDate() { return expiryDate; }

    // --- Setters for updates ---
    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setCategory(Category category) { this.category = category; }
    public void setBrand(String brand) { this.brand = brand; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }

    public void reduceStock(int amount) {
        if (amount > 0 && amount <= this.quantity) {
            this.quantity -= amount;
        }
    }
}