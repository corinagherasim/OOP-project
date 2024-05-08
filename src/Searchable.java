import java.util.List;

public interface Searchable {
    List<Book> searchByTitle(String title) throws BookNotFoundException;

    List<Book> searchByAuthor(String author) throws BookNotFoundException;
}
