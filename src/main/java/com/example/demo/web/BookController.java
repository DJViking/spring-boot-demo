package com.example.demo.web;

import com.example.demo.dto.BookDTO;
import com.example.demo.domain.Book;
import com.example.demo.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/book")
public class BookController {

    private final BookService bookService;

    public BookController(final BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<BookDTO> getBookByNumber(final Long bookNumber) {
        return bookService.getBook(bookNumber)
            .map(this::toDto)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    private BookDTO toDto(final Book book) {
        return BookDTO.builder()
            .bookNumber(book.getId())
            .name(book.getName())
            .author(book.getAuthor())
            .build();
    }
}
