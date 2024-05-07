import java.util.ArrayList;
import java.util.List;

// Reader class represents the people who read books and extends Person
class Reader extends Person implements Searchable {
    private List<Book> borrowedBooks;

    public Reader(String name) {
        super(name);
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

    @Override
    public List<Book> searchByTitle(String title) throws BookNotFoundException{
        List<Book> matchingBooks = new ArrayList<>();
        for (Book book : borrowedBooks) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                matchingBooks.add(book);
            }
        }
        if (matchingBooks.isEmpty()) {
            throw new BookNotFoundException("No books with the title '" + title + "' found.");
        }
        return matchingBooks;
    }

    // Search for books by author in the list of borrowed books
    @Override
    public List<Book> searchByAuthor(String authorName) throws BookNotFoundException{
        List<Book> matchingBooks = new ArrayList<>();
        for (Book book : borrowedBooks) {
            if (book.getAuthor().getName().equalsIgnoreCase(authorName)) {
                matchingBooks.add(book);
            }
        }
        if (matchingBooks.isEmpty()) {
            throw new BookNotFoundException("No books with the author being " + authorName + " found.");
        }
        return matchingBooks;
    }
}