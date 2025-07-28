import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User implements Serializable {
    private String username;
    private String password;
    private List<String> borrowedBooks; // List of ISBNs
    private Map<String, Integer> genrePreferences; // Genre -> count
    private Map<String, Integer> authorPreferences; // Author -> count
    
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.borrowedBooks = new ArrayList<>();
        this.genrePreferences = new HashMap<>();
        this.authorPreferences = new HashMap<>();
    }
    
    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public List<String> getBorrowedBooks() { return borrowedBooks; }
    
    public Map<String, Integer> getGenrePreferences() { return genrePreferences; }
    
    public Map<String, Integer> getAuthorPreferences() { return authorPreferences; }
    
    public void borrowBook(String isbn, String genre, String author) {
        borrowedBooks.add(isbn);
        
        // Update genre preferences
        genrePreferences.put(genre, genrePreferences.getOrDefault(genre, 0) + 1);
        
        // Update author preferences
        authorPreferences.put(author, authorPreferences.getOrDefault(author, 0) + 1);
    }
    
    public void returnBook(String isbn) {
        borrowedBooks.remove(isbn);
    }
    
    public boolean hasBorrowedBook(String isbn) {
        return borrowedBooks.contains(isbn);
    }
    
    public String getFavoriteGenre() {
        return genrePreferences.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Fiction");
    }
    
    public String getFavoriteAuthor() {
        return authorPreferences.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Unknown");
    }
    
    public List<String> getTopGenres(int count) {
        return genrePreferences.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(count)
                .map(Map.Entry::getKey)
                .toList();
    }
    
    public List<String> getTopAuthors(int count) {
        return authorPreferences.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(count)
                .map(Map.Entry::getKey)
                .toList();
    }
    
    @Override
    public String toString() {
        return username;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return username.equals(user.username);
    }
    
    @Override
    public int hashCode() {
        return username.hashCode();
    }
} 