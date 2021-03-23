package com.example.demo.service.impl;

import com.example.demo.entity.Book;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book getBookById(Long id) {
        return bookRepository.getBookById(id);
    }

    @Override
    public Book findBookByName(String name) {
        return bookRepository.findBookByName(name);
    }

    @Override
    @Transactional
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public Book findBookByWriter(String writer) {
        return bookRepository.findBookByWriter(writer);
    }

    @Override
    @Transactional
    public Book updateBookById(Long id, Book book) {
        book.setId(id);
        return bookRepository.save(book);
    }


    @PostConstruct
    public void init() {
        Book book1 = new Book();
        book1.setName("In Search of Lost Time");
        book1.setWriter("Marcel Proust");

        bookRepository.save(book1);

        Book book2 = new Book();
        book2.setName("Ulysses");
        book2.setWriter("James Joyce");
        bookRepository.save(book2);
    }
}
