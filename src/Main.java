import java.time.LocalDate;

// Enum representing book availability status
enum Availability {
    AVAILABLE,
    BORROWED,
    RESERVED,
    NOT_FOUND
}
public class Main {
    public static void main(String[] args) {
        // Creăm câteva cărți
        Book book1 = new Book("1984", new Author("George Orwell"));
        Book book2 = new Book("To Kill a Mockingbird", new Author("Harper Lee"));
        Book book3 = new Book("The Great Gatsby", new Author("F. Scott Fitzgerald"));

        // Creăm câțiva cititori
        Reader reader1 = new Reader("Popescu Claudia");
        Reader reader2 = new Reader("Georgescu George");

        // Creăm o instanță de Library
        Library library = new Library();

    }
}










