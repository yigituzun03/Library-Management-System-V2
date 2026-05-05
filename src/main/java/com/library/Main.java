package com.library;

import com.library.manager.LibraryManager;
import com.library.model.Book;
import com.library.model.Member;
import com.library.strategy.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final LibraryManager manager = LibraryManager.getInstance();

    // =========================================================
    //  DISPLAY HELPERS
    // =========================================================

    private static final String LINE  = "  +----------------------------------------------------------+";
    private static final String THIN  = "  +----------------------------------------------------------+";
    private static final String BLANK = "  |                                                          |";

    private static void header(String title) {
        String padded = centerPad(title, 58);
        System.out.println();
        System.out.println(LINE);
        System.out.println("  |" + padded + "|");
        System.out.println(LINE);
    }

    private static void sectionTitle(String title) {
        System.out.println();
        System.out.println("  >>> " + title);
        System.out.println("  " + repeat("-", 60));
    }

    private static void success(String msg) { System.out.println("  [OK]  " + msg); }
    private static void error(String msg)   { System.out.println("  [ERR] " + msg); }
    private static void info(String msg)    { System.out.println("  [..] " + msg); }
    private static void warn(String msg)    { System.out.println("  [!!] " + msg); }

    private static String repeat(String s, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) sb.append(s);
        return sb.toString();
    }

    private static String centerPad(String text, int width) {
        if (text.length() >= width) return text;
        int total = width - text.length();
        int left  = total / 2;
        int right = total - left;
        return repeat(" ", left) + text + repeat(" ", right);
    }

    // =========================================================
    //  INPUT HELPERS — Basic
    // =========================================================

    /** Reads an integer, re-prompts on invalid input. */
    private static int readInt(String prompt) {
        while (true) {
            System.out.print("  " + prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                warn("Invalid input. Please enter a number.");
            }
        }
    }

    /** Reads a non-empty string, re-prompts if blank. */
    private static String readRequired(String prompt) {
        while (true) {
            System.out.print("  " + prompt);
            String val = scanner.nextLine().trim();
            if (!val.isEmpty()) return val;
            warn("This field cannot be empty.");
        }
    }

    /** Reads an optional string (blank = keep unchanged). */
    private static String readOptional(String prompt) {
        System.out.print("  " + prompt);
        return scanner.nextLine().trim();
    }

    private static boolean confirm(String prompt) {
        while (true) {
            System.out.print("  " + prompt + " [y/n]: ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y")) return true;
            if (input.equals("n")) return false;
            warn("Invalid input. Please enter 'y' or 'n'.");
        }
    }

    // =========================================================
    //  INPUT HELPERS — Validated (used in Create flow)
    // =========================================================

    /**
     * Reads and validates an author name.
     * Allowed: letters, spaces, hyphens, apostrophes, dots. Min 2 chars.
     */
    private static String readAuthor(String prompt) {
        while (true) {
            String val = readRequired(prompt);
            if (isValidAuthor(val)) return val;
            warn("Author name must be at least 2 characters and contain only:");
            info("  letters, spaces, hyphens (-), apostrophes ('), dots (.)");
        }
    }

    private static boolean isValidAuthor(String s) {
        return s.length() >= 2 && s.matches("[a-zA-Z\\s\\-'.,]+");
    }

    /**
     * Reads and validates an ISBN (exactly 13 digits, no hyphens, starts with 978 or 979).
     */
    private static String readIsbn(String prompt) {
        while (true) {
            String val = readRequired(prompt);
            if (isValidIsbn(val)) return val;
            warn("Invalid ISBN. Must be exactly 13 digits with no hyphens (978 or 979 prefix).");
            info("  Example : 9780132350884");
        }
    }

    private static boolean isValidIsbn(String s) {
        if (s.length() != 13) return false;
        for (char c : s.toCharArray()) if (!Character.isDigit(c)) return false;
        return s.startsWith("978") || s.startsWith("979");
    }

    /**
     * Reads and validates a publication year (1000 to current year + 1).
     */
    private static int readYear(String prompt) {
        int maxYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR) + 1;
        while (true) {
            int year = readInt(prompt);
            if (year >= 1000 && year <= maxYear) return year;
            warn("Year must be between 1000 and " + maxYear + ".");
        }
    }

    // =========================================================
    //  MAIN
    // =========================================================
    public static void main(String[] args) {
        // Default members for demonstration
        manager.registerMember(new Member("M001", "Alice"));
        manager.registerMember(new Member("M002", "Bob"));

        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println(LINE);
            System.out.println("  |" + centerPad("LIBRARY MANAGEMENT SYSTEM", 58) + "|");
            System.out.println("  |" + centerPad("Design Patterns Project  |  BIM492", 58) + "|");
            System.out.println(LINE);
            System.out.println("  |  [1] Create Book                                         |");
            System.out.println("  |  [2] Search Book                                         |");
            System.out.println("  |  [3] Borrow Book                                         |");
            System.out.println("  |  [4] Return Book                                         |");
            System.out.println("  |  [5] Modify Book                                         |");
            System.out.println("  |  [6] Undo Last Modification                              |");
            System.out.println("  |  [7] List All Books                                      |");
            System.out.println("  |  [0] Exit                                                |");
            System.out.println(LINE);
            System.out.print("  >> Choice: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": createBookFlow();           break;
                case "2": searchBookFlow();           break;
                case "3": borrowBookFlow();           break;
                case "4": returnBookFlow();           break;
                case "5": modifyBookFlow();           break;
                case "6": manager.undoLastModification(); break;
                case "7": listAllBooks();             break;
                case "0":
                    running = false;
                    System.out.println();
                    System.out.println(LINE);
                    System.out.println("  |" + centerPad("Goodbye! Session closed.", 58) + "|");
                    System.out.println(LINE);
                    break;
                default:
                    warn("Invalid choice. Enter a number between 0 and 7.");
            }
        }
    }

    // =========================================================
    //  [1] CREATE BOOK
    // =========================================================
    private static void createBookFlow() {
        header("CREATE BOOK");

        sectionTitle("Book Details");
        String title     = readRequired("Title       : ");
        String author    = readAuthor  ("Author      : ");
        int    year      = readYear    ("Year        : ");
        String isbn      = readIsbn    ("ISBN        : ");
        String publisher = readRequired("Publisher   : ");

        Book newBook = new Book(title, author, year, isbn, publisher);

        sectionTitle("Categories  (max 3)");
        info("Enter categories one by one. Press Enter to skip a slot.");
        addCategoriesInteractively(newBook);

        sectionTitle("Tags  (max 3)");
        info("Enter tags one by one. Press Enter to skip a slot.");
        addTagsInteractively(newBook);

        sectionTitle("Summary");
        printBookDetail(newBook);

        if (confirm("Save this book?")) {
            manager.addBook(newBook);
        } else {
            warn("Cancelled. Book was not saved.");
        }
    }

    private static void addCategoriesInteractively(Book book) {
        for (int i = 1; i <= 3; i++) {
            System.out.print("  Category " + i + " (or Enter to skip): ");
            String cat = scanner.nextLine().trim();
            if (cat.isEmpty()) continue;
            if (!book.addCategory(cat)) {
                warn("Maximum of 3 categories reached.");
                break;
            }
        }
    }

    private static void addTagsInteractively(Book book) {
        for (int i = 1; i <= 3; i++) {
            System.out.print("  Tag " + i + " (or Enter to skip): ");
            String tag = scanner.nextLine().trim();
            if (tag.isEmpty()) continue;
            if (!book.addTag(tag)) {
                warn("Maximum of 3 tags reached.");
                break;
            }
        }
    }

    /**
     * Per-slot list editor used in Modify Book.
     * - Existing slots: Enter = keep, new value = replace, "-" = remove
     * - Empty slots: Enter = done, new value = add
     */
    private static List<String> editSlots(List<String> current, String label) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            if (i < current.size()) {
                // Existing slot — keep, replace, or remove
                System.out.printf("  %s %d [%s]: ", label, i + 1, current.get(i));
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    result.add(current.get(i));   // keep
                } else if (input.equals("-")) {
                    info(label + " " + (i + 1) + " removed."); // drop it
                } else {
                    result.add(input);             // replace
                }
            } else {
                // Empty slot — add new or stop
                System.out.printf("  %s %d [(empty, Enter to stop)]: ", label, i + 1);
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) break;
                result.add(input);
            }
        }
        return result;
    }

    // =========================================================
    //  [2] SEARCH BOOK  (Strategy Pattern)
    // =========================================================
    private static void searchBookFlow() {
        header("SEARCH BOOK");

        sectionTitle("Search Criteria");
        System.out.println("  [1] Title");
        System.out.println("  [2] Author");
        System.out.println("  [3] ISBN");
        System.out.println("  [4] Category");
        System.out.println("  [5] Tag");
        String searchChoice = readOptional(">> Choice: ");

        ISearchStrategy strategy;
        switch (searchChoice) {
            case "1": strategy = new SearchByTitle();    break;
            case "2": strategy = new SearchByAuthor();   break;
            case "3": strategy = new SearchByIsbn();     break;
            case "4": strategy = new SearchByCategory(); break;
            case "5": strategy = new SearchByTag();      break;
            default:
                error("Invalid choice. Search cancelled.");
                return;
        }
        manager.setSearchStrategy(strategy);

        String keyword = readRequired("Keyword     : ");

        // Sort is only applicable when searching by Title (per project spec)
        if (searchChoice.equals("1")) {
            sectionTitle("Sort Order");
            System.out.println("  [1] Title A -> Z");
            System.out.println("  [2] Title Z -> A");
            System.out.println("  [3] No sorting");
            String sortChoice = readOptional(">> Choice: ");
            switch (sortChoice) {
                case "1": manager.setSortStrategy(new TitleAscendingSortStrategy());  break;
                case "2": manager.setSortStrategy(new TitleDescendingSortStrategy()); break;
                default:  manager.setSortStrategy(null); break;
            }
        } else {
            manager.setSortStrategy(null);
        }

        List<Book> results = manager.searchBooks(keyword);
        sectionTitle("Results  (" + results.size() + " book(s) found)");
        if (results.isEmpty()) {
            info("No books matched your query.");
        } else {
            for (int i = 0; i < results.size(); i++) {
                System.out.println("  " + repeat("-", 60));
                System.out.println("  #" + (i + 1));
                printBookDetail(results.get(i));
            }
        }
    }

    // =========================================================
    //  [5] MODIFY BOOK  (Command Pattern)
    // =========================================================
    private static void modifyBookFlow() {
        header("MODIFY BOOK");

        List<Book> inventory = manager.getInventory();
        if (inventory.isEmpty()) {
            warn("No books in the library to modify.");
            return;
        }

        sectionTitle("Select a Book");
        for (int i = 0; i < inventory.size(); i++) {
            System.out.printf("  [%d] %s  (%s)%n", i + 1,
                inventory.get(i).getTitle(), inventory.get(i).getAuthor());
        }
        int index = readInt(">> Book number: ") - 1;
        if (index < 0 || index >= inventory.size()) {
            error("Invalid number. Operation cancelled.");
            return;
        }

        Book target  = inventory.get(index);
        Book newData = new Book(target); // deep copy

        sectionTitle("Edit Fields  (press Enter to keep current value)");
        String v;

        // --- Title ---
        System.out.printf("  Title       [%s]: ", target.getTitle());
        v = scanner.nextLine().trim();
        if (!v.isEmpty()) {
            if (v.equals(target.getTitle()))    warn("Same as current value. No change made.");
            else                                newData.setTitle(v);
        }

        // --- Author ---
        System.out.printf("  Author      [%s]: ", target.getAuthor());
        v = scanner.nextLine().trim();
        if (!v.isEmpty()) {
            if (v.equals(target.getAuthor()))   warn("Same as current value. No change made.");
            else if (!isValidAuthor(v)) {        warn("Invalid author name — field kept unchanged.");
                                                info("Allowed: letters, spaces, hyphens, apostrophes, dots. Min 2 chars."); }
            else                                newData.setAuthor(v);
        }

        // --- Year ---
        int maxYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR) + 1;
        System.out.printf("  Year        [%d]: ", target.getPublicationYear());
        v = scanner.nextLine().trim();
        if (!v.isEmpty()) {
            try {
                int y = Integer.parseInt(v);
                if (y == target.getPublicationYear())       warn("Same as current value. No change made.");
                else if (y < 1000 || y > maxYear)           warn("Year must be between 1000 and " + maxYear + " — field kept unchanged.");
                else                                        newData.setPublicationYear(y);
            } catch (NumberFormatException e) {             warn("Invalid year — field kept unchanged."); }
        }

        // --- ISBN ---
        System.out.printf("  ISBN        [%s]: ", target.getIsbn());
        v = scanner.nextLine().trim();
        if (!v.isEmpty()) {
            if (v.equals(target.getIsbn()))     warn("Same as current value. No change made.");
            else if (!isValidIsbn(v)) {          warn("Invalid ISBN — field kept unchanged.");
                                                info("Must be exactly 13 digits, no hyphens (e.g. 9780132350884)"); }
            else                                newData.setIsbn(v);
        }

        // --- Publisher ---
        System.out.printf("  Publisher   [%s]: ", target.getPublisher());
        v = scanner.nextLine().trim();
        if (!v.isEmpty()) {
            if (v.equals(target.getPublisher())) warn("Same as current value. No change made.");
            else                                 newData.setPublisher(v);
        }

        sectionTitle("Edit Categories  (max 3)");
        info("Enter to keep current value | type new value to replace | '-' to remove");
        List<String> updatedCats = editSlots(target.getCategories(), "Category");
        newData.setCategories(updatedCats);

        sectionTitle("Edit Tags  (max 3)");
        info("Enter to keep current value | type new value to replace | '-' to remove");
        List<String> updatedTags = editSlots(target.getTags(), "Tag");
        newData.setTags(updatedTags);

        manager.modifyBook(target, newData);
    }

    // =========================================================
    //  [3] BORROW BOOK
    // =========================================================
    private static void borrowBookFlow() {
        header("BORROW BOOK");

        Member member = selectMember();
        if (member == null) return;

        Book book = selectBookFromSearch();
        if (book == null) return;

        if (!book.isAvailable()) {
            error("This book is currently checked out by another member.");
            return;
        }
        manager.borrowBook(book, member);
    }

    // =========================================================
    //  [4] RETURN BOOK
    // =========================================================
    private static void returnBookFlow() {
        header("RETURN BOOK");

        Member member = selectMember();
        if (member == null) return;

        List<Book> borrowed = member.getBorrowedBooks();
        if (borrowed.isEmpty()) {
            info(member.getName() + " has no books currently checked out.");
            return;
        }

        sectionTitle("Checked-Out Books");
        for (int i = 0; i < borrowed.size(); i++) {
            System.out.printf("  [%d] %s%n", i + 1, borrowed.get(i).getTitle());
        }
        int idx = readInt(">> Book number to return: ") - 1;
        if (idx < 0 || idx >= borrowed.size()) {
            error("Invalid number. Operation cancelled.");
            return;
        }
        manager.returnBook(borrowed.get(idx), member);
    }

    // =========================================================
    //  [7] LIST ALL BOOKS
    // =========================================================
    private static void listAllBooks() {
        header("ALL BOOKS");
        List<Book> inventory = manager.getInventory();
        if (inventory.isEmpty()) {
            info("The library has no books yet.");
            return;
        }
        info("Total books in library: " + inventory.size());
        for (int i = 0; i < inventory.size(); i++) {
            System.out.println();
            System.out.println("  " + repeat("-", 60));
            System.out.printf("  #%d%n", i + 1);
            printBookDetail(inventory.get(i));
        }
        System.out.println("  " + repeat("-", 60));
    }

    // =========================================================
    //  HELPER: Member selection from list
    // =========================================================
    private static Member selectMember() {
        List<Member> members = manager.getMembers();
        if (members.isEmpty()) {
            error("No registered members in the system.");
            return null;
        }
        sectionTitle("Select Member");
        for (int i = 0; i < members.size(); i++) {
            System.out.printf("  [%d] %s  (ID: %s)%n", i + 1,
                members.get(i).getName(), members.get(i).getMemberId());
        }
        int idx = readInt(">> Member number: ") - 1;
        if (idx < 0 || idx >= members.size()) {
            error("Invalid number. Operation cancelled.");
            return null;
        }
        return members.get(idx);
    }

    // =========================================================
    //  HELPER: Book selection via title search
    // =========================================================
    private static Book selectBookFromSearch() {
        sectionTitle("Find Book");
        String keyword = readRequired("Search by title: ");

        manager.setSearchStrategy(new SearchByTitle());
        manager.setSortStrategy(null);
        List<Book> results = manager.searchBooks(keyword);

        if (results.isEmpty()) {
            warn("No books found matching: \"" + keyword + "\"");
            return null;
        }

        for (int i = 0; i < results.size(); i++) {
            String status = results.get(i).isAvailable() ? "Available" : "Borrowed";
            System.out.printf("  [%d] %s  -- %s%n", i + 1, results.get(i).getTitle(), status);
        }

        if (results.size() == 1) return results.get(0);

        int idx = readInt(">> Book number: ") - 1;
        if (idx < 0 || idx >= results.size()) {
            error("Invalid number. Operation cancelled.");
            return null;
        }
        return results.get(idx);
    }

    // =========================================================
    //  HELPER: Print full book details
    // =========================================================
    private static void printBookDetail(Book book) {
        String status = book.isAvailable() ? "Available" : "Borrowed";
        String cats   = book.getCategories().isEmpty() ? "(none)" : String.join(", ", book.getCategories());
        String tags   = book.getTags().isEmpty()       ? "(none)" : String.join(", ", book.getTags());

        System.out.println("  Title       : " + book.getTitle());
        System.out.println("  Author      : " + book.getAuthor());
        System.out.println("  Year        : " + book.getPublicationYear());
        System.out.println("  ISBN        : " + book.getIsbn());
        System.out.println("  Publisher   : " + book.getPublisher());
        System.out.println("  Categories  : " + cats);
        System.out.println("  Tags        : " + tags);
        System.out.printf ("  Status      : %-14s | Borrow Count: %d%n", status, book.getBorrowCount());
    }
}