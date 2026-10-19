package com.wenderson.bookstore.services;

import com.wenderson.bookstore.dtos.BookRecordDto;
import com.wenderson.bookstore.models.BookModel;
import com.wenderson.bookstore.repositories.BookRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final ReviewService reviewService;

    public BookService(BookRepository bookRepository, ReviewService reviewService) {
        this.bookRepository = bookRepository;
        this.reviewService = reviewService;
    }

    public List<BookModel> findAll() {
        return bookRepository.findAll();
    }

    public Optional<BookModel> findById(UUID id) {
        return bookRepository.findById(id);
    }

    public BookModel save(BookRecordDto bookRecordDto) {
        var bookModel = new BookModel();
        BeanUtils.copyProperties(bookRecordDto, bookModel);
        bookModel.setReview(reviewService.generateReview(bookRecordDto.title()));
        return bookRepository.save(bookModel);
    }

    public BookModel update(BookModel bookModel, BookRecordDto bookRecordDto) {
        BeanUtils.copyProperties(bookRecordDto, bookModel);
        return bookRepository.save(bookModel);
    }

    public void delete(BookModel bookModel) {
        bookRepository.delete(bookModel);
    }
}
