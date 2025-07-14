package com.main.workload.controllers;

import com.main.workload.dtos.WorkloadExportDTO;
import com.main.workload.services.ExportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/export")
@Tag(name = "Экспорт", description = "API для экспортирования данных")
public class ExportController {

    private final ExportService exportService;

    @Autowired
    public ExportController(ExportService exportService) {

        this.exportService = exportService;
    }

    @GetMapping("/workload/xlsx")
    public ResponseEntity<ByteArrayResource> checkCompetences() {
        try {
            byte[] excelBytes = exportService.export();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=workload_export.xlsx")
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(new ByteArrayResource(excelBytes));
        } catch (IOException ex) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/workload/")
    public List<WorkloadExportDTO> getWorkloadExport() {
        return exportService.getWorkloads();
    }
}


