package com.wenderson.bookstore.controllers;

import com.wenderson.bookstore.dtos.BookRecordDto;
import com.wenderson.bookstore.models.BookModel;
import com.wenderson.bookstore.services.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(version = "v1")
    public ResponseEntity<List<BookModel>> getAllBooks() {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.findAll());
    }

    @GetMapping(value = "/{id}", version = "v1")
    public ResponseEntity<Object> getOneBook(@PathVariable(value = "id") UUID id) {
        Optional<BookModel> bookOptional = bookService.findById(id);
        if (bookOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(bookOptional.get());
    }

    @PostMapping(version = "v1")
    public ResponseEntity<BookModel> saveBook(@RequestBody @Valid BookRecordDto bookRecordDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.save(bookRecordDto));
    }

    @PutMapping(value = "/{id}", version = "v1")
    public ResponseEntity<Object> updateBook(@PathVariable(value = "id") UUID id,
                                             @RequestBody @Valid BookRecordDto bookRecordDto) {
        Optional<BookModel> bookOptional = bookService.findById(id);
        if (bookOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(bookService.update(bookOptional.get(), bookRecordDto));
    }

    @DeleteMapping(value = "/{id}", version = "v1")
    public ResponseEntity<Object> deleteBook(@PathVariable(value = "id") UUID id) {
        Optional<BookModel> bookOptional = bookService.findById(id);
        if (bookOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found.");
        }
        bookService.delete(bookOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Book deleted successfully.");
    }
}
