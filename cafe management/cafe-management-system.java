// MenuItem.java
public class MenuItem {
    private int id;
    private String name;
    private double price;
    private String category;

    public MenuItem(int id, String name, double price, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    // Getters and setters
    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }
    
    @Override
    public String toString() {
        return String.format("%-5d%-20s$%-10.2f%-15s", id, name, price, category);
    }
}

// Order.java
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
    private int orderId;
    private List<MenuItem> items;
    private Date orderTime;
    private double totalAmount;
    private String status;

    public Order(int orderId) {
        this.orderId = orderId;
        this.items = new ArrayList<>();
        this.orderTime = new Date();
        this.totalAmount = 0.0;
        this.status = "Pending";
    }

    public void addItem(MenuItem item) {
        items.add(item);
        totalAmount += item.getPrice();
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getOrderId() { return orderId; }
    public List<MenuItem> getItems() { return items; }
    public Date getOrderTime() { return orderTime; }
    public double getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
}

// CafeManager.java
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CafeManager {
    private List<MenuItem> menu;
    private Map<Integer, Order> orders;
    private int nextOrderId;
    private double dailyRevenue;

    public CafeManager() {
        this.menu = new ArrayList<>();
        this.orders = new HashMap<>();
        this.nextOrderId = 1;
        this.dailyRevenue = 0.0;
        initializeMenu();
    }

    private void initializeMenu() {
        // Add some default menu items
        addMenuItem(new MenuItem(1, "Espresso", 3.50, "Beverages"));
        addMenuItem(new MenuItem(2, "Cappuccino", 4.50, "Beverages"));
        addMenuItem(new MenuItem(3, "Latte", 4.00, "Beverages"));
        addMenuItem(new MenuItem(4, "Croissant", 3.00, "Pastries"));
        addMenuItem(new MenuItem(5, "Muffin", 2.50, "Pastries"));
    }

    public void addMenuItem(MenuItem item) {
        menu.add(item);
    }

    public Order createNewOrder() {
        Order order = new Order(nextOrderId++);
        orders.put(order.getOrderId(), order);
        return order;
    }

    public void completeOrder(int orderId) {
        Order order = orders.get(orderId);
        if (order != null) {
            order.setStatus("Completed");
            dailyRevenue += order.getTotalAmount();
        }
    }

    public void displayMenu() {
        System.out.println("\n=== CAFE MENU ===");
        System.out.println("ID   Name                Price      Category");
        System.out.println("----------------------------------------");
        for (MenuItem item : menu) {
            System.out.println(item);
        }
    }

    public void displayOrder(int orderId) {
        Order order = orders.get(orderId);
        if (order != null) {
            System.out.println("\n=== ORDER #" + orderId + " ===");
            System.out.println("Status: " + order.getStatus());
            System.out.println("Time: " + order.getOrderTime());
            System.out.println("Items:");
            for (MenuItem item : order.getItems()) {
                System.out.println("- " + item.getName() + " ($" + item.getPrice() + ")");
            }
            System.out.println("Total Amount: $" + String.format("%.2f", order.getTotalAmount()));
        }
    }

    public MenuItem findMenuItem(int id) {
        return menu.stream()
                .filter(item -> item.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public double getDailyRevenue() {
        return dailyRevenue;
    }
}

// Main.java
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        CafeManager cafe = new CafeManager();
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\n=== CAFE MANAGEMENT SYSTEM ===");
            System.out.println("1. Display Menu");
            System.out.println("2. Create New Order");
            System.out.println("3. View Order");
            System.out.println("4. Complete Order");
            System.out.println("5. View Daily Revenue");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            
            int choice = scanner.nextInt();
            
            switch (choice) {
                case 1:
                    cafe.displayMenu();
                    break;
                    
                case 2:
                    Order newOrder = cafe.createNewOrder();
                    while (true) {
                        cafe.displayMenu();
                        System.out.print("\nEnter item ID (0 to finish): ");
                        int itemId = scanner.nextInt();
                        if (itemId == 0) break;
                        
                        MenuItem item = cafe.findMenuItem(itemId);
                        if (item != null) {
                            newOrder.addItem(item);
                            System.out.println(item.getName() + " added to order.");
                        } else {
                            System.out.println("Invalid item ID!");
                        }
                    }
                    cafe.displayOrder(newOrder.getOrderId());
                    break;
                    
                case 3:
                    System.out.print("Enter order ID: ");
                    int viewOrderId = scanner.nextInt();
                    cafe.displayOrder(viewOrderId);
                    break;
                    
                case 4:
                    System.out.print("Enter order ID to complete: ");
                    int completeOrderId = scanner.nextInt();
                    cafe.completeOrder(completeOrderId);
                    System.out.println("Order #" + completeOrderId + " completed!");
                    break;
                    
                case 5:
                    System.out.printf("Daily Revenue: $%.2f%n", cafe.getDailyRevenue());
                    break;
                    
                case 6:
                    System.out.println("Thank you for using Cafe Management System!");
                    scanner.close();
                    System.exit(0);
                    
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }
}
