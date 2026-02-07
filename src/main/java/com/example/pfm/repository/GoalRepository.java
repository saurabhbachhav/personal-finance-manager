package com.example.pfm.repository;

import com.example.pfm.model.Goal;
import com.example.pfm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {
     List<Goal> findByUser(User user);
}
