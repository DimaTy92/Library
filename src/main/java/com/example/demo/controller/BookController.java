package com.example.demo.controller;

import com.example.demo.entity.Book;
import com.example.demo.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping
    public List<Book> findAllBooks() {
        return bookService.findAllBooks();
    }

    @GetMapping("/{bookId}")
    public Book getBookByID(@PathVariable("bookId") Long id) {
        return bookService.getBookById(id);
    }


    @GetMapping("/search")
    public Book getBookByName(@RequestParam("name") String name) {
        return bookService.findBookByName(name);
    }

    @PostMapping
    public Book createBook(@RequestBody Book book) {
        return bookService.saveBook(book);
    }

    @DeleteMapping("/{bookId}")
    public void deleteBook(@PathVariable("bookId") Long id) {
        bookService.deleteBookById(id);
    }

    @GetMapping("/search-by-writer")
    public Book getBookByWrite(@RequestParam("writer") String writer) {
        return bookService.findBookByWriter(writer);
    }

    @PutMapping("/{bookId}")
    public Book updateBookById(@PathVariable("bookId") Long id, @RequestBody Book book) {
        return bookService.updateBookById(id, book);
    }



}
