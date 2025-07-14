package com.main.workload.controllers;

import com.main.workload.dtos.EmployeeAnalyticsDTO;
import com.main.workload.dtos.EmployeePositionAnalyticsDTO;
import com.main.workload.services.AnalyticsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@Tag(name = "Аналитика", description = "API для получения аналитики о распределении")
public class AnalyticsController {
    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/employee")
    public List<EmployeeAnalyticsDTO> getEmployeeAnalytics() {
        return analyticsService.getEmployeeAnalytics();
    }

    @GetMapping("/positions")
    public List<EmployeePositionAnalyticsDTO>  getEmployeePositionAnalytics() {
        return analyticsService.getEmployeePositionAnalytics();
    }
}
