package com.main.workload.dtos;

import com.main.workload.entities.Employee;
import com.main.workload.entities.EmployeePosition;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class EmployeeAnalyticsDTO {
    private Long id;
    private String name;
    private String typeOfEmployment;
    private List<PositionWithTotalHoursDTO> positions;
    private Integer totalWorkload;

    public EmployeeAnalyticsDTO(Employee employee) {
        setId(employee.getId());
        setName(employee.getName());
        setTypeOfEmployment(employee.getTypeOfEmployment());
        setPositions(
                employee
                        .getPositions()
                        .stream()
                        .map(PositionWithTotalHoursDTO::new)
                        .collect(Collectors.toList())
        );
        setTotalWorkload(
                employee
                        .getPositions()
                        .stream()
                        .map(EmployeePosition::getTotalWorkload)
                        .mapToInt(Integer::intValue)
                        .sum()
        );
    }

    @Data
    public static class PositionWithTotalHoursDTO {
        private Long id;
        private String post;
        private String structuralDivision;
        private Double rate;
        private Boolean active;
        private Integer totalHours;
        private Boolean overloaded;

        public PositionWithTotalHoursDTO(EmployeePosition position) {
            this.id = position.getId();
            this.post = position.getPost().getDisplayName();
            this.structuralDivision = position.getStructuralDivision().getDisplayName();
            this.rate = position.getRate();
            this.active = position.getActive();
            this.totalHours = position.getTotalWorkload();
            this.overloaded = position.isOverloaded();
        }
    }

}
