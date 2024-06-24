package services;

import networklist1.BookEntity;
import repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public Optional<BookEntity> findByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    public BookEntity saveBook(BookEntity book) {
        return bookRepository.save(book);
    }

    public Iterable<BookEntity> findAllBooks() {
        return bookRepository.findAll();
    }

    public void deleteBook(Long bookId) {
        bookRepository.deleteById(bookId);
    }

    public Optional<BookEntity> updateBook(Long bookId, BookEntity updatedBook) {
        return bookRepository.findById(bookId).map(book -> {
            book.setTitle(updatedBook.getTitle());
            book.setAuthor(updatedBook.getAuthor());
            book.setPublisher(updatedBook.getPublisher());
            book.setYearPublished(updatedBook.getYearPublished());
            book.setAvailableCopies(updatedBook.getAvailableCopies());
            return bookRepository.save(book);
        });
    }
}

