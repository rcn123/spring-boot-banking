package com.example.paul.repositories;

import com.example.paul.models.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    Optional<Customer> findByEmail(String email);
    
    Optional<Customer> findByPhoneNumber(String phoneNumber);
    
    List<Customer> findByLastName(String lastName);
    
    List<Customer> findByFirstNameAndLastName(String firstName, String lastName);
    
    List<Customer> findByActiveTrue();
    
    List<Customer> findByActiveFalse();
    
    List<Customer> findByCustomerType(String customerType);
    
    List<Customer> findByCity(String city);
    
    List<Customer> findByCountry(String country);
    
    List<Customer> findByPostalCode(String postalCode);
    
    List<Customer> findByDateOfBirthBetween(LocalDate startDate, LocalDate endDate);
    
    List<Customer> findByCreatedAtAfter(LocalDateTime date);
    
    List<Customer> findByCreatedAtBefore(LocalDateTime date);
    
    List<Customer> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    Page<Customer> findByActiveTrue(Pageable pageable);
    
    Page<Customer> findByCustomerType(String customerType, Pageable pageable);
    
    @Query("SELECT c FROM Customer c WHERE c.email LIKE %:domain")
    List<Customer> findByEmailDomain(@Param("domain") String domain);
    
    @Query("SELECT c FROM Customer c WHERE LOWER(c.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Customer> searchByName(@Param("name") String name);
    
    @Query("SELECT c FROM Customer c WHERE SIZE(c.accounts) > :count")
    List<Customer> findCustomersWithMultipleAccounts(@Param("count") int count);
    
    @Query("SELECT c FROM Customer c WHERE c.createdAt >= :date ORDER BY c.createdAt DESC")
    List<Customer> findRecentCustomers(@Param("date") LocalDateTime date);
    
    @Query("SELECT COUNT(c) FROM Customer c WHERE c.customerType = :type")
    Long countByCustomerType(@Param("type") String type);
    
    @Query("SELECT c FROM Customer c WHERE c.city = :city AND c.active = true")
    List<Customer> findActiveCustomersByCity(@Param("city") String city);
    
    @Query("SELECT DISTINCT c.city FROM Customer c WHERE c.country = :country")
    List<String> findDistinctCitiesByCountry(@Param("country") String country);
    
    @Query("SELECT c FROM Customer c WHERE c.lastModified < :date")
    List<Customer> findInactiveCustomers(@Param("date") LocalDateTime date);
    
    @Query("SELECT c FROM Customer c WHERE YEAR(c.dateOfBirth) = :year")
    List<Customer> findByBirthYear(@Param("year") int year);
    
    @Query("SELECT c.customerType, COUNT(c) FROM Customer c GROUP BY c.customerType")
    List<Object[]> getCustomerTypeStatistics();
    
    boolean existsByEmail(String email);
    
    boolean existsByPhoneNumber(String phoneNumber);
    
    long countByActiveTrue();
    
    long countByActiveFalse();
    
    void deleteByEmail(String email);
    
    List<Customer> findTop10ByOrderByCreatedAtDesc();
    
    List<Customer> findDistinctByCountryIn(List<String> countries);
    
    @Query(value = "SELECT * FROM online_bank.customer WHERE MONTH(date_of_birth) = :month", nativeQuery = true)
    List<Customer> findByBirthMonth(@Param("month") int month);
    
    @Query(value = "SELECT * FROM online_bank.customer WHERE DATEDIFF(CURRENT_DATE, created_at) <= :days", nativeQuery = true)
    List<Customer> findNewCustomers(@Param("days") int days);
}