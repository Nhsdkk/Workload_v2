package com.main.workload.services;

import com.main.workload.dtos.EmployeeAnalyticsDTO;
import com.main.workload.dtos.EmployeePositionAnalyticsDTO;
import com.main.workload.repositories.EmployeePositionRepository;
import com.main.workload.repositories.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {
    private final EmployeeRepository employeeRepository;
    private final EmployeePositionRepository employeePositionRepository;

    public AnalyticsService(
            EmployeeRepository employeeRepository,
            EmployeePositionRepository employeePositionRepository
    ) {
        this.employeeRepository = employeeRepository;
        this.employeePositionRepository = employeePositionRepository;
    }

    public List<EmployeeAnalyticsDTO> getEmployeeAnalytics() {
        var employees = employeeRepository.findAll();
        return employees
                .stream()
                .map(EmployeeAnalyticsDTO::new)
                .collect(Collectors.toList());
    }

    public List<EmployeePositionAnalyticsDTO> getEmployeePositionAnalytics() {
        var employeePositions = employeePositionRepository.findAll();
        return employeePositions
                .stream()
                .map(EmployeePositionAnalyticsDTO::new)
                .collect(Collectors.toList());
    }
}
