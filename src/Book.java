// Book class represents individual books
class Book {
    private String title;
    private Author author;
    private Availability availability;
    private Reader borrower;

    public Book(String title, Author author) {
        this.title = title;
        this.author = author;
        this.availability = Availability.AVAILABLE;
    }

    // Get the title of the book
    public String getTitle() {
        return title;
    }

    // Get the author of the book
    public Author getAuthor() {
        return author;
    }

    public void setAvailability(Availability availability) {
        this.availability = availability;
    }

    public Availability getAvailability() {
        return availability;
    }

    public void setBorrower(Reader borrower) {
        this.borrower = borrower;
    }

    public Reader getBorrower() {
        return borrower;
    }

}