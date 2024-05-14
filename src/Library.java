import java.io.*;
import java.util.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

// Main Library class representing the library system
public class Library implements Searchable {
    private Map<Genre, Section> sections; // Map of section name to Section object
    private List<Book> books; // List of all books in the library
    private Map<Book, LocalDate> borrowedBooks;
    private List<Reader> readers;

    //    Singleton
    private static final Library library = new Library();

    public static Library getInstanceLibrary() {
        return library;
    }

    private Library() {
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
        String csvFile = "resources/reader.csv";
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
        Genre genre = book.getGenre();
        books.add(book);
        Section section = sections.getOrDefault(genre, new Section());
        section.addBook(book);
        sections.put(genre, section);
    }

    public void addBookMenu(Book book) {
        Genre genre = book.getGenre();
        books.add(book);
        Section section = sections.getOrDefault(genre, new Section());
        section.addBook(book);
        sections.put(genre, section);
        addBookToCSV(book);
        System.out.println("Book '" + book.getTitle() + "' added to the library.");
    }

    private void addBookToCSV(Book book) {
        String csvFile = "resources/book.csv";
        try (PrintWriter writer = new PrintWriter(new FileWriter(csvFile, true))) {
            writer.println(book.getTitle() + "," + book.getAuthor().getName() + "," + book.getGenre());
            System.out.println("Book '" + book.getTitle() + "' added to the CSV file.");
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void removeBookByTitle(String title) throws BookNotFoundException {
        boolean bookRemoved = false;
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
        String filePath = "resources/book.csv";
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
                    System.out.print(bookNumber + ". Title: " + book.getTitle() + ", Author: " + book.getAuthor().getName() + ", Availability: " + book.getAvailability());

                    // Check if the book is borrowed
                    if (book.getAvailability() == Availability.BORROWED) {
                        System.out.println(", Borrowed by: " + book.getBorrower().getName() + ", Borrowed Date: " + book.getBorrowDate());
                    } else if (book.getAvailability() == Availability.RESERVED) {
                        System.out.println(", Reserved by: " + book.getReserver().getName() + ", Reservation Date: " + book.getReservationDate());
                    } else {
                        System.out.println();
                    }

                    bookNumber++;
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
    public boolean borrowBook(Book book, Reader borrower, LocalDate borrowDate) {
        boolean borrowedSuccessfully = false;
        if (book.getAvailability() == Availability.AVAILABLE) {
            book.setAvailability(Availability.BORROWED);
            book.setBorrower(borrower);
            book.setBorrowDate(borrowDate);
            borrower.borrowBook(book);
            borrowedSuccessfully = true;
            System.out.println("Book '" + book.getTitle() + "' borrowed by " + borrower.getName() + " on " + borrowDate);
        } else if (book.getAvailability() == Availability.RESERVED) {
            if (book.getReserver() != null && book.getReserver().equals(borrower)) {
                book.setAvailability(Availability.BORROWED);
                book.setBorrower(borrower);
                book.setBorrowDate(borrowDate);
                borrower.borrowBook(book);
                borrowedSuccessfully = true;
                System.out.println("Book '" + book.getTitle() + "' borrowed by " + borrower.getName() + " on " + borrowDate);
            } else {
                LocalDate reservationDate = book.getReservationDate();
                if (ChronoUnit.DAYS.between(reservationDate, LocalDate.now()) > 30) {
                    book.setAvailability(Availability.BORROWED);
                    book.setBorrower(borrower);
                    book.setBorrowDate(borrowDate);
                    borrower.borrowBook(book);
                    borrowedSuccessfully = true;
                    System.out.println("Book '" + book.getTitle() + "' borrowed by " + borrower.getName() + " on " + borrowDate);
                } else {
                    System.out.println("Book '" + book.getTitle() + "' is reserved by someone else and cannot be borrowed by " + borrower.getName() + ".");
                }
            }
        } else {
            System.out.println("Book '" + book.getTitle() + "' is not available for borrowing by " + borrower.getName() + ".");
        }
        return borrowedSuccessfully;
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
    public boolean reserveBook(Book book, Reader reader, LocalDate reserveDate) {
        boolean reservationSuccessfully = false;
        if (book.getAvailability() == Availability.AVAILABLE) {
            book.setAvailability(Availability.RESERVED);
            book.setReservationDate(reserveDate);
            book.setReserver(reader);
            reservationSuccessfully = true;
            System.out.println("Book '" + book.getTitle() + "' has been reserved by " + reader.getName() + " on " + reserveDate);
        } else if (book.getAvailability() == Availability.RESERVED) {
            LocalDate reservationDate = book.getReservationDate();
            if (ChronoUnit.DAYS.between(reservationDate, LocalDate.now()) > 30) {
                book.setAvailability(Availability.RESERVED);
                book.setReservationDate(reserveDate);
                book.setReserver(reader);
                reservationSuccessfully = true;
                System.out.println("Book '" + book.getTitle() + "' has been reserved by " + reader.getName() + " on " + reserveDate);
            } else {
                System.out.println("Sorry, the book '" + book.getTitle() + "' is already reserved by someone else and cannot be reserved by " + reader.getName() + ".");
            }
        } else {
            System.out.println("Sorry, the book '" + book.getTitle() + "' is not available for reservation.");
        }
        return reservationSuccessfully;
    }


    public void updateReaderInformation(Reader reader, String newName) {
        if (readers.contains(reader)) {
            updateReaderInformationToCSV(reader.getName(),newName);
            reader.setName(newName);
            System.out.println("Reader information updated successfully.");
        } else {
            System.out.println("Reader not found in the library.");
        }
    }

    private void updateReaderInformationToCSV(String oldName, String newName) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new FileReader("resources/reader.csv"))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                if (line.equals(oldName)) {
                    line = newName;
                }
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter("resources/reader.csv"))) {
            for (String line : lines) {
                fileWriter.write(line);
                fileWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getAuthorsFromLibrary() {
        List<String> authors = new ArrayList<>();
        for (Book book : books) {
            String authorName = book.getAuthor().getName();
            if (!authors.contains(authorName)) {
                authors.add(authorName);
            }
        }
        return authors;
    }

    public static List<String> readAuthorsFromCSV(String filePath) {
        List<String> authors = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                authors.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return authors;
    }

    public void addAuthorToCSV(String newAuthor) {
        List<String> existingAuthors = readAuthorsFromCSV("resources/book.csv");

        if (!existingAuthors.contains(newAuthor)) {
            existingAuthors.add(newAuthor);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("resources/author.csv", true))) {
                writer.write(newAuthor);
                writer.newLine();
            } catch (IOException e) {
                System.err.println("Error adding author to author.csv: " + e.getMessage());
            }
        } else {
            System.out.println("Author '" + newAuthor + "' already exists in author.csv");
        }
    }

    public void removeAuthorFromCSV(String authorToRemove) {
        List<String> existingAuthors = readAuthorsFromCSV("resources/author.csv");

        if (existingAuthors.contains(authorToRemove)) {
            existingAuthors.remove(authorToRemove);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("resources/author.csv"))) {
                for (String author : existingAuthors) {
                    writer.write(author);
                    writer.newLine();
                }
                System.out.println("Author '" + authorToRemove + "' removed from author.csv");
            } catch (IOException e) {
                System.err.println("Error removing author from author.csv: " + e.getMessage());
            }
        } else {
            System.out.println("Author '" + authorToRemove + "' not found in author.csv");
        }
    }

    public void displayAllAuthors() {
        List<String> authors = readAuthorsFromCSV("resources/author.csv");

        if (authors.isEmpty()) {
            System.out.println("No authors found in our library");
        } else {
            System.out.println("Authors in our library:");
            for (String author : authors) {
                System.out.println(author);
            }
        }
    }

    public void processBooksFromCSV(){
        //    Reading books from csv
        String filePathBook = "resources/book.csv";

        List<Book> books = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(new File(filePathBook));
            scanner.nextLine();

            while (scanner.hasNextLine()) {
                String[] tokens = scanner.nextLine().split(",");

                String title = tokens[0].trim();
                String authorName = tokens[1].trim();
                Genre genre = Genre.valueOf(tokens[2].trim());

                Author author = new Author(authorName);
                Book book = new Book(title, author, genre);

                books.add(book);
            }
            scanner.close();

            for (Book book : books) {
                addBook(book);
            }

            System.out.println("Books added to the library successfully!");
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePathBook);
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("An error occurred while reading the CSV file.");
            e.printStackTrace();
        }
    }

    public void processReadersFromCSV(){
        //        Reading readers from csv
        String filePathReader = "resources/reader.csv";

        List<Reader> readers = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(new File(filePathReader));
            scanner.nextLine();

            while (scanner.hasNextLine()) {
                String name = scanner.nextLine().trim();

                Reader reader = new Reader(name);
                readers.add(reader);
            }
            scanner.close();

            for (Reader reader : readers) {
                addReader(reader);
            }

            System.out.println("Readers added to the library successfully!");
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePathReader);
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("An error occurred while reading the CSV file.");
            e.printStackTrace();
        }
    }

    public void processTransactionsFromCSV() {
        String filePath = "resources/bookStatus.csv";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int status = Integer.parseInt(parts[0].trim());
                String readerName = parts[1].trim();
                String bookTitle = parts[2].trim();
                LocalDate date = LocalDate.parse(parts[3].trim());

                switch (status) {
                    case 0:
                        // Return book
                        Book returnBook = searchBook(bookTitle);
                        if (returnBook != null) {
                            returnBook(returnBook);
                        } else {
                            System.out.println("Book '" + bookTitle + "' not found in the library.");
                        }
                        break;
                    case 1:
                        // Reserve book
                        Book reserveBook = searchBook(bookTitle);
                        Reader reserveReader = searchReaderByName(readerName);
                        if (reserveBook != null && reserveReader != null) {
                            reserveBook(reserveBook, reserveReader, date);
                        } else {
                            System.out.println("Book or reader not found in the library.");
                        }
                        break;
                    case 2:
                        // Borrow book
                        Book borrowBook = searchBook(bookTitle);
                        Reader borrowReader = searchReaderByName(readerName);
                        if (borrowBook != null && borrowReader != null) {
                            borrowBook(borrowBook, borrowReader, date);
                        } else {
                            System.out.println("Book or reader not found in the library.");
                        }
                        break;
                    default:
                        System.out.println("Invalid status in CSV.");
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void addTransactionToCSV(int status, String readerName, String bookTitle, LocalDate date) {
        String csvFile = "resources/bookStatus.csv";
        try (PrintWriter writer = new PrintWriter(new FileWriter(csvFile, true))) {
            writer.println( status + "," + readerName + "," + bookTitle + "," + date);
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateReaderNameInTransactions(String oldName, String newName) {
        String transactionsFilePath = "resources/bookStatus.csv";
        List<String> updatedLines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(transactionsFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[1].trim().equals(oldName)) {
                    parts[1] = newName;
                    line = String.join(",", parts);
                }
                updatedLines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Write the updated contents back to the CSV file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(transactionsFilePath))) {
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}