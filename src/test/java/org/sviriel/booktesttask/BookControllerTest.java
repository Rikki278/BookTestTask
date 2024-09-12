package org.sviriel.booktesttask;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.sviriel.booktesttask.controller.BookController;
import org.sviriel.booktesttask.entity.Book;
import org.sviriel.booktesttask.service.BookService;

import java.time.Year;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@WebMvcTest(BookController.class)
public class BookControllerTest {

    @MockBean
    private BookService bookService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void getAllBooks() throws Exception {

        Book book = new Book();
        book.setTitle("Book Title");
        book.setAuthor("Author");
        book.setGenre("Genre");
        book.setIsbn("9780316219137");

        Book book2 = new Book();
        book2.setTitle("Book Title2");
        book2.setAuthor("Author");
        book2.setGenre("Genre2");
        book2.setIsbn("9780316219132");
        List<Book> books = Arrays.asList(book, book2);
        when(bookService.findAllBooks()).thenReturn(books);

        mockMvc.perform(get("/api/book/all-books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Book Title"))
                .andExpect(jsonPath("$[1].title").value("Book Title2"))
        ;
    }

    @Test
    void getBookById() throws Exception {
        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Book");
        book.setAuthor("Author");
        book.setGenre("Genre");
        book.setPublicationYear(Year.of(2022));
        book.setIsbn("9780316219137");

        when(bookService.findBookById(bookId)).thenReturn(Optional.of(book));

        mockMvc.perform(get("/api/book/{id}", bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookId))
                .andExpect(jsonPath("$.title").value("Book"))
                .andExpect(jsonPath("$.author").value("Author"))
                .andExpect(jsonPath("$.publicationYear").value(2022))
                .andExpect(jsonPath("$.genre").value("Genre"))
                .andExpect(jsonPath("$.isbn").value("9780316219137"));
    }

    @Test
    void getBookByIdNotFound() throws Exception {
        Long bookId = 1L;

        when(bookService.findBookById(bookId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/book/{id}", bookId))
                .andExpect(status().isNotFound());
    }


    @Test
    void postBook() throws Exception {
        Book book = new Book();
        book.setTitle("New Book");
        book.setAuthor("New Author");
        book.setIsbn("1234567890126");
        book.setPublicationYear(Year.of(2021));
        book.setGenre("Genre");

        System.out.println(book);
        when(bookService.saveBook(any(Book.class))).thenReturn(book);
        String bookJson = mapper.registerModule(new JavaTimeModule()).writeValueAsString(book);

        mockMvc.perform(post("/api/book/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Book"));
    }


    @Test
    void postNoTitleBook() throws Exception {
        Book book = new Book();
        book.setAuthor("Wrong");
        book.setIsbn("1234567890122");
        book.setPublicationYear(Year.of(2021));
        book.setGenre("Wrong");

        String bookJson = mapper.registerModule(new JavaTimeModule()).writeValueAsString(book);

        mockMvc.perform(post("/api/book/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postNoAuthorBook() throws Exception {
        Book book = new Book();
        book.setTitle("New Book");
        book.setIsbn("1234567890122");
        book.setPublicationYear(Year.of(2021));
        book.setGenre("Wrong");

        String bookJson = mapper.registerModule(new JavaTimeModule()).writeValueAsString(book);

        mockMvc.perform(post("/api/book/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postWrongYearBook() throws Exception {
        Book book = new Book();
        book.setTitle("New Book");
        book.setAuthor("Author");
        book.setIsbn("1234567890122");
        book.setPublicationYear(Year.of(2025));
        book.setGenre("Wrong");

        String bookJson = mapper.registerModule(new JavaTimeModule()).writeValueAsString(book);

        mockMvc.perform(post("/api/book/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.registerModule(new JavaTimeModule()).writeValueAsString(book)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postSameIsbnBook() throws Exception {
        String ISBN = "9780316769488";

        when(bookService.isIsbnExists(ISBN)).thenReturn(true);

        Book book = new Book();
        book.setTitle("New Book");
        book.setAuthor("Author");
        book.setIsbn(ISBN);
        book.setPublicationYear(Year.of(2021));
        book.setGenre("Wrong");

        String bookJson = mapper.registerModule(new JavaTimeModule()).writeValueAsString(book);

        mockMvc.perform(post("/api/book/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(status().isConflict());
    }

    @Test
    void updateBook() throws Exception {
        Long bookId = 1L;
        Book updatedBook = new Book();
        updatedBook.setId(bookId);
        updatedBook.setTitle("Updated Book");
        updatedBook.setAuthor("Updated Author");
        updatedBook.setGenre("Updated Genre");
        updatedBook.setPublicationYear(Year.of(2022));
        updatedBook.setIsbn("9780316219137");

        when(bookService.findBookById(bookId)).thenReturn(Optional.of(updatedBook));
        when(bookService.updateBook(updatedBook)).thenReturn(updatedBook);

        mockMvc.perform(put("/api/book/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.registerModule(new JavaTimeModule()).writeValueAsString(updatedBook)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Updated Book"))
                .andExpect(jsonPath("$.author").value("Updated Author"))
                .andExpect(jsonPath("$.genre").value("Updated Genre"))
                .andExpect(jsonPath("$.publicationYear").value(2022));

    }

    @Test
    void updateBookEmpty() throws Exception {
        Long bookId = 1L;
        Book updatedBook = new Book();
        updatedBook.setId(bookId);
        updatedBook.setTitle("Updated Book");
        updatedBook.setAuthor("Updated Author");
        updatedBook.setGenre("Updated Genre");
        updatedBook.setPublicationYear(Year.of(2022));
        updatedBook.setIsbn("9780316219137");

        when(bookService.findBookById(bookId)).thenReturn(Optional.empty());
        when(bookService.updateBook(updatedBook)).thenReturn(updatedBook);

        mockMvc.perform(put("/api/book/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.registerModule(new JavaTimeModule()).writeValueAsString(updatedBook)))
                .andExpect(status().isNotFound());

    }

    @Test
    void deleteBook() throws Exception {
        Long bookId = 1L;
        doNothing().when(bookService).deleteBookById(bookId);

        when(bookService.findBookById(bookId)).thenReturn(Optional.of(new Book()));

        mockMvc.perform(delete("/api/book/delete/{id}", bookId))
                .andExpect(status().isNoContent());

        verify(bookService, times(1)).deleteBookById(bookId);
    }

    @Test
    void deleteBookNotFound() throws Exception {
        Long bookId = 1L;

        when(bookService.findBookById(bookId)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/book/delete/{id}", bookId))
                .andExpect(status().isNotFound());

        verify(bookService, times(0)).deleteBookById(bookId);

    }

    @Test
    void searchBookByGenre() throws Exception {
        Book book = new Book();

        book.setTitle("Book");
        book.setAuthor("Author");
        book.setGenre("Horror");
        book.setPublicationYear(Year.of(2022));
        book.setIsbn("9780316219137");

        when(bookService.findBookByGenre("Horror")).thenReturn(List.of(book));

        mockMvc.perform(get("/api/book/search")
                        .param("genre", "Horror"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Book"))
                .andExpect(jsonPath("$[0].author").value("Author"))
                .andExpect(jsonPath("$[0].genre").value("Horror"))
                .andExpect(jsonPath("$[0].publicationYear").value(2022))
                .andExpect(jsonPath("$[0].isbn").value("9780316219137"));
    }

    @Test
    void searchBookByTitle() throws Exception {
        Book book = new Book();

        book.setTitle("The Witcher");
        book.setAuthor("Author");
        book.setGenre("Horror");
        book.setPublicationYear(Year.of(2022));
        book.setIsbn("9780316219137");

        when(bookService.findBookByTitle("The Witcher")).thenReturn(List.of(book));

        mockMvc.perform(get("/api/book/search")
                        .param("title", "The Witcher"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("The Witcher"))
                .andExpect(jsonPath("$[0].author").value("Author"))
                .andExpect(jsonPath("$[0].genre").value("Horror"))
                .andExpect(jsonPath("$[0].publicationYear").value(2022))
                .andExpect(jsonPath("$[0].isbn").value("9780316219137"));
    }

    @Test
    void searchBookByAuthor() throws Exception {
        Book book = new Book();

        book.setTitle("The Witcher");
        book.setAuthor("Andrzej Sapkowski");
        book.setGenre("Horror");
        book.setPublicationYear(Year.of(2022));
        book.setIsbn("9780316219137");

        when(bookService.findBookByAuthor("Andrzej Sapkowski")).thenReturn(List.of(book));

        mockMvc.perform(get("/api/book/search")
                        .param("author", "Andrzej Sapkowski"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("The Witcher"))
                .andExpect(jsonPath("$[0].author").value("Andrzej Sapkowski"))
                .andExpect(jsonPath("$[0].genre").value("Horror"))
                .andExpect(jsonPath("$[0].publicationYear").value(2022))
                .andExpect(jsonPath("$[0].isbn").value("9780316219137"));
    }

    @Test
    void searchWrong() throws Exception {
        when(bookService.findBookByTitle("The Witcher")).thenReturn(List.of());

        mockMvc.perform(get("/api/book/search")
                        .param("title", "The Witcher"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getBooksWIthPagination() throws Exception {

        Book book1 = new Book();

        book1.setTitle("The Witcher");
        book1.setAuthor("Andrzej Sapkowski");
        book1.setGenre("Fantasy");
        book1.setPublicationYear(Year.of(1996));
        book1.setIsbn("9780316219137");

        Book book2 = new Book();

        book2.setTitle("Fight Club");
        book2.setAuthor("Chuck Palahniuk");
        book2.setGenre("Psychological Fiction");
        book2.setPublicationYear(Year.of(1996));
        book2.setIsbn("9780393327342");

        List<Book> books = Arrays.asList(book1, book2);
        Page<Book> bookPage = new PageImpl<Book>(books, PageRequest.of(0, 10), books.size());

        when(bookService.findAllBooks(0, 10)).thenReturn(bookPage);

        mockMvc.perform(get("/api/book/pages")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("The Witcher"))
                .andExpect(jsonPath("$.content[1].title").value("Fight Club"))
                .andExpect(jsonPath("$.content[0].author").value("Andrzej Sapkowski"))
                .andExpect(jsonPath("$.content[1].author").value("Chuck Palahniuk"))
                .andExpect(jsonPath("$.content[0].genre").value("Fantasy"))
                .andExpect(jsonPath("$.content[1].genre").value("Psychological Fiction"))
                .andExpect(jsonPath("$.content[0].publicationYear").value(1996))
                .andExpect(jsonPath("$.content[1].publicationYear").value(1996))
                .andExpect(jsonPath("$.content[0].isbn").value("9780316219137"))
                .andExpect(jsonPath("$.content[1].isbn").value("9780393327342"))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.number").value(0));
    }
}

