package org.sviriel.booktesttask.service;

import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.sviriel.booktesttask.entity.Book;
import org.sviriel.booktesttask.repository.BookRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    // зберігання книги
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    // пошук всіх книг
    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    // пошук книги по айді
    public Optional<Book> findBookById(Long id) {
        return bookRepository.findById(id);
    }

    // видалення книги по айді
    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }

    // оновлення книги
    public Book updateBook(Book updatedBook) {
        // шукаємо книгу
        Book book = bookRepository.findById(updatedBook.getId())
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));

        Optional<Book> bookWithSameIsbn = bookRepository.findByIsbn(updatedBook.getIsbn());

        // перевірка на те що книга з однаковим ISBN існує,
        // та перевіряємо якщо така книга існує, а її айді не збігається з айді книги котру оновлюємо,
        // то кидаємо ексепшн
        if (bookWithSameIsbn.isPresent() && !bookWithSameIsbn.get().getId().equals(updatedBook.getId())) {
            throw new IllegalArgumentException("ISBN already exists for another book");
        }

        book.setTitle(updatedBook.getTitle());
        book.setGenre(updatedBook.getGenre());
        book.setAuthor(updatedBook.getAuthor());
        book.setPublicationYear(updatedBook.getPublicationYear());

        return bookRepository.save(book);
    }

    // пошук за категоріями
    public List<Book> findBookByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Book> findBookByGenre(String genre) {
        return bookRepository.findByGenreContainingIgnoreCase(genre);
    }

    public List<Book> findBookByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author);
    }

    // пошук всіх книг з пагінацією
    public Page<Book> findAllBooks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return bookRepository.findAll(pageable);
    }

    public boolean isIsbnExists(String isbn) {
        return bookRepository.findByIsbn(isbn).isPresent();
    }

}
