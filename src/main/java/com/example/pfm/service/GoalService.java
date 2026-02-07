package com.example.pfm.service;

import com.example.pfm.dto.GoalDTO;
import com.example.pfm.model.Goal;
import com.example.pfm.model.Transaction;
import com.example.pfm.model.User;
import com.example.pfm.repository.GoalRepository;
import com.example.pfm.repository.TransactionRepository;
import com.example.pfm.repository.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoalService {

     private final GoalRepository goalRepository;
     private final UserRepository userRepository;
     private final TransactionRepository transactionRepository;

     public GoalService(GoalRepository goalRepository, UserRepository userRepository,
               TransactionRepository transactionRepository) {
          this.goalRepository = goalRepository;
          this.userRepository = userRepository;
          this.transactionRepository = transactionRepository;
     }

     public GoalDTO createGoal(String username, GoalDTO goalDTO) {
          User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

          Goal goal = new Goal();
          goal.setGoalName(goalDTO.getGoalName());
          goal.setTargetAmount(goalDTO.getTargetAmount());
          goal.setTargetDate(goalDTO.getTargetDate());
          goal.setStartDate(goalDTO.getStartDate() != null ? goalDTO.getStartDate() : LocalDate.now());
          goal.setUser(user);

          Goal savedGoal = goalRepository.save(goal);
          return mapToDTO(savedGoal, user);
     }

     public List<GoalDTO> getGoals(String username) {
          User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

          return goalRepository.findByUser(user).stream()
                    .map(goal -> mapToDTO(goal, user))
                    .collect(Collectors.toList());
     }

     public GoalDTO getGoal(String username, Long id) {
          User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

          Goal goal = goalRepository.findById(id)
                    .filter(g -> g.getUser().getId().equals(user.getId()))
                    .orElseThrow(() -> new RuntimeException("Goal not found"));

          return mapToDTO(goal, user);
     }

     public GoalDTO updateGoal(String username, Long id, GoalDTO goalDTO) {
          User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

          Goal goal = goalRepository.findById(id)
                    .filter(g -> g.getUser().getId().equals(user.getId()))
                    .orElseThrow(() -> new RuntimeException("Goal not found"));

          if (goalDTO.getTargetAmount() != null) {
               goal.setTargetAmount(goalDTO.getTargetAmount());
          }
          if (goalDTO.getTargetDate() != null) {
               goal.setTargetDate(goalDTO.getTargetDate());
          }
          // Name, StartDate not explicitly mentioned as modifiable in "Update Goal"
          // section but typically are.
          // Requirement says "update target amount/date". I'll stick to that strictly?
          // Let's stick to strict requirements: "Users can view, update target
          // amount/date, and delete goals".

          Goal updatedGoal = goalRepository.save(goal);
          return mapToDTO(updatedGoal, user);
     }

     public void deleteGoal(String username, Long id) {
          User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

          Goal goal = goalRepository.findById(id)
                    .filter(g -> g.getUser().getId().equals(user.getId()))
                    .orElseThrow(() -> new RuntimeException("Goal not found"));

          goalRepository.delete(goal);
     }

     private GoalDTO mapToDTO(Goal goal, User user) {
          // Calculate progress: (Total Income - Total Expenses) since goal start date
          // NOTE: This logic assumes ALL savings go towards ALL goals or this is a
          // general "savings" metric.
          // The requirements say "Each goal tracks independently", which implies we might
          // need to allocate funds?
          // BUT the calculation formula is explicit: "(Total Income - Total Expenses)
          // since goal start date".
          // This means if I have 2 goals started on same day, they both show the same
          // "Progress".
          // This is a bit weird but valid per the requirements.

          List<Transaction> transactions = transactionRepository.findTransactions(
                    user, goal.getStartDate(), null, null, Sort.unsorted());

          BigDecimal income = transactions.stream()
                    .filter(t -> "INCOME".equals(t.getType()))
                    .map(Transaction::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

          BigDecimal expense = transactions.stream()
                    .filter(t -> "EXPENSE".equals(t.getType()))
                    .map(Transaction::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

          BigDecimal currentProgress = income.subtract(expense);

          // Requirement: "Display percentage completion and remaining amount"
          double progressPercentage = 0.0;
          if (goal.getTargetAmount().compareTo(BigDecimal.ZERO) > 0) {
               progressPercentage = currentProgress.divide(goal.getTargetAmount(), 4, RoundingMode.HALF_UP)
                         .doubleValue() * 100;
          }

          BigDecimal remainingAmount = goal.getTargetAmount().subtract(currentProgress);
          if (remainingAmount.compareTo(BigDecimal.ZERO) < 0) {
               remainingAmount = BigDecimal.ZERO;
          }

          return new GoalDTO(
                    goal.getId(),
                    goal.getGoalName(),
                    goal.getTargetAmount(),
                    goal.getTargetDate(),
                    goal.getStartDate(),
                    currentProgress,
                    progressPercentage,
                    remainingAmount);
     }
}
