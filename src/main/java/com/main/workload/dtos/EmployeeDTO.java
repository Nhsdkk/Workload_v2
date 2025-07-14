package com.main.workload.dtos;

import com.main.workload.entities.Employee;
import lombok.Data;

@Data
public class EmployeeDTO {
    private Long id;
    private String name;
    private String typeOfEmployment;

    public EmployeeDTO(Employee employee) {
        setId(employee.getId());
        setName(employee.getName());
        setTypeOfEmployment(employee.getTypeOfEmployment());
    }
}
