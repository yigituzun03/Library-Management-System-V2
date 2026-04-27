package com.library;

import com.library.manager.LibraryManager;
import com.library.model.Book;
import com.library.model.Member;
import com.library.strategy.*;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final LibraryManager manager = LibraryManager.getInstance();

    public static void main(String[] args) {
        // Ödünç alma işlemlerini test edebilmen için sisteme varsayılan bir üye ekliyoruz
        manager.registerMember(new Member("M001", "Test Kullanicisi"));

        boolean running = true;
        while (running) {
            System.out.println("\n===== KÜTÜPHANE YÖNETİM SİSTEMİ =====");
            System.out.println("1. Create Book (Kitap Oluştur)");
            System.out.println("2. Search Book (Kitap Ara)");
            System.out.println("3. Borrow Book (Kitap Ödünç Al)");
            System.out.println("4. Return Book (Kitap İade Et)");
            System.out.println("5. Modify Book (Kitap Düzenle)");
            System.out.println("6. Undo Last Modification (Son Düzenlemeyi Geri Al)");
            System.out.println("0. Çıkış");
            System.out.print("Seçiminiz: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1": createBookFlow(); break;
                case "2": searchBookFlow(); break;
                case "3": borrowBookFlow(); break;
                case "4": returnBookFlow(); break;
                case "5": modifyBookFlow(); break;
                case "6": manager.undoLastModification(); break;
                case "0": running = false; System.out.println("Sistem kapatılıyor. Görüşmek üzere!"); break;
                default: System.out.println("Hata: Geçersiz seçim!");
            }
        }
    }

    // --- 1. KİTAP OLUŞTURMA AKIŞI ---
    private static void createBookFlow() {
        System.out.println("\n--- Yeni Kitap Oluştur ---");
        System.out.print("Başlık: "); String title = scanner.nextLine();
        System.out.print("Yazar: "); String author = scanner.nextLine();
        System.out.print("Yayın Yılı: "); int year = Integer.parseInt(scanner.nextLine());
        System.out.print("ISBN: "); String isbn = scanner.nextLine();
        System.out.print("Yayıncı: "); String publisher = scanner.nextLine();

        Book newBook = new Book(title, author, year, isbn, publisher);

        System.out.println("Kategori eklemek ister misiniz? (En fazla 3) (Evet için 'e', Geçmek için 'h')");
        while (scanner.nextLine().equalsIgnoreCase("e")) {
            System.out.print("Kategori adı: ");
            if (!newBook.addCategory(scanner.nextLine())) {
                System.out.println("Maksimum kategori sayısına (3) ulaştınız!"); break;
            }
            System.out.print("Başka kategori? (e/h): ");
        }

        System.out.println("Etiket (Tag) eklemek ister misiniz? (En fazla 3) (Evet için 'e', Geçmek için 'h')");
        while (scanner.nextLine().equalsIgnoreCase("e")) {
            System.out.print("Etiket adı: ");
            if (!newBook.addTag(scanner.nextLine())) {
                System.out.println("Maksimum etiket sayısına (3) ulaştınız!"); break;
            }
            System.out.print("Başka etiket? (e/h): ");
        }

        System.out.print("Kitabı kaydetmek istiyor musunuz? (Save Book) (e/h): ");
        if (scanner.nextLine().equalsIgnoreCase("e")) {
            manager.addBook(newBook);
        } else {
            System.out.println("İptal edildi.");
        }
    }

    // --- 2. KİTAP ARAMA AKIŞI (STRATEGY PATTERN KULLANIMI) ---
    private static void searchBookFlow() {
        System.out.println("\n--- Kitap Ara ---");
        System.out.println("Arama Kriteri: 1-Başlık, 2-Yazar");
        String searchChoice = scanner.nextLine();

        if (searchChoice.equals("1")) {
            manager.setSearchStrategy(new SearchByTitle());
        } else if (searchChoice.equals("2")) {
            manager.setSearchStrategy(new SearchByAuthor());
        } else {
            System.out.println("Geçersiz seçim."); return;
        }

        System.out.print("Aranacak kelime: ");
        String keyword = scanner.nextLine();

        System.out.println("Sıralama: 1-Artan (A-Z), 2-Azalan (Z-A), 3-Sıralama Yok");
        String sortChoice = scanner.nextLine();
        if (sortChoice.equals("1")) manager.setSortStrategy(new TitleAscendingSortStrategy());
        else if (sortChoice.equals("2")) manager.setSortStrategy(new TitleDescendingSortStrategy());
        else manager.setSortStrategy(null);

        List<Book> results = manager.searchBooks(keyword);
        System.out.println("\n--- Arama Sonuçları ---");
        if (results.isEmpty()) {
            System.out.println("Eşleşen kitap bulunamadı.");
        } else {
            for (int i = 0; i < results.size(); i++) {
                System.out.println((i + 1) + ". " + results.get(i).toString());
            }
        }
    }

    // --- 3. KİTAP DÜZENLEME AKIŞI (COMMAND PATTERN KULLANIMI) ---
    private static void modifyBookFlow() {
        System.out.println("\n--- Kitap Düzenle ---");
        List<Book> inventory = manager.getInventory();
        if (inventory.isEmpty()) {
            System.out.println("Kütüphanede düzenlenecek kitap yok."); return;
        }

        for (int i = 0; i < inventory.size(); i++) {
            System.out.println((i + 1) + ". " + inventory.get(i).getTitle());
        }
        System.out.print("Düzenlemek istediğiniz kitabın numarası: ");
        int index = Integer.parseInt(scanner.nextLine()) - 1;

        if (index >= 0 && index < inventory.size()) {
            Book targetBook = inventory.get(index);
            // Mevcut verilerle geçici bir kopyasını oluştur (Yeni verileri tutmak için)
            Book newData = new Book(targetBook);

            System.out.println("Yeni Başlık (Değiştirmek istemiyorsanız boş bırakıp Enter'a basın): ");
            String newTitle = scanner.nextLine();
            if (!newTitle.isEmpty()) newData.setTitle(newTitle);

            System.out.println("Yeni Yazar (Değiştirmek istemiyorsanız boş bırakıp Enter'a basın): ");
            String newAuthor = scanner.nextLine();
            if (!newAuthor.isEmpty()) newData.setAuthor(newAuthor);

            // Command pattern üzerinden değişikliği uygula (Manager bunu history'ye atacak)
            manager.modifyBook(targetBook, newData);
        }
    }

    // --- 4. ÖDÜNÇ ALMA VE İADE AKIŞLARI ---
    private static void borrowBookFlow() {
        System.out.print("\nÖdünç alacak üye ID (Varsayılan: M001): ");
        String memberId = scanner.nextLine();
        Member member = manager.getMemberById(memberId);
        if (member == null) {
            System.out.println("Üye bulunamadı."); return;
        }

        System.out.print("Ödünç alınacak kitabın tam başlığı: ");
        String title = scanner.nextLine();

        manager.setSearchStrategy(new SearchByTitle());
        List<Book> results = manager.searchBooks(title);

        if (!results.isEmpty()) {
            manager.borrowBook(results.get(0), member);
        } else {
            System.out.println("Kitap bulunamadı.");
        }
    }

    private static void returnBookFlow() {
        System.out.print("\nİade edecek üye ID (Varsayılan: M001): ");
        String memberId = scanner.nextLine();
        Member member = manager.getMemberById(memberId);
        if (member == null) { System.out.println("Üye bulunamadı."); return; }

        System.out.print("İade edilecek kitabın tam başlığı: ");
        String title = scanner.nextLine();

        manager.setSearchStrategy(new SearchByTitle());
        List<Book> results = manager.searchBooks(title);

        if (!results.isEmpty()) {
            manager.returnBook(results.get(0), member);
        } else {
            System.out.println("Kitap bulunamadı.");
        }
    }
}