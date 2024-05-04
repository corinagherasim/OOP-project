import java.time.LocalDate;
import java.util.List;

// Enum representing book availability status
enum Availability {
    AVAILABLE,
    BORROWED,
    RESERVED,
    NOT_FOUND
}

// Enum representing genre of the book
enum Genre {
    FICTION,
    NON_FICTION,
    MYSTERY,
    ROMANCE,
    HORROR,
    KIDS
}
public class Main {
    public static void main(String[] args) {
        // Create some books
        Book book1 = new Book("Harry Potter and the Philosopher's Stone", new Author("J.K. Rowling"), Genre.FICTION);
        Book book2 = new Book("Pride and Prejudice", new Author("Jane Austen"), Genre.ROMANCE);
        Book book3 = new Book("The Shining", new Author("Stephen King"), Genre.HORROR);
        Book book4 = new Book("The Great Gatsby", new Author("F. Scott Fitzgerald"), Genre.FICTION);
        Book book5 = new Book("Murder on the Orient Express", new Author("Agatha Christie"), Genre.MYSTERY);
        Book book6 = new Book("Steve Jobs", new Author("Walter Isaacson"), Genre.NON_FICTION);
        Book book7 = new Book("The Little Prince", new Author("Antoine de Saint-Exupéry"), Genre.KIDS);
        Book book8 = new Book("Harry Potter and the Chamber of Secrets", new Author("J.K. Rowling"), Genre.FICTION);

        // Create some readers
        Reader reader1 = new Reader("Popescu Claudia");
        Reader reader2 = new Reader("Georgescu George");

        // Create library
        Library library = new Library();

        // Add the readers to the library
        library.addReader(reader1);
        library.addReader(reader2);

        library.displayAllReaders();

        // Update the name of the reader
        library.updateReaderInformation(reader1, "Ionescu Claudiu");

        library.displayAllReaders();

        // Add book to library
        library.addBook(book4);
        library.addBook(book2);
        library.addBook(book1);
        library.addBook(book8);

        // Remove a book from library
        library.removeBook(book2);
        library.removeBook(book8);


        //Display all books from library
        library.displayAllBooks();
        LocalDate borrowDate = LocalDate.parse("2024-04-26");
        library.borrowBook(book4, reader1, borrowDate);

        library.checkAvailability(book4);

        library.returnBook(book4);

        library.checkAvailability(book4);

        LocalDate reserveDate = LocalDate.parse("2024-02-26");

        library.reserveBook(book4, reader2, reserveDate);
        library.reserveBook(book4, reader1, LocalDate.parse("2024-05-03"));

        library.checkAvailability(book4);

        library.borrowBook(book4, reader2, LocalDate.parse("2024-05-04"));
        library.borrowBook(book4, reader1, LocalDate.parse("2024-05-04"));


        library.checkAvailability(book4);

        library.generateOverdueReport();

        //Display all the details about a book from the library
        library.viewBookDetails("Harry Potter and the Philosopher's Stone");

        //Search books
        List<Book> fictionBooks = library.searchByGenre(Genre.FICTION);
        if (!fictionBooks.isEmpty()) {
            System.out.println("Books found:");
            for (Book book : fictionBooks) {
                System.out.println("Title: " + book.getTitle() + ", Author: " + book.getAuthor().getName() + ", Genre: " + book.getGenre());
            }
        } else {
            System.out.println("No matching books found.");
        }

        List<Book> booksByTitle = library.searchByTitle("The Great Gatsby");
        if (!booksByTitle.isEmpty()) {
            System.out.println("Books found:");
            for (Book book : booksByTitle) {
                System.out.println("Title: " + book.getTitle() + ", Author: " + book.getAuthor().getName() + ", Genre: " + book.getGenre());
            }
        } else {
            System.out.println("No matching books found.");
        }

        List<Book> booksByAuthor = library.searchByAuthor("J.K. Rowling");
        if (!booksByAuthor.isEmpty()) {
            System.out.println("Books found:");
            for (Book book : booksByAuthor) {
                System.out.println("Title: " + book.getTitle() + ", Author: " + book.getAuthor().getName() + ", Genre: " + book.getGenre());
            }
        } else {
            System.out.println("No matching books found.");
        }
    }
}










