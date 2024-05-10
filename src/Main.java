import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

// Enum representing book availability status
enum Availability {
    AVAILABLE,
    BORROWED,
    RESERVED,
    NOT_FOUND
}

// Enum representing genre of the book
enum Genre {
    FICTION,
    NON_FICTION,
    MYSTERY,
    ROMANCE,
    HORROR,
    KIDS
}

public class Main {
    public static void main(String[] args) {
        ServiceMenu menu = ServiceMenu.getInstance();
        menu.startMenu();
    }
}
