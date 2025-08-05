package com.example.paul.repositories;

import com.example.paul.models.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findBySortCodeAndAccountNumber(String sortCode, String accountNumber);
    Optional<Account> findByAccountNumber(String accountNumber);
    
    List<Account> findByOwnerName(String ownerName);
    
    List<Account> findByBankName(String bankName);
    
    List<Account> findBySortCode(String sortCode);
    
    List<Account> findByCurrentBalanceGreaterThan(double balance);
    
    List<Account> findByCurrentBalanceLessThan(double balance);
    
    List<Account> findByCurrentBalanceBetween(double minBalance, double maxBalance);
    
    @Query("SELECT a FROM Account a WHERE a.currentBalance = 0")
    List<Account> findZeroBalanceAccounts();
    
    @Query("SELECT a FROM Account a WHERE a.currentBalance < 0")
    List<Account> findOverdrawnAccounts();
    
    @Query("SELECT a FROM Account a WHERE a.currentBalance > :threshold")
    List<Account> findHighValueAccounts(@Param("threshold") double threshold);
    
    @Query("SELECT a.bankName, COUNT(a) FROM Account a GROUP BY a.bankName")
    List<Object[]> getAccountCountByBank();
    
    @Query("SELECT AVG(a.currentBalance) FROM Account a WHERE a.bankName = :bankName")
    Double getAverageBalanceByBank(@Param("bankName") String bankName);
    
    @Query("SELECT SUM(a.currentBalance) FROM Account a WHERE a.ownerName = :ownerName")
    Double getTotalBalanceByOwner(@Param("ownerName") String ownerName);
    
    Page<Account> findByBankName(String bankName, Pageable pageable);
    
    Page<Account> findByCurrentBalanceGreaterThan(double balance, Pageable pageable);
    
    boolean existsBySortCodeAndAccountNumber(String sortCode, String accountNumber);
    
    boolean existsByAccountNumber(String accountNumber);
    
    long countByBankName(String bankName);
    
    long countByCurrentBalanceGreaterThan(double balance);
    
    List<Account> findTop10ByOrderByCurrentBalanceDesc();
    
    @Query(value = "SELECT * FROM online_bank.account WHERE current_balance > (SELECT AVG(current_balance) FROM online_bank.account)", nativeQuery = true)
    List<Account> findAboveAverageAccounts();
}
