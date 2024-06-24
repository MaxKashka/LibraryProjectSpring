package services;

import networklist1.LoanEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.LoanRepository;

import java.util.Optional;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    public LoanEntity saveLoan(LoanEntity loan) {
        return loanRepository.save(loan);
    }

    public Iterable<LoanEntity> findAllLoans() {
        return loanRepository.findAll();
    }

    public void deleteLoan(Long loanId) {
        loanRepository.deleteById(loanId);
    }

    public Optional<LoanEntity> updateLoan(Long loanId, LoanEntity updatedLoan) {
        return loanRepository.findById(loanId).map(loan -> {
            loan.setReturnDate(updatedLoan.getReturnDate());
            return loanRepository.save(loan);
        });
    }
}
