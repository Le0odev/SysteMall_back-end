package com.example.SysteMall_backend.service;


import com.example.SysteMall_backend.entity.Log;
import com.example.SysteMall_backend.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    public List<Log> getAllLogs() {
        return logRepository.findAllByOrderByDataHoraDesc();
    }

    public void registrarLog(String descricao) {
        Log log = new Log(descricao);
        logRepository.save(log);
    }
}

