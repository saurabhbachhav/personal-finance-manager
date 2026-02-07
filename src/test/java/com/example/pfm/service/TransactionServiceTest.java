package com.example.pfm.service;

import com.example.pfm.dto.TransactionDTO;
import com.example.pfm.model.Category;
import com.example.pfm.model.Transaction;
import com.example.pfm.model.User;
import com.example.pfm.repository.CategoryRepository;
import com.example.pfm.repository.TransactionRepository;
import com.example.pfm.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

     @Mock
     private TransactionRepository transactionRepository;

     @Mock
     private UserRepository userRepository;

     @Mock
     private CategoryRepository categoryRepository;

     @InjectMocks
     private TransactionService transactionService;

     private User user;
     private Category category;
     private TransactionDTO transactionDTO;

     @BeforeEach
     void setUp() {
          user = new User();
          user.setId(1L);
          user.setUsername("test@example.com");

          category = new Category();
          category.setId(1L);
          category.setName("Salary");
          category.setType("INCOME");

          transactionDTO = new TransactionDTO();
          transactionDTO.setAmount(BigDecimal.valueOf(1000));
          transactionDTO.setDate(LocalDate.now());
          transactionDTO.setCategory("Salary");
          transactionDTO.setDescription("Test Income");
     }

     @Test
     void createTransaction_Success() {
          when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
          when(categoryRepository.findByNameAndUser(category.getName(), user)).thenReturn(Optional.empty());
          when(categoryRepository.findByNameAndUserIsNull(category.getName())).thenReturn(Optional.of(category));

          Transaction savedTransaction = new Transaction();
          savedTransaction.setId(1L);
          savedTransaction.setAmount(transactionDTO.getAmount());
          savedTransaction.setDate(transactionDTO.getDate());
          savedTransaction.setCategory(category);
          savedTransaction.setUser(user);
          savedTransaction.setType(category.getType());

          when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);

          TransactionDTO result = transactionService.createTransaction(user.getUsername(), transactionDTO);

          assertNotNull(result);
          assertEquals(BigDecimal.valueOf(1000), result.getAmount());
          assertEquals("INCOME", result.getType());
     }

     @Test
     void getTransactions_Success() {
          when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

          Transaction transaction = new Transaction();
          transaction.setId(1L);
          transaction.setAmount(BigDecimal.valueOf(1000));
          transaction.setDate(LocalDate.now());
          transaction.setCategory(category);
          transaction.setUser(user);
          transaction.setType("INCOME");

          when(transactionRepository.findTransactions(any(), any(), any(), any(), any(Sort.class)))
                    .thenReturn(Collections.singletonList(transaction));

          List<TransactionDTO> results = transactionService.getTransactions(user.getUsername(), null, null, null);

          assertFalse(results.isEmpty());
          assertEquals(1, results.size());
     }
}
