package com.main.workload.services;

import com.main.workload.dtos.EmployeeAnalyticsDTO;
import com.main.workload.repositories.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {
    private final EmployeeRepository employeeRepository;

    public AnalyticsService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<EmployeeAnalyticsDTO> getEmployeeAnalytics() {
        var employees = employeeRepository.findAll();
        return employees
                .stream()
                .map(EmployeeAnalyticsDTO::new)
                .collect(Collectors.toList());
    }
}
