package com.library.command;

import com.library.model.Book;

public class ModifyBookCommand implements ICommand {

    private Book targetBook; 
    private Book backupBook; 
    private Book newData;    

    public ModifyBookCommand(Book targetBook, Book newData) {
        this.targetBook = targetBook;
        this.newData    = newData;
    }

    @Override
    public void execute() {

        this.backupBook = new Book(targetBook);

        targetBook.setTitle(newData.getTitle());
        targetBook.setAuthor(newData.getAuthor());
        targetBook.setPublicationYear(newData.getPublicationYear());
        targetBook.setIsbn(newData.getIsbn());
        targetBook.setPublisher(newData.getPublisher());
        targetBook.setDescription(newData.getDescription());
        targetBook.setCategories(newData.getCategories());
        targetBook.setTags(newData.getTags());

        System.out.println("  [OK]  Book updated: \"" + targetBook.getTitle() + "\"");
    }

    @Override
    public void undo() {
        if (backupBook != null) {
            targetBook.setTitle(backupBook.getTitle());
            targetBook.setAuthor(backupBook.getAuthor());
            targetBook.setPublicationYear(backupBook.getPublicationYear());
            targetBook.setIsbn(backupBook.getIsbn());
            targetBook.setPublisher(backupBook.getPublisher());
            targetBook.setDescription(backupBook.getDescription());
            targetBook.setCategories(backupBook.getCategories());
            targetBook.setTags(backupBook.getTags());

            System.out.println("  [OK]  Undo successful. Book restored to previous state.");
        }
    }
}