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
import ua.edu.viti.military.dto.request.SupplyCategoryCreateDTO;
import ua.edu.viti.military.dto.response.SupplyCategoryResponseDTO;
import ua.edu.viti.military.service.SupplyCategoryService;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/supply-categories")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Supply Categories", description = "Manage supply categories")
public class SupplyCategoryController {

    private final SupplyCategoryService categoryService;

    @Operation(summary = "Create supply category")
    @PostMapping
    public ResponseEntity<SupplyCategoryResponseDTO> create(@Valid @RequestBody SupplyCategoryCreateDTO dto) {
        return ResponseEntity.status(201).body(categoryService.create(dto));
    }

    @Operation(summary = "Get all categories")
    @GetMapping
    public ResponseEntity<List<SupplyCategoryResponseDTO>> getAll() {
        return ResponseEntity.ok(categoryService.getAll());
    }

    @Operation(summary = "Get category by id")
    @GetMapping("/{id}")
    public ResponseEntity<SupplyCategoryResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getById(id));
    }
}
