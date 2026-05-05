package com.library.strategy;

import com.library.model.Book;
import java.util.ArrayList;
import java.util.List;

/**
 * Strategy: Kitabın etiketleri (tags) arasında anahtar kelimeyi arar.
 */
public class SearchByTag implements ISearchStrategy {
    @Override
    public List<Book> search(List<Book> inventory, String keyword) {
        List<Book> results = new ArrayList<>();
        for (Book book : inventory) {
            for (String tag : book.getTags()) {
                if (tag.toLowerCase().contains(keyword.toLowerCase())) {
                    results.add(book);
                    break; // Aynı kitabı birden fazla kez eklememek için
                }
            }
        }
        return results;
    }
}
