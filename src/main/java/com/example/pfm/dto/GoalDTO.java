package com.example.pfm.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public class GoalDTO {

     private Long id;

     @NotBlank
     private String goalName;

     @NotNull
     @Positive
     private BigDecimal targetAmount;

     @NotNull
     @Future
     private LocalDate targetDate;

     private LocalDate startDate; // Defaults to creation date if null

     // Response fields
     private BigDecimal currentProgress;
     private double progressPercentage;
     private BigDecimal remainingAmount;

     public GoalDTO() {
     }

     public GoalDTO(Long id, String goalName, BigDecimal targetAmount, LocalDate targetDate, LocalDate startDate,
               BigDecimal currentProgress, double progressPercentage, BigDecimal remainingAmount) {
          this.id = id;
          this.goalName = goalName;
          this.targetAmount = targetAmount;
          this.targetDate = targetDate;
          this.startDate = startDate;
          this.currentProgress = currentProgress;
          this.progressPercentage = progressPercentage;
          this.remainingAmount = remainingAmount;
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

     public BigDecimal getCurrentProgress() {
          return currentProgress;
     }

     public void setCurrentProgress(BigDecimal currentProgress) {
          this.currentProgress = currentProgress;
     }

     public double getProgressPercentage() {
          return progressPercentage;
     }

     public void setProgressPercentage(double progressPercentage) {
          this.progressPercentage = progressPercentage;
     }

     public BigDecimal getRemainingAmount() {
          return remainingAmount;
     }

     public void setRemainingAmount(BigDecimal remainingAmount) {
          this.remainingAmount = remainingAmount;
     }
}
