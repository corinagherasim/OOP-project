import java.time.LocalDate;
import java.util.Map;
import java.time.temporal.ChronoUnit;

public class ReportBorrow extends Report {
    public ReportBorrow(Map<Genre, Section> sections) {
        super(sections);
    }

    @Override
    public void generateReport() {
        int borrowed = 0;
        int bookNumber = 1;
        System.out.println("Overdue Report for borrowed books: ");

        // Iterate through each genre
        for (Genre genre : sections.keySet()) {
            Section section = sections.get(genre);

            // Check if the section is empty
            if (!section.getBooks().isEmpty()) {
                for (Book book : section.getBooks()) {
                    // Check if the book is borrowed
                    if (book.getAvailability() == Availability.BORROWED && LocalDate.now().isAfter(book.getBorrowDate().plusDays(30))) {
                        borrowed = 1;
                        LocalDate borrowDate = book.getBorrowDate();
                        long daysDifference = Math.abs(LocalDate.now().until(borrowDate, ChronoUnit.DAYS)) - 30;
                        System.out.println(bookNumber + ". Title: " + book.getTitle());
                        System.out.println("Author: " + book.getAuthor().getName());
                        System.out.println("Borrowed by: " + book.getBorrower().getName());
                        System.out.println("Borrowed Date: " + book.getBorrowDate());
                        System.out.println("Days Overdue: " + daysDifference);
                        System.out.println('\n');
                        bookNumber++;
                    }
                }
            }
        }
        if(borrowed == 0) {
            System.out.println("No overdue books found.");
        }
    }
}
