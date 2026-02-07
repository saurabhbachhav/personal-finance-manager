package com.example.pfm.repository;

import com.example.pfm.model.Transaction;
import com.example.pfm.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

     List<Transaction> findByUser(User user, Sort sort);

     @Query("SELECT t FROM Transaction t WHERE t.user = :user " +
               "AND (:startDate IS NULL OR t.date >= :startDate) " +
               "AND (:endDate IS NULL OR t.date <= :endDate) " +
               "AND (:categoryId IS NULL OR t.category.id = :categoryId) " +
               "AND (:categoryName IS NULL OR t.category.name = :categoryName)")
     List<Transaction> findTransactions(
               @Param("user") User user,
               @Param("startDate") LocalDate startDate,
               @Param("endDate") LocalDate endDate,
               @Param("categoryId") Long categoryId,
               @Param("categoryName") String categoryName,
               Sort sort);

     // For reports
     @Query("SELECT t FROM Transaction t WHERE t.user = :user AND YEAR(t.date) = :year")
     List<Transaction> findByYear(@Param("user") User user, @Param("year") int year);

     @Query("SELECT t FROM Transaction t WHERE t.user = :user AND YEAR(t.date) = :year AND MONTH(t.date) = :month")
     List<Transaction> findByMonthAndYear(@Param("user") User user, @Param("month") int month, @Param("year") int year);
}
