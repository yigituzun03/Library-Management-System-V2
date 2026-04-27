package com.library.strategy;

import com.library.model.Book;
import java.util.List;

public interface ISortStrategy {
    void sort(List<Book> books);
}