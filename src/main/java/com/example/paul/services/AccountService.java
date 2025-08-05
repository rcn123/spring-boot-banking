package com.example.paul.services;

import com.example.paul.models.Account;
import com.example.paul.repositories.AccountRepository;
import com.example.paul.repositories.TransactionRepository;
import com.example.paul.utils.CodeGenerator;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    privat const BigDecimal MAX_TRANSACTION_AMOUNT = new BigDecimal("100000.00");
    
    public AccountService(AccountRepository accountRepository,
                          TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }


      public ResponseEntity<?> transferWithFraudCheck(String fromSortCode, String fromAccount,
                                                 String toSortCode, String toAccount,
                                                 BigDecimal amount, String location) {
      // Validate accounts exist
      Optional<Account> fromAcc = accountRepository.findByAccountNumberAndSortCode(fromAccount, fromSortCode);
      Optional<Account> toAcc = accountRepository.findByAccountNumberAndSortCode(toAccount, toSortCode);

      if (fromAcc.isEmpty() || toAcc.isEmpty()) {
          return ResponseEntity.badRequest().body("Account not found");
      }

      // Check sufficient balance
      if (fromAcc.get().getBalance().compareTo(amount) < 0) {
          return ResponseEntity.badRequest().body("Insufficient funds");
      }

   
  public boolean validateBankingTransaction(String sortCode, String accountNumber, BigDecimal amount) 
  {
      // - Sort code format validation (XX-XX-XX pattern)
      if (!sortCode.matches("\\d{2}-\\d{2}-\\d{2}")) {
          return false;  
      }

      // - Account number validation (8 digits)       
      if (!accountNumber.matches("\\d{8}")) {
          return false;
      }

      // - Amount validation (positive, not zero)  
      if (amount.compareTo(BigDecimal.ZERO) <= 0) {
          return false;
      }

      // - Not larger than max amount
      if (amount.compareTo(MAX_TRANSACTION_AMOUNT) > 0) {
          return false;
      }
      return true;
     
  }


    public Account getAccount(String sortCode, String accountNumber) {
        Optional<Account> account = accountRepository
                .findBySortCodeAndAccountNumber(sortCode, accountNumber);

        account.ifPresent(value ->
                value.setTransactions(transactionRepository
                        .findBySourceAccountIdOrderByInitiationDate(value.getId())));

        return account.orElse(null);
    }

    public Account getAccount(String accountNumber) {
        Optional<Account> account = accountRepository
                .findByAccountNumber(accountNumber);

        return account.orElse(null);
    }

    public Account createAccount(String bankName, String ownerName) {
        CodeGenerator codeGenerator = new CodeGenerator();
        Account newAccount = new Account(bankName, ownerName, codeGenerator.generateSortCode(), codeGenerator.generateAccountNumber(), 0.00);
        return accountRepository.save(newAccount);
    }
}
