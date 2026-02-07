package com.example.pfm.service;

import com.example.pfm.dto.TransactionDTO;
import com.example.pfm.model.Category;
import com.example.pfm.model.Transaction;
import com.example.pfm.model.User;
import com.example.pfm.repository.CategoryRepository;
import com.example.pfm.repository.TransactionRepository;
import com.example.pfm.repository.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

     private final TransactionRepository transactionRepository;
     private final UserRepository userRepository;
     private final CategoryRepository categoryRepository;

     public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository,
               CategoryRepository categoryRepository) {
          this.transactionRepository = transactionRepository;
          this.userRepository = userRepository;
          this.categoryRepository = categoryRepository;
     }

     public TransactionDTO createTransaction(String username, TransactionDTO transactionDTO) {
          User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

          if (transactionDTO.getDate().isAfter(LocalDate.now().plusDays(1))) { // Allow today? Assignment says "cannot
                                                                               // be a future date"
               if (transactionDTO.getDate().isAfter(LocalDate.now())) {
                    throw new IllegalArgumentException("Date cannot be in the future");
               }
          }

          Category category = categoryRepository.findByNameAndUser(transactionDTO.getCategory(), user)
                    .or(() -> categoryRepository.findByNameAndUserIsNull(transactionDTO.getCategory()))
                    .orElseThrow(() -> new IllegalArgumentException("Category not found"));

          Transaction transaction = new Transaction();
          transaction.setAmount(transactionDTO.getAmount());
          transaction.setDate(transactionDTO.getDate());
          transaction.setDescription(transactionDTO.getDescription());
          transaction.setUser(user);
          transaction.setCategory(category);
          transaction.setType(category.getType());

          Transaction savedTransaction = transactionRepository.save(transaction);
          return mapToDTO(savedTransaction);
     }

     public List<TransactionDTO> getTransactions(String username, LocalDate startDate, LocalDate endDate,
               Long categoryId) {
          User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

          List<Transaction> transactions = transactionRepository.findTransactions(user, startDate, endDate, categoryId,
                    Sort.by(Sort.Direction.DESC, "date"));

          return transactions.stream().map(this::mapToDTO).collect(Collectors.toList());
     }

     public TransactionDTO updateTransaction(String username, Long id, TransactionDTO transactionDTO) {
          User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

          Transaction transaction = transactionRepository.findById(id)
                    .filter(t -> t.getUser().getId().equals(user.getId()))
                    .orElseThrow(() -> new RuntimeException("Transaction not found"));

          // Date cannot be modified (Requirement)

          if (transactionDTO.getAmount() != null) {
               transaction.setAmount(transactionDTO.getAmount());
          }
          if (transactionDTO.getDescription() != null) {
               transaction.setDescription(transactionDTO.getDescription());
          }
          if (transactionDTO.getCategory() != null) {
               Category category = categoryRepository.findByNameAndUser(transactionDTO.getCategory(), user)
                         .or(() -> categoryRepository.findByNameAndUserIsNull(transactionDTO.getCategory()))
                         .orElseThrow(() -> new IllegalArgumentException("Category not found"));
               transaction.setCategory(category);
               transaction.setType(category.getType());
          }

          Transaction updatedTransaction = transactionRepository.save(transaction);
          return mapToDTO(updatedTransaction);
     }

     public void deleteTransaction(String username, Long id) {
          User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

          Transaction transaction = transactionRepository.findById(id)
                    .filter(t -> t.getUser().getId().equals(user.getId()))
                    .orElseThrow(() -> new RuntimeException("Transaction not found"));

          transactionRepository.delete(transaction);
     }

     private TransactionDTO mapToDTO(Transaction transaction) {
          return new TransactionDTO(
                    transaction.getId(),
                    transaction.getAmount(),
                    transaction.getDate(),
                    transaction.getCategory().getName(),
                    transaction.getDescription(),
                    transaction.getType());
     }
}
