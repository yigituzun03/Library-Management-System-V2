package com.library.strategy;

import com.library.model.Book;
import java.util.Comparator;
import java.util.List;

public class TitleAscendingSortStrategy implements ISortStrategy {
    @Override
    public void sort(List<Book> books) {
        // Kitapları başlıklarına göre büyük/küçük harf duyarsız sıralar
        books.sort(Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER));
    }
}