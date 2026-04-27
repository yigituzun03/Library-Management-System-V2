package com.library;

import com.library.manager.LibraryManager;
import com.library.model.Book;
import com.library.model.Member;
import com.library.strategy.SearchByAuthor;
import com.library.strategy.SearchByTitle;
import com.library.strategy.TitleAscendingSortStrategy;

public class SystemTest {
    public static void main(String[] args) {
        System.out.println("========== SİSTEM OTOMATİK TESTİ BAŞLIYOR ==========\n");

        // 1. Singleton Manager'ı Çağır
        LibraryManager manager = LibraryManager.getInstance();

        // 2. Üye Ekleme
        System.out.println("--- 1. ÜYE VE KİTAP EKLEME TESTİ ---");
        Member member = new Member("M001", "Yiğit");
        manager.registerMember(member);

        // 3. Kitap Ekleme (Kısıtlamaları test edelim)
        Book book1 = new Book("Design Patterns", "Erich Gamma", 1994, "111222", "Addison-Wesley");
        book1.addCategory("Software");
        book1.addTag("Classic");
        manager.addBook(book1);

        Book book2 = new Book("Clean Code", "Robert C. Martin", 2008, "999888", "Prentice Hall");
        manager.addBook(book2);

        // 4. Arama ve Sıralama (Strategy Pattern)
        System.out.println("\n--- 2. ARAMA VE SIRALAMA TESTİ (STRATEGY PATTERN) ---");
        manager.setSearchStrategy(new SearchByTitle());
        manager.setSortStrategy(new TitleAscendingSortStrategy());
        System.out.println("'Code' kelimesi başlıkta aranıyor: " + manager.searchBooks("Code"));

        manager.setSearchStrategy(new SearchByAuthor());
        System.out.println("'Erich' kelimesi yazarda aranıyor: " + manager.searchBooks("Erich"));

        // 5. Düzenleme ve Geri Alma (Command Pattern)
        System.out.println("\n--- 3. DÜZENLEME VE GERİ ALMA TESTİ (COMMAND PATTERN) ---");
        System.out.println("Eski Başlık: " + book1.getTitle());

        // Yeni verileri tutacak geçici bir kopya oluşturuyoruz
        Book newData = new Book(book1);
        newData.setTitle("Design Patterns (GÜNCELLENDİ)");
        manager.modifyBook(book1, newData); // Değişikliği uygula

        System.out.println("Yeni Başlık: " + book1.getTitle());

        System.out.println("\nSon işlem geri alınıyor (Undo)...");
        manager.undoLastModification();
        System.out.println("Undo Sonrası Başlık: " + book1.getTitle());

        // 6. Ödünç Alma ve İade
        System.out.println("\n--- 4. ÖDÜNÇ ALMA VE İADE TESTİ ---");
        manager.borrowBook(book1, member);
        System.out.println("Kitap müsait mi? (False olmalı): " + book1.isAvailable());
        System.out.println("Kitabın ödünç alınma sayısı (1 olmalı): " + book1.getBorrowCount());

        // Aynı kitabı tekrar ödünç almayı deneme (Hata vermeli)
        System.out.println("\nAynı kitabı başkası almaya çalışırsa:");
        manager.borrowBook(book1, new Member("M002", "Ahmet"));

        System.out.println("\nKitap iade ediliyor...");
        manager.returnBook(book1, member);
        System.out.println("İade sonrası kitap müsait mi? (True olmalı): " + book1.isAvailable());

        System.out.println("\n========== TÜM TESTLER BAŞARIYLA TAMAMLANDI ==========");
    }
}