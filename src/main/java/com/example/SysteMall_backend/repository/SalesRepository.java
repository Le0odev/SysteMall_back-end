package com.example.SysteMall_backend.repository;

import com.example.SysteMall_backend.entity.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SalesRepository extends JpaRepository<Sales, Long> {

    List<Sales> findBySaleDate(LocalDate saleDate);

    List<Sales> findBySaleDateBetween(LocalDateTime startDate, LocalDateTime endDate);



}
