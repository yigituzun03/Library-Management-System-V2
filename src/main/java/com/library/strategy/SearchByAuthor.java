package com.library.strategy;

import com.library.model.Book;
import java.util.ArrayList;
import java.util.List;

public class SearchByAuthor implements ISearchStrategy {
    @Override
    public List<Book> search(List<Book> inventory, String keyword) {
        List<Book> results = new ArrayList<>();
        for (Book book : inventory) {
            if (book.getAuthor().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(book);
            }
        }
        return results;
    }
}