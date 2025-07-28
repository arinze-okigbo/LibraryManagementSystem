import java.io.Serializable;
import java.util.Objects;

public class Book implements Serializable {
    private String title;
    private String author;
    private String genre;
    private String isbn;
    private boolean isAvailable;
    private String borrowedBy;
    private String borrowDate;
    
    public Book(String title, String author, String genre, String isbn) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.isbn = isbn;
        this.isAvailable = true;
        this.borrowedBy = null;
        this.borrowDate = null;
    }
    
    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
    
    public String getBorrowedBy() { return borrowedBy; }
    public void setBorrowedBy(String borrowedBy) { this.borrowedBy = borrowedBy; }
    
    public String getBorrowDate() { return borrowDate; }
    public void setBorrowDate(String borrowDate) { this.borrowDate = borrowDate; }
    
    public boolean borrow(String username) {
        if (isAvailable) {
            isAvailable = false;
            borrowedBy = username;
            borrowDate = java.time.LocalDate.now().toString();
            return true;
        }
        return false;
    }
    
    public boolean returnBook() {
        if (!isAvailable) {
            isAvailable = true;
            borrowedBy = null;
            borrowDate = null;
            return true;
        }
        return false;
    }
    
    @Override
    public String toString() {
        return title + " by " + author + " (" + genre + ")";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Book book = (Book) obj;
        return Objects.equals(isbn, book.isbn);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }
} 