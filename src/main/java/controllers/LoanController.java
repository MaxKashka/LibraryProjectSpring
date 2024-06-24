package controllers;

import dto.LoanDTO;
import networklist1.LoanEntity;
import networklist1.BookEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import repositories.BookRepository;
import repositories.LoanRepository;
import dto.DTOConverter;

@RestController
@RequestMapping("/loans")
public class LoanController {
    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;

    @Autowired
    public LoanController(LoanRepository loanRepository, BookRepository bookRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
    }

    @PostMapping("/borrow")
    @PreAuthorize("hasRole('READER')")
    public ResponseEntity<?> borrowBook(@RequestBody LoanDTO loanDTO) {
        return bookRepository.findById(loanDTO.getBookId())
                .map(book -> {
                    if (book.getAvailableCopies() > 0) {
                        LoanEntity loan = DTOConverter.convertToLoanEntity(loanDTO);
                        loan.setBook(book);
                        LoanEntity savedLoan = loanRepository.save(loan);
                        book.setAvailableCopies(book.getAvailableCopies() - 1);
                        bookRepository.save(book);
                        return ResponseEntity.ok().body("User borrowed the book!");
                    } else {
                        return new ResponseEntity<>("No copies available!", HttpStatus.BAD_REQUEST);
                    }
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found!"));
    }

    @PostMapping("/return/{loanId}")
    @PreAuthorize("hasRole('READER')")
    public ResponseEntity<?> returnBook(@PathVariable Long loanId) {
        return loanRepository.findById(loanId)
                .map(loan -> {
                    loanRepository.delete(loan);
                    BookEntity book = loan.getBook();
                    book.setAvailableCopies(book.getAvailableCopies() + 1);
                    bookRepository.save(book);
                    return ResponseEntity.ok().body("User returned the book!");
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Loan not found!"));
    }
}





