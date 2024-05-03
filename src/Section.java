import java.util.ArrayList;
import java.util.List;

class Section {
    private List<Book> books;

    public Section() {
        this.books = new ArrayList<>();
    }

    // Add a book to this section
    public void addBook(Book book) {
        books.add(book);
    }

    // Get all books in this section
    public List<Book> getBooks() {
        return books;
    }
}
