package com.example.pfm.service;

import com.example.pfm.model.Transaction;
import com.example.pfm.model.User;
import com.example.pfm.repository.TransactionRepository;
import com.example.pfm.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {

     private final TransactionRepository transactionRepository;
     private final UserRepository userRepository;

     public ReportService(TransactionRepository transactionRepository, UserRepository userRepository) {
          this.transactionRepository = transactionRepository;
          this.userRepository = userRepository;
     }

     public Map<String, Object> getMonthlyReport(String username, int year, int month) {
          User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

          List<Transaction> transactions = transactionRepository.findByMonthAndYear(user, month, year);

          return generateReport(transactions, year, month);
     }

     public Map<String, Object> getYearlyReport(String username, int year) {
          User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

          List<Transaction> transactions = transactionRepository.findByYear(user, year);

          return generateReport(transactions, year, null);
     }

     private Map<String, Object> generateReport(List<Transaction> transactions, int year, Integer month) {
          Map<String, BigDecimal> totalIncomeByCategory = transactions.stream()
                    .filter(t -> "INCOME".equals(t.getType()))
                    .collect(Collectors.groupingBy(
                              t -> t.getCategory().getName(),
                              Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)));

          Map<String, BigDecimal> totalExpensesByCategory = transactions.stream()
                    .filter(t -> "EXPENSE".equals(t.getType()))
                    .collect(Collectors.groupingBy(
                              t -> t.getCategory().getName(),
                              Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)));

          BigDecimal totalIncome = totalIncomeByCategory.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
          BigDecimal totalExpenses = totalExpensesByCategory.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
          BigDecimal netSavings = totalIncome.subtract(totalExpenses);

          Map<String, Object> report = new HashMap<>();
          if (month != null) {
               report.put("month", month);
          }
          report.put("year", year);
          report.put("totalIncome", totalIncomeByCategory);
          report.put("totalExpenses", totalExpensesByCategory);
          report.put("netSavings", netSavings);

          return report;
     }
}
