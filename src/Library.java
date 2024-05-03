import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;

// Main Library class representing the library system
public class Library {
    private Map<String, Section> sections; // Map of section name to Section object
    private List<Book> books; // List of all books in the library
    private Map<Book, LocalDate> borrowedBooks;

    public Library() {
        this.sections = new HashMap<>();
        this.books = new ArrayList<>();
    }

    // Remove a book from the library
    public void removeBook(Book book) {
        if (books.contains(book)) {
            books.remove(book);
            for (Section section : sections.values()) {
                if (section.getBooks().contains(book)) {
                    section.getBooks().remove(book);
                    break;
                }
            }
        } else {
            System.out.println("Book not found in the library.");
        }
    }

    // Check the availability of a book
    public void borrowBook(Book book, Reader reader, LocalDate borrowDate) {
        // Setăm data scadentă la 30 de zile de la data de împrumut
        LocalDate dueDate = borrowDate.plusDays(30);
        borrowedBooks.put(book, dueDate);
        System.out.println("Book '" + book.getTitle() + "' borrowed by " + reader.getName() + " until " + dueDate);
    }

    public void generateOverdueReport() {
        LocalDate currentDate = LocalDate.now();
        System.out.println("Overdue Books Report:");

        for (Map.Entry<Book, LocalDate> entry : borrowedBooks.entrySet()) {
            Book book = entry.getKey();
            LocalDate dueDate = entry.getValue();

            // Verificăm dacă data curentă este mai mare decât data scadentă cu cel puțin 30 de zile
            if (currentDate.isAfter(dueDate.plusDays(30))) {
                long daysOverdue = java.time.temporal.ChronoUnit.DAYS.between(dueDate, currentDate);
                System.out.println("Book: " + book.getTitle() + " (Due Date: " + dueDate + ", Days Overdue: " + daysOverdue + ")");
            }
        }
    }

    public Availability checkAvailability(Book book) {
        for (Section section : sections.values()) {
            for (Book b : section.getBooks()) {
                if (b.equals(book)) {
                    return b.getAvailability();
                }
            }
        }
        return Availability.NOT_FOUND;
    }

    // Return a book
    public void returnBook(Book book) {
        // Check if the book is borrowed
        if (book.getAvailability() == Availability.BORROWED) {
            // Update the book's availability status to AVAILABLE
            book.setAvailability(Availability.AVAILABLE);

            // Remove the association with the borrower
            book.setBorrower(null);

            System.out.println("Book '" + book.getTitle() + "' has been returned.");
        } else {
            System.out.println("This book is not borrowed and cannot be returned.");
        }
    }

    // Reserve a book
    public void reserveBook(Book book, Reader reader) {
        // Check if the book is available for reservation
        if (book.getAvailability() == Availability.AVAILABLE) {
            // Update the book's availability status to RESERVED
            book.setAvailability(Availability.RESERVED);

            // Associate the reader with the reserved book
            book.setBorrower(reader);

            System.out.println("Book '" + book.getTitle() + "' has been reserved by " + reader.getName());
        } else {
            System.out.println("Sorry, the book '" + book.getTitle() + "' is not available for reservation.");
        }
    }

    public void updateReaderInformation(Reader reader, String newName) {
        // Get the list of all readers
        List<Reader> allReaders = new ArrayList<>();

        for (Section section : sections.values()) {
            for (Book book : section.getBooks()) {
                Reader borrower = book.getBorrower();
                if (borrower != null) {
                    allReaders.add(borrower);
                }
            }
        }

        // Check if the reader exists in the library
        if (allReaders.contains(reader)) {
            // Update the reader's information
            reader.setName(newName);

            System.out.println("Reader information updated successfully.");
        } else {
            System.out.println("Reader not found in the library.");
        }
    }

}