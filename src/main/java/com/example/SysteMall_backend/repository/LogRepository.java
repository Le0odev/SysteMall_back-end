package com.example.SysteMall_backend.repository;

import com.example.SysteMall_backend.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LogRepository extends JpaRepository<Log, Long> {
    List<Log> findAllByOrderByDataHoraDesc();
}
