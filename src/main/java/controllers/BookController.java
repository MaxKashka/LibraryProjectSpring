package controllers;

import dto.BookDTO;
import networklist1.BookEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import repositories.BookRepository;
import dto.DTOConverter;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookRepository bookRepository;

    @Autowired
    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('LIBRARIAN')")
    @ResponseStatus(code = HttpStatus.CREATED)
    public @ResponseBody BookDTO addBook(@RequestBody BookDTO bookDTO) {
        bookRepository.findByIsbn(bookDTO.getIsbn())
                .ifPresent(b -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book with this ISBN already exists: " + bookDTO.getIsbn());
                });
        BookEntity book = DTOConverter.convertToBookEntity(bookDTO);
        BookEntity savedBook = bookRepository.save(book);
        return DTOConverter.convertToBookDTO(savedBook);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @RequestBody BookDTO updatedBookDTO) {
        return bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(updatedBookDTO.getTitle());
                    book.setAuthor(updatedBookDTO.getAuthor());
                    book.setPublisher(updatedBookDTO.getPublisher());
                    book.setYearPublished(updatedBookDTO.getYearPublished());
                    book.setAvailableCopies(updatedBookDTO.getAvailableCopies());
                    BookEntity updatedBook = bookRepository.save(book);
                    return new ResponseEntity<>(DTOConverter.convertToBookDTO(updatedBook), HttpStatus.OK);
                }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        return bookRepository.findById(id)
                .map(book -> {
                    bookRepository.delete(book);
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/getAll")
    public @ResponseBody Iterable<BookDTO> getAll() {
        return bookRepository.findAll().stream().map(DTOConverter::convertToBookDTO).collect(Collectors.toList());
    }
}



