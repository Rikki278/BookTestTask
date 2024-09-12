package org.sviriel.booktesttask.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.sviriel.booktesttask.entity.Book;
import org.sviriel.booktesttask.service.BookService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/all-books")
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.findAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Book>> getBookById(@PathVariable("id") Long id) {
        Optional<Book> book = bookService.findBookById(id);

        // якщо книга по айді існує - отримуємо її
        if (book.isPresent()) {
            return ResponseEntity.ok(book);
        }

        // якщо не існує - не знайдено
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/post")
    public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) {
        // якщо книга котру ми хочемо завантажити має ISBN такий самий як у іншої книги
        // повертаємо статус конфлікту
        if (bookService.isIsbnExists(book.getIsbn())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null); // 409 Conflict
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.saveBook(book));
    }

    @PutMapping("/update")
    public ResponseEntity<Book> updateBook(@RequestBody Book book) {
        Optional<Book> existingBook = bookService.findBookById(book.getId());

        if (existingBook.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (bookService.isIsbnExists(book.getIsbn())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        return ResponseEntity.ok(bookService.updateBook(book));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable("id") Long id) {
        Optional<Book> book = bookService.findBookById(id);

        // якщо книги немає - повертаємо статус, що не знайдено
        if (book.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found");
        }

        bookService.deleteBookById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBook(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String genre) {

        List<Book> books = List.of();

        // пошук за категоріями
        if (author != null) {
            books = bookService.findBookByAuthor(author);
        }
        if (genre != null) {
            books = bookService.findBookByGenre(genre);
        }
        if (title != null) {
            books = bookService.findBookByTitle(title);
        }

        if (books.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(books);

    }

    @GetMapping("/pages")
    public ResponseEntity<Page<Book>> getBooks(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "5") int size) {

        Page<Book> books = bookService.findAllBooks(page, size);
        return ResponseEntity.ok(books);
    }


}

