import java.util.*;
import java.util.stream.Collectors;

public class RecommendationEngine {
    private Library library;
    
    public RecommendationEngine(Library library) {
        this.library = library;
    }
    
    public List<Book> getRecommendations(User user, int maxRecommendations) {
        List<Book> recommendations = new ArrayList<>();
        
        // Get user's favorite genres and authors
        List<String> favoriteGenres = user.getTopGenres(3);
        List<String> favoriteAuthors = user.getTopAuthors(3);
        
        // Get all available books
        List<Book> availableBooks = library.getAllBooks().stream()
                .filter(Book::isAvailable)
                .collect(Collectors.toList());
        
        // Score books based on user preferences
        Map<Book, Integer> bookScores = new HashMap<>();
        
        for (Book book : availableBooks) {
            int score = 0;
            
            // Score based on genre preference
            if (favoriteGenres.contains(book.getGenre())) {
                score += 10;
            }
            
            // Score based on author preference
            if (favoriteAuthors.contains(book.getAuthor())) {
                score += 15;
            }
            
            // Bonus for books by favorite authors in favorite genres
            if (favoriteGenres.contains(book.getGenre()) && 
                favoriteAuthors.contains(book.getAuthor())) {
                score += 5;
            }
            
            // Avoid recommending books the user has already borrowed
            if (user.hasBorrowedBook(book.getIsbn())) {
                score -= 50;
            }
            
            if (score > 0) {
                bookScores.put(book, score);
            }
        }
        
        // Sort by score and return top recommendations
        return bookScores.entrySet().stream()
                .sorted(Map.Entry.<Book, Integer>comparingByValue().reversed())
                .limit(maxRecommendations)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
    
    public List<Book> getRecommendationsByGenre(String genre, int maxRecommendations) {
        return library.getAllBooks().stream()
                .filter(book -> book.getGenre().equalsIgnoreCase(genre) && book.isAvailable())
                .limit(maxRecommendations)
                .collect(Collectors.toList());
    }
    
    public List<Book> getRecommendationsByAuthor(String author, int maxRecommendations) {
        return library.getAllBooks().stream()
                .filter(book -> book.getAuthor().equalsIgnoreCase(author) && book.isAvailable())
                .limit(maxRecommendations)
                .collect(Collectors.toList());
    }
    
    public List<Book> searchBooks(String query) {
        String lowerQuery = query.toLowerCase();
        return library.getAllBooks().stream()
                .filter(book -> 
                    book.getTitle().toLowerCase().contains(lowerQuery) ||
                    book.getAuthor().toLowerCase().contains(lowerQuery) ||
                    book.getGenre().toLowerCase().contains(lowerQuery) ||
                    book.getIsbn().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }
    
    public List<String> getAllGenres() {
        return library.getAllBooks().stream()
                .map(Book::getGenre)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
    
    public List<String> getAllAuthors() {
        return library.getAllBooks().stream()
                .map(Book::getAuthor)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
} 