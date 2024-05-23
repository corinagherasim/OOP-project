# Library Management App
This is the project for the Advanced Object Oriented Programming course taken in the 3rd year at the Faculty of Mathematics and Computer Science, University of Bucharest. 

Team members: [Carina](https://github.com/SaicuCarina), [Corina](https://github.com/corinagherasim)

### Description of the application
This is a library manangement app that provides librarians and library staff with tools to efficiently manage the library's resources, including books, readers and book transactions. All details about the books, readers and book actions such as borrowing, reserving or returning a book are stored in CSV files, automatically updating while using the app. There is also an audit system keeping track of every action made.

### Technical requirements:
* Include simple classes with private/protected attributes and methods
* Include abstract classes and interfaces with default behaviour
* Follow OOP principles while defining the classes and their interaction
* Include at least 2 different collection interfaces, each with multiple 
implementations (e.g. at least 2 List implementations, at list 2 Map 
implementations etc.), capable of administering the objects in the application
* Use inheritance and polymorphism for some of the classes used within the 
collections
* At least one service class that exposes the system's operations
* A main class that calls the service's methods
* Define custom Exceptions and use them to make decisions
* Use Enums
* CSV files will be used to store at least 4 types of objects. Each column in 
the file is separated with a comma. Example: name,surname,age or use data 
bases
* Generic singleton services will be created for reading and writing from/to 
files
* At system startup, the data will be automatically loaded from the files.
* An auditing service will need to be created that will log to a CSV file each time an action
defined in the service is performed. Structure of the file: name_of_action,timestamp

### Classes used:
* ServiceMenu - contains an interactive menu 
* Library - manages the collection of books, readers, and transactions
* Book - contains details about the book, including its title, author, genre, availability status, borrower information, and reservation details
* Section - category of a book based on its genre
* Person - base class
* Author - extends the Person class and is used for the books
* Reader - extends the Person class and contains a list of books borrowed by them
* Report - abstract class that has the method for generating reports and is extended by ReportBorrow and ReportReserve
* ReportBorrow - inherits the Report class and is used for generating an overdue borrow report
* ReportReserve - inherits the Report class and is used for generating an overdue reservervation report and resets the availability of the books
* BookNotFound - custom exception for books that are not found

### Interface used:
Searchable - contains methods that are used for searching books by author and by title in a list of books and is implemented in Library and Reader classes

### Design Patterns
We used the Singleton design pattern for 2 classes:
* ServiceMenu: only one instance of the interactive menu can be created and its methods are called in the Main class
* Library: only one instance can be created and used to ensure that the CSV files are updated properly

### Services
The application features 3 services:
* Auditing Service: Logs the actions performed in the application in a csv file (audit.csv)
* Data Service: Retrieves from csv files all the informations about authors (author.csv), books (book.csv), book transactions (bookStatus.csv) and readers (reader.csv)    
* Menu Service: This is the main service that is used to interact with all the objects in the application

### Menu actions:
*   Display all books in the library
*   Add books to the library
*   Remove books from the library
*   Search books by title, author or genre
*   Add reader to the library
*   Update reader information
*   Reserve book for 30 days
*   Borrow book
*   Return book
*   Generate overdue report for borrowed books
*   Reset availability for overdue reservations
