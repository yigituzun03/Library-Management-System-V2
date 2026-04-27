package com.library.command;

import com.library.model.Book;

public class ModifyBookCommand implements ICommand {
    private Book targetBook; // Değiştirilecek asıl kitap
    private Book backupBook; // Geri alma işlemi için tutulan kopya
    private Book newData;    // Kullanıcının girdiği yeni veriler

    public ModifyBookCommand(Book targetBook, Book newData) {
        this.targetBook = targetBook;
        this.newData = newData;
    }

    @Override
    public void execute() {
        // 1. İşlemden önce asıl kitabın derin kopyasını al (Yedekle)
        this.backupBook = new Book(targetBook);

        // 2. Yeni verileri asıl kitabın üzerine yaz
        targetBook.setTitle(newData.getTitle());
        targetBook.setAuthor(newData.getAuthor());
        targetBook.setPublicationYear(newData.getPublicationYear());
        targetBook.setIsbn(newData.getIsbn());
        targetBook.setPublisher(newData.getPublisher());
        targetBook.setDescription(newData.getDescription());
        targetBook.setCategories(newData.getCategories());
        targetBook.setTags(newData.getTags());

        System.out.println("Başarılı: '" + targetBook.getTitle() + "' adlı kitabın bilgileri güncellendi.");
    }

    @Override
    public void undo() {
        if (backupBook != null) {
            // Yedekteki eski verileri asıl kitabın üzerine geri yaz
            targetBook.setTitle(backupBook.getTitle());
            targetBook.setAuthor(backupBook.getAuthor());
            targetBook.setPublicationYear(backupBook.getPublicationYear());
            targetBook.setIsbn(backupBook.getIsbn());
            targetBook.setPublisher(backupBook.getPublisher());
            targetBook.setDescription(backupBook.getDescription());
            targetBook.setCategories(backupBook.getCategories());
            targetBook.setTags(backupBook.getTags());

            System.out.println("Geri Alma Başarılı: Kitap eski haline döndürüldü.");
        }
    }
}