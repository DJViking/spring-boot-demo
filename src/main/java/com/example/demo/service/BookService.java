package com.example.demo.service;

import java.util.Optional;

import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;

    public BookService(final BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Optional<Book> getBook(final Long bookNumber) {
        return bookRepository.findById(bookNumber);
    }

}
