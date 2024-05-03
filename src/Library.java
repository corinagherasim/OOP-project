import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;

// Main Library class representing the library system
public class Library {
    private Map<Genre, Section> sections; // Map of section name to Section object
    private List<Book> books; // List of all books in the library
    private Map<Book, LocalDate> borrowedBooks;

    public Library() {
        this.sections = new HashMap<>();
        this.books = new ArrayList<>();
    }

    //Add book to library
    public void addBook(Book book) {
        Genre genre = book.getGenre(); // Get the genre of the book
        // Check if the book already exists in the library
        if (!books.contains(book)) {
            // Add the book to the list of all books
            books.add(book);

            // Find or create the appropriate section for the book
            Section section = sections.getOrDefault(genre, new Section());
            section.addBook(book);
            sections.put(genre, section);

            System.out.println("Book '" + book.getTitle() + "' added to the library.");
        } else {
            System.out.println("Book '" + book.getTitle() + "' already exists in the library.");
        }
    }

    // Remove a book from the library (also the author if he doesn't have any other books in the library)
    public void removeBook(Book book) {
        if (books.contains(book)) {
            // Remove the book from the list of all books
            books.remove(book);
            // Remove the book from its section
            Genre bookGenre = book.getGenre();
            Section section = sections.get(bookGenre);
            if (section != null && section.getBooks().contains(book)) {
                section.getBooks().remove(book);
                System.out.println("Book '" + book.getTitle() + "' removed from section '" + bookGenre + "'.");
            } else {
                System.out.println("Book '" + book.getTitle() + "' not found in its section.");
            }

            System.out.println("Book '" + book.getTitle() + "' removed from the library.");
        } else {
            System.out.println("Book not found in the library.");
        }
    }


    //Displays all books from a library
    public void displayAllBooks() {
        System.out.println("All Books in the Library:");

        // Iterate through each genre
        for (Genre genre : sections.keySet()) {
            Section section = sections.get(genre);

            // Check if the section is empty
            if (!section.getBooks().isEmpty()) {
                // Print section name (genre)
                System.out.println("Genre: " + genre);

                // Iterate through each book in the section
                for (Book book : section.getBooks()) {
                    // Print book details
                    System.out.println("Title: " + book.getTitle() + ", Author: " + book.getAuthor().getName() + ", Availability: " + book.getAvailability());
                }
            }
        }
    }



    //Displays all the details about a book
    public void viewBookDetails(String title) {
        boolean found = false;

        // Iterate through each section
        for (Section section : sections.values()) {
            // Iterate through each book in the section
            for (Book book : section.getBooks()) {
                // Check if the book title matches the input title
                if (book.getTitle().equals(title)) {
                    found = true;
                    // Print book details
                    System.out.println("Book Title: " + book.getTitle());
                    System.out.println("Author: " + book.getAuthor().getName());
                    System.out.println("Availability: " + book.getAvailability());
                    if (book.getAvailability() == Availability.BORROWED) {
                        // If the book is borrowed, print borrower details
                        System.out.println("Borrower: " + book.getBorrower().getName());
                        System.out.println("Due Date: " + borrowedBooks.get(book));
                    }
                    break; // Exit loop once the book is found
                }
            }
            if (found) {
                break; // Exit loop once the book is found
            }
        }

        if (!found) {
            System.out.println("Book not found in the library.");
        }
    }

    // Search by book genre, title or author
    public List<Book> searchByAuthor(String authorName) {
        List<Book> matchingBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.getAuthor().getName().equalsIgnoreCase(authorName)) {
                matchingBooks.add(book);
            }
        }
        return matchingBooks;
    }

    public List<Book> searchByGenre(Genre genre) {
        List<Book> matchingBooks = new ArrayList<>();
        Section section = sections.get(genre);
        if (section != null) {
            matchingBooks.addAll(section.getBooks());
        }
        return matchingBooks;
    }

    public List<Book> searchByTitle(String title) {
        List<Book> matchingBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                matchingBooks.add(book);
            }
        }
        return matchingBooks;
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