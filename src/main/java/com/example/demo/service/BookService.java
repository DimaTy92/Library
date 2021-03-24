package com.example.demo.service;

import com.example.demo.entity.Book;

import java.util.List;

public interface BookService {
    List<Book> findAllBooks();

    Book getBookById(Long id);

    Book saveBook(Book book);

    void deleteBookById(Long id);

    Book updateBookById(Long id, Book book);
}

