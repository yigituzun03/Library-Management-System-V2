package com.library.strategy;

import com.library.model.Book;
import java.util.Comparator;
import java.util.List;

public class TitleDescendingSortStrategy implements ISortStrategy {
    @Override
    public void sort(List<Book> books) {
        // reversed() metodu ile tam tersine çeviriyoruz
        books.sort(Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER).reversed());
    }
}