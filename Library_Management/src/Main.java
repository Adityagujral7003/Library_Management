import java.io.*;
import java.util.*;

class Book {
    int id;
    String title;
    String author;
    int quantity;

    public Book(int id, String title, String author, int quantity) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.quantity = quantity;
    }

    public String toString() {
        return "ID: " + id + " | Title: " + title + " | Author: " + author + " | Quantity: " + quantity;
    }

    public String toCSV() {
        return id + "," + title + "," + author + "," + quantity;
    }

    public static Book fromCSV(String line) {
        String[] parts = line.split(",");
        return new Book(
                Integer.parseInt(parts[0]),
                parts[1],
                parts[2],
                Integer.parseInt(parts[3])
        );
    }
}

class Library {
    private final Map<Integer, Book> books = new HashMap<>();
    private final String FILE_NAME = "books.txt";

    public Library() {
        loadBooksFromFile();
    }

    public void addBook(Book book) {
        if (books.containsKey(book.id)) {
            System.out.println("Book with this ID already exists.");
        } else {
            books.put(book.id, book);
            System.out.println("Book added successfully.");
            saveBooksToFile();
        }
    }

    public void viewBooks() {
        if (books.isEmpty()) {
            System.out.println("No books available in the library.");
        } else {
            System.out.println("\n--- Book Inventory ---");
            for (Book book : books.values()) {
                System.out.println(book);
            }
        }
    }

    public void borrowBook(int id) {
        Book book = books.get(id);
        if (book == null) {
            System.out.println("Book not found.");
        } else if (book.quantity == 0) {
            System.out.println("Book is currently not available.");
        } else {
            book.quantity--;
            System.out.println("You have borrowed: " + book.title);
            saveBooksToFile();
        }
    }

    public void returnBook(int id) {
        Book book = books.get(id);
        if (book != null) {
            book.quantity++;
            System.out.println("You have returned: " + book.title);
            saveBooksToFile();
        } else {
            System.out.println("Invalid book ID.");
        }
    }

    public void searchBook(String keyword) {
        boolean found = false;
        keyword = keyword.toLowerCase();
        for (Book book : books.values()) {
            if (book.title.toLowerCase().contains(keyword) ||
                    book.author.toLowerCase().contains(keyword)) {
                System.out.println(book);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No books matched your search.");
        }
    }

    private void saveBooksToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Book book : books.values()) {
                writer.println(book.toCSV());
            }
        } catch (IOException e) {
            System.out.println("Error saving books to file: " + e.getMessage());
        }
    }

    private void loadBooksFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Book book = Book.fromCSV(line);
                books.put(book.id, book);
            }
        } catch (IOException e) {
            System.out.println("Error loading books from file: " + e.getMessage());
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Library library = new Library();
        library.addBook(new Book(201, "The White Tiger", "Aravind Adiga", 5));
        library.addBook(new Book(202, "A Suitable Boy", "Vikram Seth", 4));
        library.addBook(new Book(203, "Midnight’s Children", "Salman Rushdie", 3));
        library.addBook(new Book(204, "Train to Pakistan", "Khushwant Singh", 3));
        library.addBook(new Book(205, "The God of Small Things", "Arundhati Roy", 4));
        library.addBook(new Book(206, "Wings of Fire", "A.P.J. Abdul Kalam", 6));
        library.addBook(new Book(207, "India After Gandhi", "Ramachandra Guha", 2));
        library.addBook(new Book(208, "The Palace of Illusions", "Chitra Banerjee Divakaruni", 5));
        library.addBook(new Book(209, "Chanakya's Chant", "Ashwin Sanghi", 4));
        library.addBook(new Book(210, "Immortals of Meluha", "Amish Tripathi", 5));

        int choice;

        do {
            System.out.println("\n===== Library Menu =====");
            System.out.println("1. Add Book");
            System.out.println("2. View All Books");
            System.out.println("3. Borrow Book");
            System.out.println("4. Return Book");
            System.out.println("5. Search Book");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            while (!scanner.hasNextInt()) {
                System.out.print("Please enter a valid number: ");
                scanner.next();
            }
            choice = scanner.nextInt();
            scanner.nextLine(); // Clear buffer

            switch (choice) {
                case 1:
                    System.out.print("Enter Book ID: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter Book Title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter Book Author: ");
                    String author = scanner.nextLine();
                    System.out.print("Enter Quantity: ");
                    int qty = scanner.nextInt();
                    library.addBook(new Book(id, title, author, qty));
                    break;

                case 2:
                    library.viewBooks();
                    break;

                case 3:
                    System.out.print("Enter Book ID to borrow: ");
                    int borrowId = scanner.nextInt();
                    library.borrowBook(borrowId);
                    break;

                case 4:
                    System.out.print("Enter Book ID to return: ");
                    int returnId = scanner.nextInt();
                    library.returnBook(returnId);
                    break;

                case 5:
                    System.out.print("Enter keyword to search: ");
                    String keyword = scanner.nextLine();
                    library.searchBook(keyword);
                    break;

                case 0:
                    System.out.println("Exiting system. Goodbye!");
                    break;

                default:
                    System.out.println("Invalid choice. Please select again.");
            }
        } while (choice != 0);

        scanner.close();
    }
}
