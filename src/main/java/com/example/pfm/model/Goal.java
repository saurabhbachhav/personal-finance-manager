package com.example.pfm.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "goals")
public class Goal {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

     @NotBlank
     @Column(nullable = false)
     private String goalName;

     @NotNull
     @Positive
     @Column(nullable = false)
     private BigDecimal targetAmount;

     @NotNull
     @Future
     @Column(nullable = false)
     private LocalDate targetDate;

     @NotNull
     @Column(nullable = false)
     private LocalDate startDate;

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "user_id", nullable = false)
     private User user;

     public Goal() {
     }

     public Goal(String goalName, BigDecimal targetAmount, LocalDate targetDate, LocalDate startDate, User user) {
          this.goalName = goalName;
          this.targetAmount = targetAmount;
          this.targetDate = targetDate;
          this.startDate = startDate;
          this.user = user;
     }

     public Long getId() {
          return id;
     }

     public void setId(Long id) {
          this.id = id;
     }

     public String getGoalName() {
          return goalName;
     }

     public void setGoalName(String goalName) {
          this.goalName = goalName;
     }

     public BigDecimal getTargetAmount() {
          return targetAmount;
     }

     public void setTargetAmount(BigDecimal targetAmount) {
          this.targetAmount = targetAmount;
     }

     public LocalDate getTargetDate() {
          return targetDate;
     }

     public void setTargetDate(LocalDate targetDate) {
          this.targetDate = targetDate;
     }

     public LocalDate getStartDate() {
          return startDate;
     }

     public void setStartDate(LocalDate startDate) {
          this.startDate = startDate;
     }

     public User getUser() {
          return user;
     }

     public void setUser(User user) {
          this.user = user;
     }
}
