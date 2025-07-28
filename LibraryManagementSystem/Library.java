import java.io.*;
import java.util.*;

public class Library implements Serializable {
    private List<Book> books;
    private Map<String, User> users;
    private User currentUser;
    private RecommendationEngine recommendationEngine;
    
    public Library() {
        this.books = new ArrayList<>();
        this.users = new HashMap<>();
        this.currentUser = null;
        initializeSampleData();
        this.recommendationEngine = new RecommendationEngine(this);
    }
    
    private void initializeSampleData() {
        // Add sample books
        addBook(new Book("The Great Gatsby", "F. Scott Fitzgerald", "Fiction", "978-0743273565"));
        addBook(new Book("To Kill a Mockingbird", "Harper Lee", "Fiction", "978-0446310789"));
        addBook(new Book("1984", "George Orwell", "Science Fiction", "978-0451524935"));
        addBook(new Book("Pride and Prejudice", "Jane Austen", "Romance", "978-0141439518"));
        addBook(new Book("The Hobbit", "J.R.R. Tolkien", "Fantasy", "978-0547928244"));
        addBook(new Book("The Catcher in the Rye", "J.D. Salinger", "Fiction", "978-0316769488"));
        addBook(new Book("Lord of the Flies", "William Golding", "Fiction", "978-0399501487"));
        addBook(new Book("Animal Farm", "George Orwell", "Political Fiction", "978-0451526342"));
        addBook(new Book("Brave New World", "Aldous Huxley", "Science Fiction", "978-0060850524"));
        addBook(new Book("The Alchemist", "Paulo Coelho", "Fiction", "978-0062315007"));
        addBook(new Book("The Little Prince", "Antoine de Saint-Exupéry", "Fiction", "978-0156013987"));
        addBook(new Book("The Da Vinci Code", "Dan Brown", "Thriller", "978-0307474278"));
        addBook(new Book("Gone Girl", "Gillian Flynn", "Thriller", "978-0307588364"));
        addBook(new Book("The Hunger Games", "Suzanne Collins", "Young Adult", "978-0439023481"));
        addBook(new Book("Harry Potter and the Sorcerer's Stone", "J.K. Rowling", "Fantasy", "978-0590353427"));
        
        // Add sample users
        addUser(new User("admin", "admin123"));
        addUser(new User("john", "password123"));
        addUser(new User("sarah", "password456"));
    }
    
    public void addBook(Book book) {
        books.add(book);
    }
    
    public void addUser(User user) {
        users.put(user.getUsername(), user);
    }
    
    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }
    
    public Book findBookByIsbn(String isbn) {
        return books.stream()
                .filter(book -> book.getIsbn().equals(isbn))
                .findFirst()
                .orElse(null);
    }
    
    public User findUserByUsername(String username) {
        return users.get(username);
    }
    
    public boolean login(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            return true;
        }
        return false;
    }
    
    public void logout() {
        currentUser = null;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public boolean borrowBook(String isbn) {
        if (currentUser == null) return false;
        
        Book book = findBookByIsbn(isbn);
        if (book != null && book.borrow(currentUser.getUsername())) {
            currentUser.borrowBook(isbn, book.getGenre(), book.getAuthor());
            return true;
        }
        return false;
    }
    
    public boolean returnBook(String isbn) {
        if (currentUser == null) return false;
        
        Book book = findBookByIsbn(isbn);
        if (book != null && book.returnBook()) {
            currentUser.returnBook(isbn);
            return true;
        }
        return false;
    }
    
    public List<Book> getCurrentUserBorrowedBooks() {
        if (currentUser == null) return new ArrayList<>();
        
        return books.stream()
                .filter(book -> currentUser.hasBorrowedBook(book.getIsbn()))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    public RecommendationEngine getRecommendationEngine() {
        return recommendationEngine;
    }
    
    public void saveData(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this);
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    public static Library loadData(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            Library library = (Library) ois.readObject();
            library.recommendationEngine = new RecommendationEngine(library);
            return library;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading data: " + e.getMessage());
            return new Library();
        }
    }
} 