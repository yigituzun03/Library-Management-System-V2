package com.library.manager;

import com.library.command.CommandHistory;
import com.library.command.ModifyBookCommand;
import com.library.model.Book;
import com.library.model.Member;
import com.library.strategy.ISearchStrategy;
import com.library.strategy.ISortStrategy;

import java.util.ArrayList;
import java.util.List;

public class LibraryManager {

    // Singleton instance
    private static LibraryManager instance;

    // Data stores
    private List<Book>   inventory;
    private List<Member> members;

    // Design pattern components
    private CommandHistory  commandHistory;
    private ISearchStrategy searchStrategy;
    private ISortStrategy   sortStrategy;

    // Private constructor (Singleton)
    private LibraryManager() {
        this.inventory      = new ArrayList<>();
        this.members        = new ArrayList<>();
        this.commandHistory = new CommandHistory();
    }

    // Global access point
    public static LibraryManager getInstance() {
        if (instance == null) {
            instance = new LibraryManager();
        }
        return instance;
    }

    // ----------------------------------------------------------
    //  CORE OPERATIONS
    // ----------------------------------------------------------

    public void addBook(Book book) {
        inventory.add(book);
        System.out.println("  [OK]  Book added: \"" + book.getTitle() + "\"");
    }

    public List<Book> getInventory() {
        return inventory;
    }

    public void registerMember(Member member) {
        members.add(member);
        System.out.println("  [OK]  Member registered: " + member.getName()
            + "  (ID: " + member.getMemberId() + ")");
    }

    public Member getMemberById(String memberId) {
        for (Member m : members) {
            if (m.getMemberId().equals(memberId)) return m;
        }
        return null;
    }

    public List<Member> getMembers() {
        return members;
    }

    // ----------------------------------------------------------
    //  BORROW / RETURN MODULE
    // ----------------------------------------------------------

    public boolean borrowBook(Book book, Member member) {
        if (book.isAvailable()) {
            book.setAvailable(false);
            book.incrementBorrowCount();
            member.borrowBook(book);
            System.out.println("  [OK]  \"" + book.getTitle()
                + "\" checked out to " + member.getName() + ".");
            return true;
        } else {
            System.out.println("  [ERR] This book is already checked out.");
            return false;
        }
    }

    public boolean returnBook(Book book, Member member) {
        if (!book.isAvailable() && member.getBorrowedBooks().contains(book)) {
            book.setAvailable(true);
            member.returnBook(book);
            System.out.println("  [OK]  \"" + book.getTitle()
                + "\" returned by " + member.getName() + ".");
            return true;
        } else {
            System.out.println("  [ERR] Return failed: book not checked out by this member.");
            return false;
        }
    }

    // ----------------------------------------------------------
    //  MODIFICATION MODULE  (Command Pattern)
    // ----------------------------------------------------------

    public void modifyBook(Book targetBook, Book newData) {
        ModifyBookCommand command = new ModifyBookCommand(targetBook, newData);
        commandHistory.push(command);
    }

    public void undoLastModification() {
        commandHistory.undo();
    }

    // ----------------------------------------------------------
    //  SEARCH MODULE  (Strategy Pattern)
    // ----------------------------------------------------------

    public void setSearchStrategy(ISearchStrategy searchStrategy) {
        this.searchStrategy = searchStrategy;
    }

    public void setSortStrategy(ISortStrategy sortStrategy) {
        this.sortStrategy = sortStrategy;
    }

    public List<Book> searchBooks(String keyword) {
        if (searchStrategy == null) {
            System.out.println("  [ERR] No search strategy set.");
            return new ArrayList<>();
        }
        List<Book> results = searchStrategy.search(inventory, keyword);
        if (sortStrategy != null && !results.isEmpty()) {
            sortStrategy.sort(results);
        }
        return results;
    }
}