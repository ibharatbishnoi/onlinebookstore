import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class User {
    private String username;
    private String password;
    // Other user profile details

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and setters for user profile details
}

class Book {
    private String title;
    private String author;
    private double price;
    private int availability;

    public Book(String title, String author, double price, int availability) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.availability = availability;
    }

    // Getters and setters for book details
}

class ShoppingCart {
    private List<Book> books;

    public ShoppingCart() {
        this.books = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void removeBook(Book book) {
        books.remove(book);
    }

    public List<Book> getBooks() {
        return books;
    }
}

class Inventory {
    private Map<Book, Integer> stock;

    public Inventory() {
        this.stock = new HashMap<>();
    }

    public void addBook(Book book, int quantity) {
        int currentQuantity = stock.getOrDefault(book, 0);
        stock.put(book, currentQuantity + quantity);
    }

    public void removeBook(Book book, int quantity) {
        int currentQuantity = stock.getOrDefault(book, 0);
        if (currentQuantity >= quantity) {
            stock.put(book, currentQuantity - quantity);
        } else {
            throw new IllegalArgumentException("Insufficient stock for book: " + book.getTitle());
        }
    }

    public int getAvailability(Book book) {
        return stock.getOrDefault(book, 0);
    }
}

class Order {
    private List<Book> books;
    private double totalAmount;
    // Other order details

    public Order(List<Book> books, double totalAmount) {
        this.books = books;
        this.totalAmount = totalAmount;
    }

    // Getters and setters for order details
}

class Bookstore {
    private List<User> users;
    private List<Book> catalog;
    private Inventory inventory;

    public Bookstore() {
        this.users = new ArrayList<>();
        this.catalog = new ArrayList<>();
        this.inventory = new Inventory();
    }

    public void registerUser(User user) {
        users.add(user);
    }

    public User loginUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public void addBookToCatalog(Book book) {
        catalog.add(book);
        inventory.addBook(book, book.getAvailability());
    }

    public void processOrder(User user, ShoppingCart cart) {
        List<Book> books = cart.getBooks();
        double totalAmount = 0.0;
        for (Book book : books) {
            totalAmount += book.getPrice();
            inventory.removeBook(book, 1);
        }
        Order order = new Order(books, totalAmount);
        // Generate invoice and handle payment transaction
        // Send confirmation email to user, etc.
    }
}

public class OnlineBookstoreDemo {
    public static void main(String[] args) {
        Bookstore bookstore = new Bookstore();

        // Register users
        User user1 = new User("john123", "password");
        User user2 = new User("emma456", "password");
        bookstore.registerUser(user1);
        bookstore.registerUser(user2);

        // Add books to the catalog
        Book book1 = new Book("Book 1", "Author 1", 19.99, 10);
        Book book2 = new Book("Book 2", "Author 2", 14.99, 5);
        bookstore.addBookToCatalog(book1);
        bookstore.addBookToCatalog(book2);

        // Simulate user activities
        User loggedInUser = bookstore.loginUser("john123", "password");
        if (loggedInUser != null) {
            ShoppingCart cart = new ShoppingCart();
            cart.addBook(book1);
            cart.addBook(book2);
            bookstore.processOrder(loggedInUser, cart);
        }
    }
}
