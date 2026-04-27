package com.library.strategy;

import com.library.model.Book;
import java.util.List;

public interface ISearchStrategy {
    List<Book> search(List<Book> inventory, String keyword);
}