package com.example.book_app.service;

import com.example.book_app.entity.Book;
import com.example.book_app.repository.BookRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book addBook(Book book) {
        if (book == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book payload is required");
        }
        return bookRepository.save(book);
    }

    public List<Book> getBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Integer id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book id is required");
        }
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found with id: " + id));
    }

    public Book updateBook(@Valid Book book) {
        if (book == null || book.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book payload with valid id is required");
        }
        if (!bookRepository.existsById(book.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found with id: " + book.getId());
        }
        return bookRepository.save(book);
    }

    public void deleteBookById(Integer id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book id is required");
        }
        if (!bookRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }

    public void deleteAllBooks() {
        if(bookRepository.count() == 0){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No books found to delete");
        }
        bookRepository.deleteAll();
    }
}
