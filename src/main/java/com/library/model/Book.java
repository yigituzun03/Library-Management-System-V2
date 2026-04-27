package com.library.model;

import java.util.ArrayList;
import java.util.List;

public class Book {
    private String title;
    private String author;
    private int publicationYear;
    private String isbn;
    private String publisher;
    private String description;
    private List<String> categories;
    private List<String> tags;
    private boolean isAvailable;
    private int borrowCount;

    // 1. Standart Yapıcı Metot (Yeni kitap oluştururken kullanılır)
    public Book(String title, String author, int publicationYear, String isbn, String publisher) {
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.isbn = isbn;
        this.publisher = publisher;
        this.description = ""; // Başlangıçta boş
        this.categories = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.isAvailable = true; // Kütüphaneye yeni eklenen kitap müsaittir
        this.borrowCount = 0;
    }

    // 2. Kopya Yapıcı Metot (Command Pattern'da "Undo" işlemi için çok kritik)
    public Book(Book source) {
        this.title = source.title;
        this.author = source.author;
        this.publicationYear = source.publicationYear;
        this.isbn = source.isbn;
        this.publisher = source.publisher;
        this.description = source.description;
        // Referans kopyalamak yerine yeni liste oluşturuyoruz (Deep Copy)
        this.categories = new ArrayList<>(source.categories);
        this.tags = new ArrayList<>(source.tags);
        this.isAvailable = source.isAvailable;
        this.borrowCount = source.borrowCount;
    }

    // --- Proje Kısıtlamaları: Maksimum 3 Kategori / Etiket ---

    public boolean addCategory(String category) {
        if (this.categories.size() < 3) {
            this.categories.add(category);
            return true; // Ekleme başarılı
        }
        return false; // Sınır aşıldı
    }

    public boolean addTag(String tag) {
        if (this.tags.size() < 3) {
            this.tags.add(tag);
            return true; // Ekleme başarılı
        }
        return false; // Sınır aşıldı
    }

    // --- Temel Metotlar ---

    public void incrementBorrowCount() {
        this.borrowCount++;
    }

    // Konsolda aramaları ve listeleri düzgün göstermek için
    @Override
    public String toString() {
        return "Book [" + title + " | " + author + " | ISBN: " + isbn + " | Available: " + isAvailable + "]";
    }

    // --- Getter ve Setter Metotları (Command Pattern için gerekli) ---
    // (Aşağıdakileri IntelliJ'de Alt+Insert veya sağ tık -> Generate -> Getter and Setter ile de üretebilirsin, buraya ekliyorum)

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public int getPublicationYear() { return publicationYear; }
    public void setPublicationYear(int publicationYear) { this.publicationYear = publicationYear; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<String> getCategories() { return categories; }
    public void setCategories(List<String> categories) { this.categories = new ArrayList<>(categories); }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = new ArrayList<>(tags); }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    public int getBorrowCount() { return borrowCount; }
}