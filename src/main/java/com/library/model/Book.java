package com.library.model;

import java.util.ArrayList;
import java.util.List;

public class Book {
    private String title;
    private String author;
    private int    publicationYear;
    private String isbn;
    private String publisher;
    private String description;
    private List<String> categories;
    private List<String> tags;
    private boolean isAvailable;
    private int     borrowCount;

    // Standard constructor — used when creating a new book
    public Book(String title, String author, int publicationYear, String isbn, String publisher) {
        this.title           = title;
        this.author          = author;
        this.publicationYear = publicationYear;
        this.isbn            = isbn;
        this.publisher       = publisher;
        this.description     = "";
        this.categories      = new ArrayList<>();
        this.tags            = new ArrayList<>();
        this.isAvailable     = true;
        this.borrowCount     = 0;
    }

    // Deep-copy constructor — critical for Command Pattern undo support
    public Book(Book source) {
        this.title           = source.title;
        this.author          = source.author;
        this.publicationYear = source.publicationYear;
        this.isbn            = source.isbn;
        this.publisher       = source.publisher;
        this.description     = source.description;
        this.categories      = new ArrayList<>(source.categories); // new list, not shared reference
        this.tags            = new ArrayList<>(source.tags);
        this.isAvailable     = source.isAvailable;
        this.borrowCount     = source.borrowCount;
    }

    // Maximum 3 categories and 3 tags per book (project requirement)

    public boolean addCategory(String category) {
        if (this.categories.size() < 3) {
            this.categories.add(category);
            return true;
        }
        return false; // limit reached
    }

    public boolean addTag(String tag) {
        if (this.tags.size() < 3) {
            this.tags.add(tag);
            return true;
        }
        return false; // limit reached
    }

    public void incrementBorrowCount() {
        this.borrowCount++;
    }

    @Override
    public String toString() {
        return "Book [" + title + " | " + author + " | ISBN: " + isbn
            + " | " + (isAvailable ? "Available" : "Borrowed") + "]";
    }

    // Getters and Setters

    public String getTitle()                        { return title; }
    public void   setTitle(String title)            { this.title = title; }

    public String getAuthor()                       { return author; }
    public void   setAuthor(String author)          { this.author = author; }

    public int    getPublicationYear()              { return publicationYear; }
    public void   setPublicationYear(int year)      { this.publicationYear = year; }

    public String getIsbn()                         { return isbn; }
    public void   setIsbn(String isbn)              { this.isbn = isbn; }

    public String getPublisher()                    { return publisher; }
    public void   setPublisher(String publisher)    { this.publisher = publisher; }

    public String getDescription()                  { return description; }
    public void   setDescription(String description){ this.description = description; }

    public List<String> getCategories()             { return categories; }
    public void setCategories(List<String> cats)    { this.categories = new ArrayList<>(cats); }

    public List<String> getTags()                   { return tags; }
    public void setTags(List<String> tags)          { this.tags = new ArrayList<>(tags); }

    public boolean isAvailable()                    { return isAvailable; }
    public void    setAvailable(boolean available)  { this.isAvailable = available; }

    public int getBorrowCount()                     { return borrowCount; }
}