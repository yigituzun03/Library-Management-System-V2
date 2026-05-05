package com.library.strategy;

import com.library.model.Book;
import java.util.ArrayList;
import java.util.List;

public class SearchByCategory implements ISearchStrategy {
    @Override
    public List<Book> search(List<Book> inventory, String keyword) {
        List<Book> results = new ArrayList<>();
        for (Book book : inventory) {
            for (String category : book.getCategories()) {
                if (category.toLowerCase().contains(keyword.toLowerCase())) {
                    results.add(book);
                    break; 
                }
            }
        }
        return results;
    }
}
