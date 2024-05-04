import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

// Main Library class representing the library system
public class Library {
    private Map<Genre, Section> sections; // Map of section name to Section object
    private List<Book> books; // List of all books in the library
    private Map<Book, LocalDate> borrowedBooks;
    private List<Reader> readers;

    public Library() {
        this.sections = new HashMap<>();
        this.books = new ArrayList<>();
        this.readers = new ArrayList<>();
        this.borrowedBooks = new HashMap<>();
    }

    //Add reader to library
    public void addReader(Reader reader) {
        readers.add(reader);
    }

    public void displayAllReaders() {
        System.out.println("All Readers:");
        for (Reader reader : readers) {
            System.out.println(reader.getName());
        }
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

    public void removeBookByTitle(String title) {
        // Iterate over the list of books in the library
        for (Book book : books) {
            // Check if the current book's title matches the specified title
            if (book.getTitle().equals(title)) {
                // Remove the book from the list of books
                books.remove(book);
                // Remove the book from its section (if sections are being used)
                for (Section section : sections.values()) {
                    if (section.getBooks().contains(book)) {
                        section.getBooks().remove(book);
                        break;
                    }
                }

                System.out.println("Book '" + title + "' has been removed from the library.");
                return;
            }
        }

        System.out.println("No book with the title '" + title + "' found in the library.");
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
    public void borrowBook(Book book, Reader borrower, LocalDate borrowDate) {
        if (book.getAvailability() == Availability.AVAILABLE) {
            book.setAvailability(Availability.BORROWED);
            book.setBorrower(borrower);
            book.setBorrowDate(borrowDate);
            borrower.borrowBook(book);
            System.out.println("Book '" + book.getTitle() + "' borrowed by " + borrower.getName() + " on " + borrowDate);
        } else if (book.getAvailability() == Availability.RESERVED) {
            if (book.getReserver() != null && book.getReserver().equals(borrower)) {
                book.setAvailability(Availability.BORROWED);
                book.setBorrower(borrower);
                book.setBorrowDate(borrowDate);
                borrower.borrowBook(book);
                System.out.println("Book '" + book.getTitle() + "' borrowed by " + borrower.getName() + " on " + borrowDate);
            } else {
                LocalDate reservationDate = book.getReservationDate();
                if (ChronoUnit.DAYS.between(reservationDate, LocalDate.now()) > 30) {
                    book.setAvailability(Availability.BORROWED);
                    book.setBorrower(borrower);
                    book.setBorrowDate(borrowDate);
                    borrower.borrowBook(book);
                    System.out.println("Book '" + book.getTitle() + "' borrowed by " + borrower.getName() + " on " + borrowDate);
                } else {
                    System.out.println("Book '" + book.getTitle() + "' is reserved by someone else and cannot be borrowed by " + borrower.getName() + ".");
                }
            }
        } else {
            System.out.println("Book '" + book.getTitle() + "' is not available for borrowing by " + borrower.getName() + ".");
        }
    }



    public void generateOverdueReport() {
        LocalDate currentDate = LocalDate.now();
        System.out.println("Overdue Books Report:");

        for (Section section : sections.values()) {
            for (Book book : section.getBooks()) {
                if (book.getAvailability() == Availability.BORROWED) {
                    Reader borrower = book.getBorrower();
                    LocalDate borrowDate = borrowedBooks.get(book);
                    if (borrowDate != null && borrower != null) {
                        if (currentDate.isAfter(borrowDate.plusDays(30))) {
                            long daysOverdue = java.time.temporal.ChronoUnit.DAYS.between(borrowDate, currentDate);
                            System.out.println("Book: " + book.getTitle() + " (Borrower: " + borrower.getName() + ", Due Date: " + borrowDate.plusDays(30) + ", Days Overdue: " + daysOverdue + ")");
                        } else {
                            System.out.println("Book: " + book.getTitle() + " is not overdue.");
                        }
                    } else {
                        System.out.println("Book: " + book.getTitle() + " is missing borrower or borrow date.");
                    }
                } else {
                    System.out.println("Book: " + book.getTitle() + " is not borrowed.");
                }
            }
        }
    }




    public void checkAvailability(Book book) {
        boolean found = false;
        for (Section section : sections.values()) {
            for (Book b : section.getBooks()) {
                if (b.equals(book)) {
                    found = true;
                    if (b.getAvailability() == Availability.AVAILABLE) {
                        System.out.println("Book '" + book.getTitle() + "' is available.");
                    } else if (b.getAvailability() == Availability.RESERVED) {
                        LocalDate reservationDate = b.getReservationDate();
                        if (ChronoUnit.DAYS.between(reservationDate, LocalDate.now()) > 30) {
                            b.setAvailability(Availability.AVAILABLE);
                            System.out.println("Book '" + book.getTitle() + "' is available now.");
                        } else {
                            System.out.println("Book '" + book.getTitle() + "' is not available. Status: " + b.getAvailability());
                        }
                    } else {
                        System.out.println("Book '" + book.getTitle() + "' is not available. Status: " + b.getAvailability());
                    }
                    break;
                }
            }
            if (found) {
                break;
            }
        }
        if (!found) {
            System.out.println("Book not found in the library.");
        }
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
    public void reserveBook(Book book, Reader reader, LocalDate reserveDate) {
        if (book.getAvailability() == Availability.AVAILABLE) {
            book.setAvailability(Availability.RESERVED);
            book.setReservationDate(reserveDate);
            book.setReserver(reader);
            System.out.println("Book '" + book.getTitle() + "' has been reserved by " + reader.getName() + " on " + reserveDate);
        } else if (book.getAvailability() == Availability.RESERVED) {
            LocalDate reservationDate = book.getReservationDate();
            if (ChronoUnit.DAYS.between(reservationDate, LocalDate.now()) > 30) {
                book.setAvailability(Availability.RESERVED);
                book.setReservationDate(reserveDate);
                book.setReserver(reader);
                System.out.println("Book '" + book.getTitle() + "' has been reserved by " + reader.getName() + " on " + reserveDate);
            } else {
                System.out.println("Sorry, the book '" + book.getTitle() + "' is already reserved by someone else and cannot be reserved by " + reader.getName() + ".");
            }
        } else {
            System.out.println("Sorry, the book '" + book.getTitle() + "' is not available for reservation.");
        }
    }



    public void updateReaderInformation(Reader reader, String newName) {
        // Check if the reader exists in the library's list of readers
        if (readers.contains(reader)) {
            // Update the reader's information
            reader.setName(newName);
            System.out.println("Reader information updated successfully.");
        } else {
            System.out.println("Reader not found in the library.");
        }
    }
}