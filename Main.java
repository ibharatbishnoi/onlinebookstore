import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

class Book {
    private String title;
    private String author;
    private double price;
    private int stock;

    public Book(String title, String author, double price, int stock) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.stock = stock;
    }

    public double getPrice() {
        return price;
    }
}

class ShoppingCart {
    private List<Book> cartItems;

    public ShoppingCart() {
        this.cartItems = new ArrayList<>();
    }

    public void addBookToCart(Book book) {
        cartItems.add(book);
    }

    public void removeBookFromCart(Book book) {
        cartItems.remove(book);
    }

    public List<Book> getCartItems() {
        return cartItems;
    }
}

class InventoryManager {
    private Map<Book, Integer> bookStock;

    public InventoryManager() {
        this.bookStock = new HashMap<>();
    }

    public void addToInventory(Book book, int quantity) {
        int currentStock = bookStock.getOrDefault(book, 0);
        bookStock.put(book, currentStock + quantity);
    }

    public void removeFromInventory(Book book, int quantity) {
        int currentStock = bookStock.getOrDefault(book, 0);
        if (currentStock >= quantity) {
            bookStock.put(book, currentStock - quantity);
        }
    }

    public int getBookStock(Book book) {
        return bookStock.getOrDefault(book, 0);
    }
}

class Order {
    private static int orderCounter = 1;

    private int orderId;
    private User user;
    private List<Book> orderedBooks;

    public Order(User user, List<Book> orderedBooks) {
        this.orderId = orderCounter++;
        this.user = user;
        this.orderedBooks = orderedBooks;
    }

    public double calculateTotal() {
        double total = 0.0;
        for (Book book : orderedBooks) {
            total += book.getPrice();
        }
        return total;
    }
}

public class Main {
    public static void main(String[] args) {
        Book book1 = new Book("Java Programming", "John Doe", 25.99, 50);
        Book book2 = new Book("Python for Beginners", "Jane Smith", 19.99, 30);

        User user1 = new User("user1", "password1");
        User user2 = new User("user2", "password2");

        ShoppingCart cartUser1 = new ShoppingCart();
        ShoppingCart cartUser2 = new ShoppingCart();

        cartUser1.addBookToCart(book1);
        cartUser1.addBookToCart(book2);

        cartUser2.addBookToCart(book1);

        InventoryManager inventoryManager = new InventoryManager();
        inventoryManager.addToInventory(book1, 50);
        inventoryManager.addToInventory(book2, 30);

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService.submit(() -> {
            Order order1 = new Order(user1, cartUser1.getCartItems());
            double total1 = order1.calculateTotal();
            inventoryManager.removeFromInventory(book1, 1);
            inventoryManager.removeFromInventory(book2, 1);
            System.out.println("Order 1 Total: $" + total1);
        });

        executorService.submit(() -> {
            Order order2 = new Order(user2, cartUser2.getCartItems());
            double total2 = order2.calculateTotal();
            inventoryManager.removeFromInventory(book1, 1);
            System.out.println("Order 2 Total: $" + total2);
        });

        executorService.shutdown();
    }
}
