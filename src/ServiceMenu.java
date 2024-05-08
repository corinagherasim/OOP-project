import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ServiceMenu {

//    Singleton
    private static final ServiceMenu menu = new ServiceMenu();

    private ServiceMenu() {
    }

    public static ServiceMenu getInstance() {
        return menu;
    }

    public void startMenu() {

        Library library = addLocalStorage();
        Scanner scanner = new Scanner(System.in);

//        System.out.println("\n");
//        System.out.println("Please select one of the following commands for more information.");
//        System.out.println("1. Display all books in the library");
//        System.out.println("2. Add books to the library");
//        System.out.println("3. Remove books from the library");
//        System.out.println("4. Search books");
//        System.out.println("5. Add reader to the library");
//        System.out.println("6. Update reader information");
//        System.out.println("7. Reserve book");
//        System.out.println("8. Borrow book");
//        System.out.println("9. Return book");
//        System.out.println("10. Generate overdue report");
//        System.out.println("0.Exit");

        String option;
        do {
            System.out.println("\n");
            System.out.println("Please select one of the following commands for more information.");
            System.out.println("1. Display all books in the library");
            System.out.println("2. Add books to the library");
            System.out.println("3. Remove books from the library");
            System.out.println("4. Search books");
            System.out.println("5. Add reader to the library");
            System.out.println("6. Update reader information");
            System.out.println("7. Reserve book");
            System.out.println("8. Borrow book");
            System.out.println("9. Return book");
            System.out.println("10. Generate overdue report");
            System.out.println("11. Reset availability for overdue reservations");
            System.out.println("0.Exit");

            option = scanner.next();
            scanner.nextLine();
            switch (option) {
                case "0":
                    System.out.println("Exiting menu...");
                    break;
                case "1":
                    library.displayAllBooks();
                    break;
                case "2":
                    String bookTitle;
                    String bookAuthor;
                    Genre bookGenre;

                    System.out.print("Enter book title: ");
                    bookTitle = scanner.nextLine();

                    System.out.print("Enter book author: ");
                    bookAuthor = scanner.nextLine();

                    System.out.println("Select book genre:");
                    System.out.println("1. FICTION");
                    System.out.println("2. NON_FICTION");
                    System.out.println("3. MYSTERY");
                    System.out.println("4. ROMANCE");
                    System.out.println("5. HORROR");
                    System.out.println("6. KIDS");

                    String genreChoice = scanner.next();
                    scanner.nextLine();
                    switch (genreChoice) {
                        case "1":
                            bookGenre = Genre.FICTION;
                            break;
                        case "2":
                            bookGenre = Genre.NON_FICTION;
                            break;
                        case "3":
                            bookGenre = Genre.MYSTERY;
                            break;
                        case "4":
                            bookGenre = Genre.ROMANCE;
                            break;
                        case "5":
                            bookGenre = Genre.HORROR;
                            break;
                        case "6":
                            bookGenre = Genre.KIDS;
                            break;
                        default:
                            System.out.println("Invalid genre choice. Defaulting to FICTION.");
                            bookGenre = Genre.FICTION;
                    }

                    Book book = new Book(bookTitle, new Author(bookAuthor), bookGenre);
                    library.addBook(book);
                    break;
                case "3":
                    try {
                        System.out.println("Enter the title of the book you want to remove:");
                        String removedBookTitle = scanner.nextLine();
                        library.removeBookByTitle(removedBookTitle);
                    } catch (BookNotFoundException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case "4":
                    System.out.println("Search the book by:");
                    System.out.println("1. title");
                    System.out.println("2. author");
                    System.out.println("3. genre");

                    String searchType = scanner.next();
                    scanner.nextLine();
                    switch (searchType) {
                        case "1":
                            System.out.println("Enter the title of the book you want to search:");
                            String searchTitle = scanner.nextLine();
                            try {
                                List<Book> listByTitle = library.searchByTitle(searchTitle);
                                if (!listByTitle.isEmpty()) {
                                    System.out.println("Books found:");
                                    for (Book bookSearchedTitle : listByTitle) {
                                        System.out.println("Title: " + bookSearchedTitle.getTitle() + ", Author: " + bookSearchedTitle.getAuthor().getName() + ", Genre: " + bookSearchedTitle.getGenre() + ", Availability: " + bookSearchedTitle.getAvailability());
                                    }
                                }
                            } catch (BookNotFoundException e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                            break;
                        case "2":
                            System.out.println("Author you want to search:");
                            String searchAuthor = scanner.nextLine();
                            try {
                                List<Book> listByAuthor = library.searchByAuthor(searchAuthor);
                                if (!listByAuthor.isEmpty()) {
                                    System.out.println("Books found:");
                                    for (Book bookSearchedAuthor : listByAuthor) {
                                        System.out.println("Title: " + bookSearchedAuthor.getTitle() + ", Author: " + bookSearchedAuthor.getAuthor().getName() + ", Genre: " + bookSearchedAuthor.getGenre() + ", Availability: " + bookSearchedAuthor.getAvailability());
                                    }
                                }
                            } catch (BookNotFoundException e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                            break;
                        case "3":
                            System.out.println("Select book genre:");
                            System.out.println("1. FICTION");
                            System.out.println("2. NON_FICTION");
                            System.out.println("3. MYSTERY");
                            System.out.println("4. ROMANCE");
                            System.out.println("5. HORROR");
                            System.out.println("6. KIDS");

                            Genre searchGenre;
                            String genreChoiceForSearch = scanner.next();
                            scanner.nextLine();
                            switch (genreChoiceForSearch) {
                                case "1":
                                    searchGenre = Genre.FICTION;
                                    break;
                                case "2":
                                    searchGenre = Genre.NON_FICTION;
                                    break;
                                case "3":
                                    searchGenre = Genre.MYSTERY;
                                    break;
                                case "4":
                                    searchGenre = Genre.ROMANCE;
                                    break;
                                case "5":
                                    searchGenre = Genre.HORROR;
                                    break;
                                case "6":
                                    searchGenre = Genre.KIDS;
                                    break;
                                default:
                                    System.out.println("Invalid genre choice. Defaulting to FICTION.");
                                    searchGenre = Genre.FICTION;
                            }
                            try {
                                List<Book> listByGenre = library.searchByGenre(searchGenre);
                                if (!listByGenre.isEmpty()) {
                                    System.out.println("Books found:");
                                    for (Book bookSearchedGenre : listByGenre) {
                                        System.out.println("Title: " + bookSearchedGenre.getTitle() + ", Author: " + bookSearchedGenre.getAuthor().getName() + ", Genre: " + bookSearchedGenre.getGenre() + ", Availability: " + bookSearchedGenre.getAvailability());
                                    }
                                }
                            } catch (BookNotFoundException e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                            break;
                        default:
                            System.out.println("Invalid option");
                    }
                    break;

                case "5":
                    do {
                        System.out.print("Enter name: ");
                        String name = scanner.nextLine();

                        if (library.isReaderNameTaken(name)) {
                            System.out.println("Name already exists. Please choose another name.");
                        } else {
                            Reader reader = new Reader(name);
                            library.addReader(reader);
                            System.out.println("Reader '" + name + "' added to the library.");
                            break;
                        }
                    } while (true);
                    break;

                case "6":
                    do {
                        System.out.print("Enter name: ");
                        String name = scanner.nextLine();

                        if (!library.isReaderNameTaken(name)) {
                            System.out.println("Name does not exist. Please enter a valid name.");
                        } else {
                            System.out.print("Enter new name: ");
                            String newName = scanner.nextLine();

                            library.updateReaderInformation(library.searchReaderByName(name), newName);
                            System.out.println("Reader information updated successfully.");
                            break;
                        }
                    } while (true);
                    break;

                case "7":
                    String nameReserve;
                    Reader reader;
                    do {
                        System.out.print("Enter name: ");
                        nameReserve = scanner.nextLine();
                        reader = library.searchReaderByName(nameReserve);
                        if (reader == null) {
                            System.out.println("Invalid name. Please enter a valid name.");
                        }
                    } while (reader == null);

                    // Display only available books for reservation
                    library.displayAllBooks();

                    // Get the total number of available books
                    int totalBooks = library.getTotalBooks();

                    // Prompt the user to select a book by its number
                    System.out.print("Enter the number of the book you want to reserve: ");
                    int bookNumber = Integer.parseInt(scanner.nextLine());

                    // Validate the book number
                    if (bookNumber < 1 || bookNumber > totalBooks) {
                        System.out.println("Invalid book number. Please enter a valid number.");
                    } else {
                        // Get the selected book
                        Book selectedBook = library.getBookByNumber(bookNumber);

                        // Prompt the user to enter the reservation date
                        System.out.print("Enter the date of the reservation (YYYY-MM-DD): ");
                        String dateReserve = scanner.nextLine();

                        // Reserve the selected book
                        library.reserveBook(selectedBook, reader, LocalDate.parse(dateReserve));
                    }
                    break;

                case "8":
                    String nameBorrower;
                    Reader borrower;
                    do {
                        System.out.print("Enter name: ");
                        nameBorrower = scanner.nextLine();
                        borrower = library.searchReaderByName(nameBorrower);
                        if (borrower == null) {
                            System.out.println("Invalid name. Please enter a valid name.");
                        }
                    } while (borrower == null);

                    // Display only available books for reservation
                    library.displayAllBooks();

                    // Get the total number of available books
                    int totalB = library.getTotalBooks();

                    // Prompt the user to select a book by its number
                    System.out.print("Enter the number of the book you want to borrow: ");
                    int bookNr = Integer.parseInt(scanner.nextLine());

                    // Validate the book number
                    if (bookNr < 1 || bookNr > totalB) {
                        System.out.println("Invalid book number. Please enter a valid number.");
                    } else {
                        // Get the selected book
                        Book selectedBook = library.getBookByNumber(bookNr);

                        // Prompt the user to enter the reservation date
                        System.out.print("Enter the date of the reservation (YYYY-MM-DD): ");
                        String dateReserve = scanner.nextLine();

                        // Reserve the selected book
                        library.borrowBook(selectedBook, borrower, LocalDate.parse(dateReserve));
                    }
                    break;

                case "9":
                    boolean validName = false;
                    String name;

                    do {
                        System.out.print("Enter name: ");
                        name = scanner.nextLine();
                        borrower = library.searchReaderByName(name);
                        if (borrower != null) {
                            validName = true;
                        } else {
                            System.out.println("Invalid name. Please enter a valid name.");
                        }
                    } while (!validName);

                    int choice;
                    do {
                        System.out.print("1. Search book by title\n2. Search book by author\nChoose an option: ");
                        try {
                            choice = Integer.parseInt(scanner.nextLine());
                            if (choice != 1 && choice != 2) {
                                System.out.println("Invalid choice. Please enter 1 or 2.");
                            } else {
                                break;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid choice. Please enter a number.");
                        }
                    } while (true);

                    if (choice == 1) {
                        System.out.print("Enter the title of the book: ");
                        String title = scanner.nextLine();
                        try {
                            List<Book> matchingBooksTitle = borrower.searchByTitle(title);
                            if (!matchingBooksTitle.isEmpty()) {
                                System.out.println("The reader borrowed these books:");
                                for (Book bk : matchingBooksTitle) {
                                    System.out.println("Title: " + bk.getTitle() + ", Author: " + bk.getAuthor().getName());
                                }
                            } else {
                                System.out.println("No books with the title '" + title + "' found.");
                            }
                        } catch (BookNotFoundException e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    } else {
                        System.out.print("Enter the author of the book: ");
                        String author = scanner.nextLine();
                        try {
                            List<Book> matchingBooksAuthor = borrower.searchByAuthor(author);
                            if (!matchingBooksAuthor.isEmpty()) {
                                System.out.println("The reader borrowed these books:");
                                for (Book bk : matchingBooksAuthor) {
                                    System.out.println("Title: " + bk.getTitle() + ", Author: " + bk.getAuthor().getName());
                                }
                            } else {
                                System.out.println("No books with the author '" + author + "' found.");
                            }
                        } catch (BookNotFoundException e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    }

                    System.out.print("Do you want to return a book? (y/n): ");
                    String response = scanner.nextLine().toLowerCase();

                    while (!response.equals("y") && !response.equals("n")) {
                        System.out.print("Invalid option. Please enter 'y' or 'n': ");
                        response = scanner.nextLine().toLowerCase();
                    }

                    if (response.equals("y")) {
                        System.out.print("Enter the title of the book you want to return: ");
                        String title = scanner.nextLine();

                        Book bookToReturn = library.searchBook(title);

                        if (bookToReturn != null) {
                            library.returnBook(bookToReturn);
                        } else {
                            System.out.println("Book not found.");
                        }
                    } else {
                        System.out.println("No books were returned.");
                    }

                    break;

                case "10":
//                    library.generateOverdueReport();
                    ReportBorrow overdueBorrow = new ReportBorrow(library.getSections());
                    overdueBorrow.generateReport();

                    break;

                case "11":
                    ReportReserve overdueReserve = new ReportReserve(library.getSections());
                    overdueReserve.generateReport();

                    break;

                default:
                    System.out.println("Invalid option");
            }
        } while (!option.equals("0"));
    }

    private Library addLocalStorage() {
        // Create some books
        Book book1 = new Book("Harry Potter and the Philosopher's Stone", new Author("J.K. Rowling"), Genre.FICTION);
        Book book2 = new Book("Pride and Prejudice", new Author("Jane Austen"), Genre.ROMANCE);
        Book book3 = new Book("The Shining", new Author("Stephen King"), Genre.HORROR);
        Book book4 = new Book("The Great Gatsby", new Author("F. Scott Fitzgerald"), Genre.FICTION);
        Book book5 = new Book("Murder on the Orient Express", new Author("Agatha Christie"), Genre.MYSTERY);
        Book book6 = new Book("Steve Jobs", new Author("Walter Isaacson"), Genre.NON_FICTION);
        Book book7 = new Book("The Little Prince", new Author("Antoine de Saint-Exupéry"), Genre.KIDS);
        Book book8 = new Book("Harry Potter and the Chamber of Secrets", new Author("J.K. Rowling"), Genre.FICTION);
        Book book9 = new Book("The fault in our stars", new Author("John Green"), Genre.ROMANCE);

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
        library.addBook(book1);
        library.addBook(book2);
        library.addBook(book3);
        library.addBook(book4);
        library.addBook(book5);
        library.addBook(book6);
        library.addBook(book7);
        library.addBook(book8);
        library.addBook(book9);

        LocalDate borrowDate = LocalDate.parse("2024-01-26");
        library.borrowBook(book4, reader1, borrowDate);

        library.borrowBook(book2, reader2, LocalDate.parse("2024-02-26"));
        library.reserveBook(book3, reader2, LocalDate.parse("2024-03-26"));

        library.checkAvailability(book4);

        library.returnBook(book4);

        library.checkAvailability(book4);

        LocalDate reserveDate = LocalDate.parse("2024-02-26");

        library.reserveBook(book4, reader2, reserveDate);
        library.reserveBook(book4, reader1, LocalDate.parse("2024-05-03"));

        library.checkAvailability(book4);

        library.borrowBook(book4, reader2, LocalDate.parse("2024-05-04"));
        library.borrowBook(book4, reader1, LocalDate.parse("2024-05-04"));

//        library.generateOverdueReport();

        return library;
    }
}
