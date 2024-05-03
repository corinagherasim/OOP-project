import java.util.ArrayList;
import java.util.List;

// Reader class represents the people who read books
class Reader {
    private String name;
    private List<Book> borrowedBooks;

    public Reader(String name) {
        this.name = name;
        this.borrowedBooks = new ArrayList<>();
    }

    // Get the name of the reader
    public String getName() {
        return name;
    }

    public void borrowBook(Book book) {
        borrowedBooks.add(book);
    }

    public void setName(String name) {
        this.name = name;
    }

}