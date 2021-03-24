package com.example.demo.service;

import com.example.demo.entity.Book;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.impl.BookServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void findAllBooks_WhenDataExist_ThenReturnValidBooks() {
        Book book = mock(Book.class);
        when(bookRepository.findAll()).thenReturn(List.of(book));

        List<Book> allBooks = bookService.findAllBooks();
        assertThat(allBooks).hasSize(1).containsOnly(book);
    }

    @Test
    void findAllBooks_WhenDataNotExist_ThenReturnEmptyList() {
        when(bookRepository.findAll()).thenReturn(Collections.emptyList());
        List<Book> allBooks = bookService.findAllBooks();
        assertThat(allBooks).isEmpty();
    }

    @Test
    void getBookById_WhenBookExist_ThenReturnBook() {
        Book book = new Book();
        when(bookRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(book));

        Book bookById = bookService.getBookById(1L);
        assertThat(book).isEqualTo(bookById);
    }

    @Test
    void getBookById_WhenBookDoesntExist_ThenThrowException(){
        when(bookRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());
        assertThatThrownBy(()->bookService.getBookById(1L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void saveBook_WhenBookSave_ThenReturnThisBook() {
        Book book = new Book();
        when(bookRepository.save(book)).thenReturn(book);
        Book savedBook = bookService.saveBook(book);
        assertThat(book).isEqualTo(savedBook);

    }

}
