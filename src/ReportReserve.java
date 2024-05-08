import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class ReportReserve extends Report {

    public ReportReserve(Map<Genre, Section> sections) {
        super(sections);
    }

    @Override
    public void generateReport() {
        int reserved = 0;
        int bookNumber = 1;
        System.out.println("Reset the availability for the Overdue Reservations: ");

        // Iterate through each genre
        for (Genre genre : sections.keySet()) {
            Section section = sections.get(genre);

            // Check if the section is empty
            if (!section.getBooks().isEmpty()) {
                for (Book book : section.getBooks()) {
                    // Check if the book is borrowed
                    if (book.getAvailability() == Availability.RESERVED && LocalDate.now().isAfter(book.getReservationDate().plusDays(30))) {
                        reserved = 1;
                        LocalDate reservationDate = book.getReservationDate();
                        long daysDifference = Math.abs(LocalDate.now().until(reservationDate, ChronoUnit.DAYS)) - 30;

                        System.out.println(bookNumber + ". Title: " + book.getTitle());
                        System.out.println("Author: " + book.getAuthor().getName());
                        System.out.println("Reserved by: " + book.getReserver().getName());
                        System.out.println("Reservation Date: " + book.getReservationDate());
                        System.out.println("Days Overdue: " + daysDifference);

                        book.setAvailability(Availability.AVAILABLE);
                        System.out.println("BOOK IS NOW AVAILABLE.");
                        System.out.println('\n');

                        bookNumber++;
                    }
                }
            }
        }
        if(reserved == 0) {
            System.out.println("No expired reservations found.");
        }
    }
}


