package com.example.pfm.controller;

import com.example.pfm.dto.TransactionDTO;
import com.example.pfm.service.TransactionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

     private final TransactionService transactionService;

     public TransactionController(TransactionService transactionService) {
          this.transactionService = transactionService;
     }

     @PostMapping
     public ResponseEntity<?> createTransaction(@RequestBody TransactionDTO transactionDTO,
               Authentication authentication) {
          try {
               TransactionDTO createdTransaction = transactionService.createTransaction(authentication.getName(),
                         transactionDTO);
               return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
          } catch (IllegalArgumentException e) {
               return new ResponseEntity<>(Collections.singletonMap("message", e.getMessage()), HttpStatus.BAD_REQUEST);
          } catch (Exception e) {
               return new ResponseEntity<>(Collections.singletonMap("message", e.getMessage()),
                         HttpStatus.INTERNAL_SERVER_ERROR);
          }
     }

     @GetMapping
     public ResponseEntity<List<TransactionDTO>> getTransactions(
               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
               @RequestParam(required = false) Long categoryId,
               @RequestParam(required = false) String categoryName,
               Authentication authentication) {
          return ResponseEntity.ok(transactionService.getTransactions(authentication.getName(), startDate, endDate,
                    categoryId, categoryName));
     }

     @PutMapping("/{id}")
     public ResponseEntity<?> updateTransaction(@PathVariable Long id, @RequestBody TransactionDTO transactionDTO,
               Authentication authentication) {
          try {
               TransactionDTO updatedTransaction = transactionService.updateTransaction(authentication.getName(), id,
                         transactionDTO);
               return ResponseEntity.ok(updatedTransaction);
          } catch (RuntimeException e) {
               return new ResponseEntity<>(Collections.singletonMap("message", e.getMessage()), HttpStatus.NOT_FOUND);
          }
     }

     @DeleteMapping("/{id}")
     public ResponseEntity<?> deleteTransaction(@PathVariable Long id, Authentication authentication) {
          try {
               transactionService.deleteTransaction(authentication.getName(), id);
               return ResponseEntity.ok(Collections.singletonMap("message", "Transaction deleted successfully"));
          } catch (RuntimeException e) {
               return new ResponseEntity<>(Collections.singletonMap("message", e.getMessage()), HttpStatus.NOT_FOUND);
          }
     }
}
