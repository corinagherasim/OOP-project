// Book class represents individual books
class Book {
    private String title;
    private Author author;

    public Book(String title, Author author) {
        this.title = title;
        this.author = author;
    }

    // Get the title of the book
    public String getTitle() {
        return title;
    }

    // Get the author of the book
    public Author getAuthor() {
        return author;
    }

}