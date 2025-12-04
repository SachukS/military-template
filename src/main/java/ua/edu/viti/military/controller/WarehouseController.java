package ua.edu.viti.military.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.viti.military.dto.request.WarehouseCreateDTO;
import ua.edu.viti.military.dto.response.WarehouseResponseDTO;
import ua.edu.viti.military.entity.Warehouse;
import ua.edu.viti.military.repository.WarehouseRepository;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/warehouses")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Warehouses", description = "Manage warehouses")
public class WarehouseController {

    private final WarehouseRepository warehouseRepository;

    @Operation(summary = "Create warehouse")
    @PostMapping
    public ResponseEntity<WarehouseResponseDTO> create(@Valid @RequestBody WarehouseCreateDTO dto) {
        Warehouse w = new Warehouse();
        w.setName(dto.getName());
        w.setCode(dto.getCode());
        w.setAddress(dto.getAddress());
        w.setCapacity(dto.getCapacity());
        w.setCurrentOccupancy(dto.getCurrentOccupancy());
        w.setHasRefrigeration(dto.getHasRefrigeration());

        Warehouse saved = warehouseRepository.save(w);
        WarehouseResponseDTO resp = new WarehouseResponseDTO();
        resp.setId(saved.getId());
        resp.setName(saved.getName());
        resp.setCode(saved.getCode());
        resp.setAddress(saved.getAddress());
        resp.setCapacity(saved.getCapacity());
        resp.setCurrentOccupancy(saved.getCurrentOccupancy());
        resp.setHasRefrigeration(saved.getHasRefrigeration());
        resp.setCreatedAt(saved.getCreatedAt());
        resp.setUpdatedAt(saved.getUpdatedAt());

        return ResponseEntity.status(201).body(resp);
    }

    @Operation(summary = "Get all warehouses")
    @GetMapping
    public ResponseEntity<List<WarehouseResponseDTO>> getAll() {
        List<WarehouseResponseDTO> list = warehouseRepository.findAll().stream().map(w -> {
            WarehouseResponseDTO dto = new WarehouseResponseDTO();
            dto.setId(w.getId()); dto.setName(w.getName()); dto.setCode(w.getCode());
            dto.setAddress(w.getAddress()); dto.setCapacity(w.getCapacity());
            dto.setCurrentOccupancy(w.getCurrentOccupancy()); dto.setHasRefrigeration(w.getHasRefrigeration());
            dto.setCreatedAt(w.getCreatedAt()); dto.setUpdatedAt(w.getUpdatedAt());
            return dto;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }
}
