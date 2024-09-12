package org.sviriel.booktesttask.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.sviriel.booktesttask.entity.Book;

import java.util.List;
import java.util.Optional;


public interface BookRepository extends JpaRepository<Book, Long> {
    // optional для методі isPresent та перевірки
    Optional<Book> findByIsbn(String isbn);
    // методи для пошуку
    List<Book> findByAuthorContainingIgnoreCase(String author);
    List<Book> findByTitleContainingIgnoreCase(String title);
    List<Book> findByGenreContainingIgnoreCase(String genre);

    Page<Book> findAll(Pageable pageable);

}
