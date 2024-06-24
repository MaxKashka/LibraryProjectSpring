package dto;

import networklist1.BookEntity;
import networklist1.LoanEntity;
import networklist1.UserEntity;

public class DTOConverter {

    public static BookDTO convertToBookDTO(BookEntity book) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(book.getId());
        bookDTO.setIsbn(book.getIsbn());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setAuthor(book.getAuthor());
        bookDTO.setPublisher(book.getPublisher());
        bookDTO.setYearPublished(book.getYearPublished());
        bookDTO.setAvailableCopies(book.getAvailableCopies());
        return bookDTO;
    }

    public static BookEntity convertToBookEntity(BookDTO bookDTO) {
        BookEntity book = new BookEntity();
        book.setId(bookDTO.getId());
        book.setIsbn(bookDTO.getIsbn());
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setPublisher(bookDTO.getPublisher());
        book.setYearPublished(bookDTO.getYearPublished());
        book.setAvailableCopies(bookDTO.getAvailableCopies());
        return book;
    }

    public static LoanDTO convertToLoanDTO(LoanEntity loan) {
        LoanDTO loanDTO = new LoanDTO();
        loanDTO.setLoanId(loan.getLoanId());
        loanDTO.setBookId(loan.getBook().getId());
        loanDTO.setUserId(loan.getUser().getUserId());
        loanDTO.setLoanDate(loan.getLoanDate());
        loanDTO.setDueDate(loan.getDueDate());
        loanDTO.setReturnDate(loan.getReturnDate());
        return loanDTO;
    }

    public static LoanEntity convertToLoanEntity(LoanDTO loanDTO) {
        LoanEntity loan = new LoanEntity();
        loan.setLoanId(loanDTO.getLoanId());
        // Set book and user entities separately
        loan.setLoanDate(loanDTO.getLoanDate());
        loan.setDueDate(loanDTO.getDueDate());
        loan.setReturnDate(loanDTO.getReturnDate());
        return loan;
    }

    public static UserDTO convertToUserDTO(UserEntity user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setUsername(user.getUsername());
        userDTO.setRole(user.getRole().name()); // Convert UserRole to String
        userDTO.setEmail(user.getEmail());
        userDTO.setName(user.getName());
        // Do not set the password in the DTO
        return userDTO;
    }

    public static UserEntity convertToUserEntity(UserDTO userDTO) {
        UserEntity user = new UserEntity();
        user.setUserId(userDTO.getUserId());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setName(userDTO.getName());
        // Do not set the password in the entity directly
        return user;
    }
}

