package com.example.pfm.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
public class Transaction {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

     @NotNull
     @Positive
     @Column(nullable = false)
     private BigDecimal amount;

     @NotNull
     @Column(nullable = false)
     private LocalDate date;

     private String description;

     @NotNull
     @Column(nullable = false)
     private String type; // INCOME or EXPENSE (Derived from Category usually, but can be stored for
                          // convenience/performance)

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "user_id", nullable = false)
     private User user;

     @ManyToOne(fetch = FetchType.EAGER)
     @JoinColumn(name = "category_id", nullable = false)
     private Category category;

     public Transaction() {
     }

     public Transaction(BigDecimal amount, LocalDate date, String description, String type, User user,
               Category category) {
          this.amount = amount;
          this.date = date;
          this.description = description;
          this.type = type;
          this.user = user;
          this.category = category;
     }

     public Long getId() {
          return id;
     }

     public void setId(Long id) {
          this.id = id;
     }

     public BigDecimal getAmount() {
          return amount;
     }

     public void setAmount(BigDecimal amount) {
          this.amount = amount;
     }

     public LocalDate getDate() {
          return date;
     }

     public void setDate(LocalDate date) {
          this.date = date;
     }

     public String getDescription() {
          return description;
     }

     public void setDescription(String description) {
          this.description = description;
     }

     public String getType() {
          return type;
     }

     public void setType(String type) {
          this.type = type;
     }

     public User getUser() {
          return user;
     }

     public void setUser(User user) {
          this.user = user;
     }

     public Category getCategory() {
          return category;
     }

     public void setCategory(Category category) {
          this.category = category;
          this.type = category.getType(); // Sync type with category
     }
}
