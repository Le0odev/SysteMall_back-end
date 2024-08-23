package com.example.SysteMall_backend.controller;

import com.example.SysteMall_backend.entity.Log;
import com.example.SysteMall_backend.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/produto/log")
@CrossOrigin(origins = "http://localhost:5173")
public class LogController {

    @Autowired
    private LogService logService;

    @GetMapping
    public List<Log> getLogs() {
        return logService.getAllLogs();
    }
}
