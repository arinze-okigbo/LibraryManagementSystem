import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class LibraryGUI extends JFrame {
    private Library library;
    private JTable bookTable;
    private DefaultTableModel bookTableModel;
    private JTable recommendationTable;
    private DefaultTableModel recommendationTableModel;
    private JTable borrowedTable;
    private DefaultTableModel borrowedTableModel;
    private JTextField searchField;
    private JComboBox<String> filterComboBox;
    private JLabel statusLabel;
    private JLabel userLabel;
    
    public LibraryGUI() {
        this.library = new Library();
        initializeGUI();
        showLoginDialog();
    }
    
    private void initializeGUI() {
        setTitle("Library Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Create top panel for user info and logout
        JPanel topPanel = createTopPanel();
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // Create center panel with tabs
        JTabbedPane tabbedPane = createTabbedPane();
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        // Create bottom panel for status
        JPanel bottomPanel = createBottomPanel();
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(new Color(240, 240, 240));
        
        // User info
        userLabel = new JLabel("Not logged in");
        userLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(userLabel, BorderLayout.WEST);
        
        // Logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());
        panel.add(logoutButton, BorderLayout.EAST);
        
        return panel;
    }
    
    private JTabbedPane createTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Catalog tab
        tabbedPane.addTab("Book Catalog", createCatalogPanel());
        
        // My Books tab
        tabbedPane.addTab("My Borrowed Books", createBorrowedBooksPanel());
        
        // Recommendations tab
        tabbedPane.addTab("Recommendations", createRecommendationsPanel());
        
        return tabbedPane;
    }
    
    private JPanel createCatalogPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Search and filter panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        searchField = new JTextField(20);
        searchField.setToolTipText("Search by title, author, genre, or ISBN");
        
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> performSearch());
        
        filterComboBox = new JComboBox<>(new String[]{"All", "Available", "Borrowed"});
        filterComboBox.addActionListener(e -> refreshBookTable());
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshBookTable());
        
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(new JLabel("Filter:"));
        searchPanel.add(filterComboBox);
        searchPanel.add(refreshButton);
        
        panel.add(searchPanel, BorderLayout.NORTH);
        
        // Book table
        String[] columns = {"Title", "Author", "Genre", "ISBN", "Status", "Borrowed By", "Borrow Date"};
        bookTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        bookTable = new JTable(bookTableModel);
        bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(bookTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        JButton borrowButton = new JButton("Borrow Book");
        borrowButton.addActionListener(e -> borrowSelectedBook());
        
        JButton returnButton = new JButton("Return Book");
        returnButton.addActionListener(e -> returnSelectedBook());
        
        buttonPanel.add(borrowButton);
        buttonPanel.add(returnButton);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        refreshBookTable();
        return panel;
    }
    
    private JPanel createBorrowedBooksPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String[] columns = {"Title", "Author", "Genre", "ISBN", "Borrow Date"};
        borrowedTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        borrowedTable = new JTable(borrowedTableModel);
        borrowedTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(borrowedTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton returnButton = new JButton("Return Selected Book");
        returnButton.addActionListener(e -> returnSelectedBorrowedBook());
        buttonPanel.add(returnButton);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        refreshBorrowedBooksTable();
        return panel;
    }
    
    private JPanel createRecommendationsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String[] columns = {"Title", "Author", "Genre", "ISBN", "Status"};
        recommendationTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        recommendationTable = new JTable(recommendationTableModel);
        recommendationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(recommendationTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton borrowButton = new JButton("Borrow Recommended Book");
        borrowButton.addActionListener(e -> borrowSelectedRecommendedBook());
        JButton refreshButton = new JButton("Refresh Recommendations");
        refreshButton.addActionListener(e -> refreshRecommendationsTable());
        
        buttonPanel.add(borrowButton);
        buttonPanel.add(refreshButton);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        refreshRecommendationsTable();
        return panel;
    }
    
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        panel.setBackground(new Color(240, 240, 240));
        
        statusLabel = new JLabel("Ready");
        panel.add(statusLabel, BorderLayout.WEST);
        
        // Save/Load buttons
        JPanel saveLoadPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save Data");
        JButton loadButton = new JButton("Load Data");
        
        saveButton.addActionListener(e -> saveData());
        loadButton.addActionListener(e -> loadData());
        
        saveLoadPanel.add(saveButton);
        saveLoadPanel.add(loadButton);
        panel.add(saveLoadPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    private void showLoginDialog() {
        JDialog loginDialog = new JDialog(this, "Login", true);
        loginDialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JTextField usernameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        
        gbc.gridx = 0; gbc.gridy = 0;
        loginDialog.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        loginDialog.add(usernameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        loginDialog.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        loginDialog.add(passwordField, gbc);
        
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            
            if (library.login(username, password)) {
                userLabel.setText("Logged in as: " + username);
                statusLabel.setText("Login successful");
                loginDialog.dispose();
                refreshAllTables();
            } else {
                JOptionPane.showMessageDialog(loginDialog, 
                    "Invalid username or password", 
                    "Login Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginDialog.add(loginButton, gbc);
        
        loginDialog.pack();
        loginDialog.setLocationRelativeTo(this);
        loginDialog.setVisible(true);
    }
    
    private void logout() {
        library.logout();
        userLabel.setText("Not logged in");
        statusLabel.setText("Logged out");
        refreshAllTables();
        showLoginDialog();
    }
    
    private void performSearch() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            refreshBookTable();
            return;
        }
        
        List<Book> searchResults = library.getRecommendationEngine().searchBooks(query);
        updateBookTable(searchResults);
        statusLabel.setText("Search completed: " + searchResults.size() + " results");
    }
    
    private void refreshBookTable() {
        List<Book> books = library.getAllBooks();
        String filter = (String) filterComboBox.getSelectedItem();
        
        if ("Available".equals(filter)) {
            books = books.stream().filter(Book::isAvailable).toList();
        } else if ("Borrowed".equals(filter)) {
            books = books.stream().filter(book -> !book.isAvailable()).toList();
        }
        
        updateBookTable(books);
    }
    
    private void updateBookTable(List<Book> books) {
        bookTableModel.setRowCount(0);
        for (Book book : books) {
            bookTableModel.addRow(new Object[]{
                book.getTitle(),
                book.getAuthor(),
                book.getGenre(),
                book.getIsbn(),
                book.isAvailable() ? "Available" : "Borrowed",
                book.getBorrowedBy(),
                book.getBorrowDate()
            });
        }
    }
    
    private void refreshBorrowedBooksTable() {
        List<Book> borrowedBooks = library.getCurrentUserBorrowedBooks();
        borrowedTableModel.setRowCount(0);
        for (Book book : borrowedBooks) {
            borrowedTableModel.addRow(new Object[]{
                book.getTitle(),
                book.getAuthor(),
                book.getGenre(),
                book.getIsbn(),
                book.getBorrowDate()
            });
        }
    }
    
    private void refreshRecommendationsTable() {
        User currentUser = library.getCurrentUser();
        if (currentUser == null) {
            recommendationTableModel.setRowCount(0);
            return;
        }
        
        List<Book> recommendations = library.getRecommendationEngine().getRecommendations(currentUser, 10);
        recommendationTableModel.setRowCount(0);
        for (Book book : recommendations) {
            recommendationTableModel.addRow(new Object[]{
                book.getTitle(),
                book.getAuthor(),
                book.getGenre(),
                book.getIsbn(),
                book.isAvailable() ? "Available" : "Borrowed"
            });
        }
    }
    
    private void refreshAllTables() {
        refreshBookTable();
        refreshBorrowedBooksTable();
        refreshRecommendationsTable();
    }
    
    private void borrowSelectedBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to borrow");
            return;
        }
        
        String isbn = (String) bookTable.getValueAt(selectedRow, 3);
        if (library.borrowBook(isbn)) {
            statusLabel.setText("Book borrowed successfully");
            refreshAllTables();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to borrow book. It may not be available.");
        }
    }
    
    private void returnSelectedBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to return");
            return;
        }
        
        String isbn = (String) bookTable.getValueAt(selectedRow, 3);
        if (library.returnBook(isbn)) {
            statusLabel.setText("Book returned successfully");
            refreshAllTables();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to return book.");
        }
    }
    
    private void returnSelectedBorrowedBook() {
        int selectedRow = borrowedTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to return");
            return;
        }
        
        String isbn = (String) borrowedTable.getValueAt(selectedRow, 3);
        if (library.returnBook(isbn)) {
            statusLabel.setText("Book returned successfully");
            refreshAllTables();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to return book.");
        }
    }
    
    private void borrowSelectedRecommendedBook() {
        int selectedRow = recommendationTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to borrow");
            return;
        }
        
        String isbn = (String) recommendationTable.getValueAt(selectedRow, 3);
        if (library.borrowBook(isbn)) {
            statusLabel.setText("Recommended book borrowed successfully");
            refreshAllTables();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to borrow book. It may not be available.");
        }
    }
    
    private void saveData() {
        library.saveData("library_data.ser");
        statusLabel.setText("Data saved successfully");
    }
    
    private void loadData() {
        Library loadedLibrary = Library.loadData("library_data.ser");
        if (loadedLibrary != null) {
            this.library = loadedLibrary;
            statusLabel.setText("Data loaded successfully");
            refreshAllTables();
        } else {
            statusLabel.setText("Failed to load data");
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new LibraryGUI().setVisible(true);
        });
    }
} 