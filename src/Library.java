import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

// Main Library class representing the library system
public class Library implements Searchable {
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

    public Map<Genre, Section> getSections() {
        return sections;
    }

    //Add reader to library
    public void addReader(Reader reader) {
        readers.add(reader);
    }
    public void addReaderToCSV(Reader reader) {
        readers.add(reader);
        String csvFile = "C:\\Users\\Corina\\IdeaProjects\\OOP-project\\resources\\reader.csv";
        try (PrintWriter writer = new PrintWriter(new FileWriter(csvFile, true))) {
            writer.println(reader.getName());
            System.out.println("Reader added to the CSV file.");
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void displayAllReaders() {
        System.out.println("All Readers:");
        for (Reader reader : readers) {
            System.out.println(reader.getName());
        }
    }

    public Reader searchReaderByName(String name) {
        for (Reader reader : readers) {
            if (reader.getName().equals(name)) {
                return reader;
            }
        }
        return null;
    }

    public boolean isReaderNameTaken(String name) {
        return searchReaderByName(name) != null;
    }

    public Book getBookByNumber(int bookNumber) {
        int count = 0;
        for (Section section : sections.values()) {
            for (Book book : section.getBooks()) {
                count++;
                if (count == bookNumber) {
                    return book;
                }
            }
        }
        return null;
    }

    public int getTotalBooks() {
        int count = 0;
        for (Section section : sections.values()) {
            for (Book book : section.getBooks()) {
                count++;
            }
        }
        return count;
    }

    //Add book to library
    public void addBook(Book book) {
        Genre genre = book.getGenre(); // Get the genre of the book
        // Add the book to the list of all books
        books.add(book);
        // Find or create the appropriate section for the book
        Section section = sections.getOrDefault(genre, new Section());
        section.addBook(book);
        sections.put(genre, section);
    }

    public void addBookMenu(Book book) {
        Genre genre = book.getGenre(); // Get the genre of the book
        // Add the book to the list of all books
        books.add(book);
        // Find or create the appropriate section for the book
        Section section = sections.getOrDefault(genre, new Section());
        section.addBook(book);
        sections.put(genre, section);
        addBookToCSV(book);
        System.out.println("Book '" + book.getTitle() + "' added to the library.");
    }

    private void addBookToCSV(Book book) {
        String csvFile = "C:\\Users\\Corina\\IdeaProjects\\OOP-project\\resources\\book.csv";
        try (PrintWriter writer = new PrintWriter(new FileWriter(csvFile, true))) {
            writer.println(book.getTitle() + "," + book.getAuthor().getName() + "," + book.getGenre());
            System.out.println("Book '" + book.getTitle() + "' added to the CSV file.");
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void removeBookByTitle(String title) throws BookNotFoundException {
        // To later check if the book has been found
        boolean bookRemoved = false;
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
                bookRemoved = true;
                break;
            }
        }

        if (!bookRemoved) {
            throw new BookNotFoundException("Book with title '" + title + "' not found in the library.");
        }

        removeBookFromCSV(title);
    }

    private void removeBookFromCSV(String title) {
        String filePath = "C:\\Users\\Corina\\IdeaProjects\\OOP-project\\resources\\book.csv";
        List<String> updatedLines = new ArrayList<>();

        // Rewrite the CSV file excluding the removed book
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String bookTitle = parts[0].trim();
                if (!bookTitle.equals(title)) {
                    updatedLines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : updatedLines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Displays all books from a library
    public void displayAllBooks() {
        System.out.println("All Books in the Library:");

        int bookNumber = 1;

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
                    System.out.print(bookNumber + ". Title: " + book.getTitle() + ", Author: " + book.getAuthor().getName() + ", Availability: " + book.getAvailability());

                    // Check if the book is borrowed
                    if (book.getAvailability() == Availability.BORROWED) {
                        System.out.println(", Borrowed by: " + book.getBorrower().getName() + ", Borrowed Date: " + book.getBorrowDate());
                    } else if (book.getAvailability() == Availability.RESERVED) {
                        System.out.println(", Reserved by: " + book.getReserver().getName() + ", Reservation Date: " + book.getReservationDate());
                    } else {
                        System.out.println(); // Print a new line if the book is not borrowed or reserved
                    }

                    bookNumber++;
                }
            }
        }
    }

//    public void generateOverdueReport() {
//
//        int borrowed = 0;
//        System.out.println("Overdue Report:");
//
//        // Iterate through each genre
//        for (Genre genre : sections.keySet()) {
//            Section section = sections.get(genre);
//
//            // Check if the section is empty
//            if (!section.getBooks().isEmpty()) {
//                for (Book book : section.getBooks()) {
//                    // Check if the book is borrowed
//                    if (book.getAvailability() == Availability.BORROWED && LocalDate.now().isAfter(book.getBorrowDate().plusDays(30))) {
//                        borrowed = 1;
//                        LocalDate borrowDate = book.getBorrowDate();
//                        long daysDifference = Math.abs(LocalDate.now().until(borrowDate, ChronoUnit.DAYS)) - 30;
//                        System.out.println("Title: " + book.getTitle() + ", Author: " + book.getAuthor().getName() + ", Borrowed by: " + book.getBorrower().getName() + ", Borrowed Date: " + book.getBorrowDate() + ", Days Overdue: " + daysDifference);
//                    }
//                }
//            }
//        }
//        if(borrowed == 0)
//        {
//            System.out.println("No overdue books found.");
//        }
//    }


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

    @Override
    // Search by book genre, title or author
    public List<Book> searchByAuthor(String authorName) throws BookNotFoundException {
        List<Book> matchingBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.getAuthor().getName().equalsIgnoreCase(authorName)) {
                matchingBooks.add(book);
            }
        }
        if (matchingBooks.isEmpty()) {
            throw new BookNotFoundException("No books with the author being " + authorName + " found.");
        }
        return matchingBooks;
    }

    public List<Book> searchByGenre(Genre genre) throws BookNotFoundException {
        List<Book> matchingBooks = new ArrayList<>();
        Section section = sections.get(genre);
        if (section != null) {
            matchingBooks.addAll(section.getBooks());
        }
        if (matchingBooks.isEmpty()) {
            throw new BookNotFoundException("No " + genre + " books found.");
        }
        return matchingBooks;
    }

    @Override
    public List<Book> searchByTitle(String title) throws BookNotFoundException {
        List<Book> matchingBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                matchingBooks.add(book);
            }
        }
        if (matchingBooks.isEmpty()) {
            throw new BookNotFoundException("No books with the title '" + title + "' found.");
        }
        return matchingBooks;
    }

    public Book searchBook(String title) {
        for (Section section : sections.values()) {
            for (Book book : section.getBooks()) {
                if (book.getTitle().equals(title)) {
                    return book;
                }
            }
        }
        return null; // Returnează null dacă cartea nu este găsită
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
            Reader borrower = book.getBorrower();
            if (borrower != null) {
                borrower.returnBook(book);
            } else {
                System.out.println("Error: The book has no associated borrower.");
            }

            // Remove the book from the library's borrowed books map
            borrowedBooks.remove(book);

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
            updateReaderInformationToCSV(reader.getName(),newName);
            reader.setName(newName);
            System.out.println("Reader information updated successfully.");
        } else {
            System.out.println("Reader not found in the library.");
        }
    }

    private void updateReaderInformationToCSV(String oldName, String newName) {
        List<String> lines = new ArrayList<>();
        // Read the existing contents of the CSV file and update the relevant line
        try (BufferedReader fileReader = new BufferedReader(new FileReader("C:\\Users\\Corina\\IdeaProjects\\OOP-project\\resources\\reader.csv"))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                if (line.equals(oldName)) {
                    line = newName;
                    System.out.println("works.");
                }
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Write the updated contents back to the CSV file
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter("C:\\Users\\Corina\\IdeaProjects\\OOP-project\\resources\\reader.csv"))) {
            for (String line : lines) {
                fileWriter.write(line);
                fileWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}