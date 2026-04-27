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
    // Singleton Instance
    private static LibraryManager instance;

    // Sistem verileri
    private List<Book> inventory;
    private List<Member> members;

    // Tasarım Deseni Yöneticileri
    private CommandHistory commandHistory;
    private ISearchStrategy searchStrategy;
    private ISortStrategy sortStrategy;

    // Gizli Yapıcı Metot
    private LibraryManager() {
        this.inventory = new ArrayList<>();
        this.members = new ArrayList<>();
        this.commandHistory = new CommandHistory();
    }

    // Global Erişim Noktası
    public static LibraryManager getInstance() {
        if (instance == null) {
            instance = new LibraryManager();
        }
        return instance;
    }

    // --- TEMEL İŞLEMLER ---

    public void addBook(Book book) {
        inventory.add(book);
        System.out.println("Başarılı: '" + book.getTitle() + "' kütüphaneye eklendi.");
    }

    public List<Book> getInventory() {
        return inventory;
    }

    public void registerMember(Member member) {
        members.add(member);
        System.out.println("Başarılı: Yeni üye eklendi (" + member.getName() + ").");
    }

    public Member getMemberById(String memberId) {
        for (Member m : members) {
            if (m.getMemberId().equals(memberId)) {
                return m;
            }
        }
        return null;
    }

    // --- ÖDÜNÇ ALMA MODÜLÜ ---

    public boolean borrowBook(Book book, Member member) {
        if (book.isAvailable()) {
            book.setAvailable(false);
            book.incrementBorrowCount();
            member.borrowBook(book);
            System.out.println("İşlem Başarılı: " + member.getName() + " adlı üye '" + book.getTitle() + "' kitabını ödünç aldı.");
            return true;
        } else {
            System.out.println("Hata: Bu kitap şu an başkası tarafından okunuyor!");
            return false;
        }
    }

    public boolean returnBook(Book book, Member member) {
        if (!book.isAvailable() && member.getBorrowedBooks().contains(book)) {
            book.setAvailable(true);
            member.returnBook(book);
            System.out.println("İşlem Başarılı: " + member.getName() + " adlı üye '" + book.getTitle() + "' kitabını iade etti.");
            return true;
        } else {
            System.out.println("Hata: İade işlemi başarısız. Kitap bu üyede değil veya zaten kütüphanede.");
            return false;
        }
    }

    // --- DÜZENLEME MODÜLÜ (COMMAND PATTERN) ---

    public void modifyBook(Book targetBook, Book newData) {
        // Yeni bir komut oluştur ve geçmişe ekle (Komut kendi kendini çalıştıracak)
        ModifyBookCommand command = new ModifyBookCommand(targetBook, newData);
        commandHistory.push(command);
    }

    public void undoLastModification() {
        commandHistory.undo();
    }

    // --- ARAMA MODÜLÜ (STRATEGY PATTERN) ---

    public void setSearchStrategy(ISearchStrategy searchStrategy) {
        this.searchStrategy = searchStrategy;
    }

    public void setSortStrategy(ISortStrategy sortStrategy) {
        this.sortStrategy = sortStrategy;
    }

    public List<Book> searchBooks(String keyword) {
        if (searchStrategy == null) {
            System.out.println("Hata: Lütfen önce bir arama stratejisi belirleyin.");
            return new ArrayList<>();
        }

        // 1. Belirlenen stratejiye göre filtrele
        List<Book> results = searchStrategy.search(inventory, keyword);

        // 2. Eğer bir sıralama stratejisi belirlenmişse sonuçları sırala
        if (sortStrategy != null && !results.isEmpty()) {
            sortStrategy.sort(results);
        }

        return results;
    }
}