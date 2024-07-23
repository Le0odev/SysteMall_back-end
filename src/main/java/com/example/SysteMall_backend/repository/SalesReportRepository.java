package com.example.SysteMall_backend.repository;

import com.example.SysteMall_backend.entity.Sales;
import com.example.SysteMall_backend.entity.SalesReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SalesReportRepository extends JpaRepository<SalesReport, Long>{

    List<Sales>findByDate(LocalDate date);



}
