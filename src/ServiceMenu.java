import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.Instant;
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
                    addActionToCSV("Display all books");
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
                    library.addBookMenu(book);
                    List<String> authorsFromFile = library.readAuthorsFromCSV("resources/author.csv");
                    List<String> authorsInLibrary = library.getAuthorsFromLibrary();
                    for (String author : authorsInLibrary) {
                        if (!authorsFromFile.contains(author)) {
                            library.addAuthorToCSV(author);
                        }
                    }
                    addActionToCSV("Add book");
                    break;
                case "3":
                    try {
                        System.out.println("Enter the title of the book you want to remove:");
                        String removedBookTitle = scanner.nextLine();
                        library.removeBookByTitle(removedBookTitle);
                        addActionToCSV("Remove book");
                    } catch (BookNotFoundException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    authorsFromFile = library.readAuthorsFromCSV("resources/author.csv");
                    authorsInLibrary = library.getAuthorsFromLibrary();
                    for (String author : authorsFromFile) {
                        if (!authorsInLibrary.contains(author)) {
                            library.removeAuthorFromCSV(author);
                        }
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
                            addActionToCSV("Search book by title");
                            break;
                        case "2":
                            library.displayAllAuthors();
                            System.out.println("Enter the author you want to search:");
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
                            addActionToCSV("Search book by author");
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
                            addActionToCSV("Search book by genre");
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
                            library.addReaderToCSV(reader);
                            System.out.println("Reader '" + name + "' added to the library.");
                            addActionToCSV("Add reader");
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
                            library.updateReaderNameInTransactions(name,newName);
                            addActionToCSV("Update reader information");
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
                        library.addTransactionToCSV(1,reader.getName(),selectedBook.getTitle(),LocalDate.parse(dateReserve));
                        addActionToCSV("Reserve book");
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
                        String dateBorrow = scanner.nextLine();

                        // Reserve the selected book
                        library.borrowBook(selectedBook, borrower, LocalDate.parse(dateBorrow));
                        library.addTransactionToCSV(2,borrower.getName(),selectedBook.getTitle(),LocalDate.parse(dateBorrow));
                        addActionToCSV("Borrow book");
                    }

                    break;

                case "9":
                    boolean validName = false;
                    String name;
                    int var = 1;

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
                            }
                        } catch (BookNotFoundException e) {
                            var = 0;
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
                            }
                        } catch (BookNotFoundException e) {
                            var = 0;
                            System.out.println("Error: " + e.getMessage());
                        }
                    }

                    if(var != 0){
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
                                library.addTransactionToCSV(0,borrower.getName(),bookToReturn.getTitle(),LocalDate.now());
                                addActionToCSV("Return book");
                            } else {
                                System.out.println("Book not found.");
                            }
                        } else {
                            System.out.println("No books were returned.");
                        }
                    }
                    break;

                case "10":
//                    library.generateOverdueReport();
                    ReportBorrow overdueBorrow = new ReportBorrow(library.getSections());
                    overdueBorrow.generateReport();

                    addActionToCSV("Generate overdue report");
                    break;

                case "11":
                    ReportReserve overdueReserve = new ReportReserve(library.getSections());
                    overdueReserve.generateReport();

                    addActionToCSV("Reset availability for overdue reservations");
                    break;

                default:
                    System.out.println("Invalid option");
            }
        } while (!option.equals("0"));
    }

    private Library addLocalStorage() {
        // Create library
//        Library library = Library.getInstanceLibrary();
        Library library = new Library();

        library.processBooksFromCSV();

        library.processReadersFromCSV();

        List<String> authorsFromFile = library.readAuthorsFromCSV("resources/author.csv");

// Get authors from the library
        List<String> authorsInLibrary = library.getAuthorsFromLibrary();

// Check for extra authors in author.csv
        for (String author : authorsFromFile) {
            if (!authorsInLibrary.contains(author)) {
                library.removeAuthorFromCSV(author);
            }
        }

// Check for new authors in the library
        for (String author : authorsInLibrary) {
            if (!authorsFromFile.contains(author)) {
                library.addAuthorToCSV(author);
            }
        }

        library.processTransactionsFromCSV();

        return library;
    }

    private void addActionToCSV(String action) {
        String csvFile = "resources/audit.csv";
        try (PrintWriter writer = new PrintWriter(new FileWriter(csvFile, true))) {
            writer.println(action + "," + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
