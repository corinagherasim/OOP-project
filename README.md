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
