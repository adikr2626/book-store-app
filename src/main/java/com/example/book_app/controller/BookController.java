package com.example.book_app.controller;

import com.example.book_app.entity.Book;
import com.example.book_app.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookService")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/addBook")
    public ResponseEntity<Book> addBook(@Valid @RequestBody Book book) {
        Book savedBook = bookService.addBook(book);
        return ResponseEntity.ok(savedBook);
    }

    @GetMapping("/getAllBooks")
    public List<Book> getAllBooks() {
        return bookService.getBooks();
    }

    @GetMapping({"/getBookById/{id}", "/getBookById"})
    public ResponseEntity<Book> getBookById(
            @PathVariable(required = false) Integer id,
            @RequestParam(required = false, name = "id") Integer queryId) {
        Integer resolvedId = id != null ? id : queryId;
        return ResponseEntity.ok(bookService.getBookById(resolvedId));
    }

    //Use Put method to update book resource
    @PutMapping("/updateBook")
    public ResponseEntity<Book> updateBook(@Valid @RequestBody Book book) {
        Book updatedBook = bookService.updateBook(book);
        return ResponseEntity.ok(updatedBook);
    }

    @PatchMapping("/updateBook/{id}")
    public ResponseEntity<Book> updateBookPartially(@PathVariable Integer id, @RequestBody Book book) {
        Book existingBook = bookService.getBookById(id);
        if (book.getTitle() != null) {
            existingBook.setTitle(book.getTitle());
        }
        if (book.getAuthor() != null) {
            existingBook.setAuthor(book.getAuthor());
        }
        if (book.getPublisher() != null) {
            existingBook.setPublisher(book.getPublisher());
        }
        Book updatedBook = bookService.updateBook(existingBook);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/deleteBook/{id}")
    public ResponseEntity<Void> deleteBookById(@PathVariable Integer id) {
        bookService.deleteBookById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("delete-all-books")
    public ResponseEntity<Void> deleteAllBooks(){
        bookService.deleteAllBooks();
        return ResponseEntity.noContent().build();
    }
}
