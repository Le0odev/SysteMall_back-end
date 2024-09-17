package com.example.SysteMall_backend.repository;


import com.example.SysteMall_backend.entity.Feedback;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {


}
