package com.main.workload.controllers;

import com.main.workload.dtos.CreateWorkloadDTO;
import com.main.workload.dtos.SwapWorkloadDTO;
import com.main.workload.dtos.UpdateWorkloadDTO;
import com.main.workload.dtos.WorkloadExportDTO;
import com.main.workload.services.ExportService;
import com.main.workload.services.WorkloadService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/workload")
@Tag(name = "Нагрузка", description = "API для работы с нагрузкой")
public class WorkloadController {
    private final ExportService exportService;
    private final WorkloadService workloadService;

    public WorkloadController(
            ExportService exportService,
            WorkloadService workloadService
    ) {
        this.exportService = exportService;
        this.workloadService = workloadService;
    }

    @GetMapping("/xlsx")
    public ResponseEntity<ByteArrayResource> exportWorkloadToXlsx() {
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

    @GetMapping
    public List<WorkloadExportDTO> getWorkload() {
        return exportService.getWorkloads();
    }

    @PostMapping
    public List<WorkloadExportDTO> createWorkload(@RequestBody CreateWorkloadDTO createWorkloadDTO) {
        return workloadService.createWorkload(createWorkloadDTO);
    }

    @PutMapping("/swap")
    public void swapWorkload(@RequestBody SwapWorkloadDTO swapWorkloadDTO) {
        var workloadTypes = exportService.getWorkloads().stream().filter(x -> swapWorkloadDTO.getOldContainerIds() == x.getId()).findFirst().orElseThrow().getWorkloadTypes();
        workloadService.swapWorkload(swapWorkloadDTO, workloadTypes);
    }

    @DeleteMapping
    public void deleteWorkload(List<Long> id) {
        workloadService.deleteWorkload(id);
    }

    @PatchMapping
    public WorkloadExportDTO updateWorkload(@RequestBody UpdateWorkloadDTO updateWorkloadDTO) {
        return workloadService.updateWorkload(updateWorkloadDTO);
    }
}
