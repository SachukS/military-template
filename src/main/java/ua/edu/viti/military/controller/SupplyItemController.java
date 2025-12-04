package ua.edu.viti.military.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.viti.military.dto.request.SupplyItemCreateDTO;
import ua.edu.viti.military.dto.request.SupplyItemUpdateDTO;
import ua.edu.viti.military.dto.response.SupplyItemResponseDTO;
import ua.edu.viti.military.service.SupplyItemService;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/supply-items")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Supply Items", description = "Manage supply items")
public class SupplyItemController {

    private final SupplyItemService itemService;

    @Operation(summary = "Create supply item")
    @PostMapping
    public ResponseEntity<SupplyItemResponseDTO> create(@Valid @RequestBody SupplyItemCreateDTO dto) {
        return ResponseEntity.status(201).body(itemService.create(dto));
    }

    @Operation(summary = "Get all items")
    @GetMapping
    public ResponseEntity<List<SupplyItemResponseDTO>> getAll() {
        return ResponseEntity.ok(itemService.getAll());
    }

    @Operation(summary = "Get item by id")
    @GetMapping("/{id}")
    public ResponseEntity<SupplyItemResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.getById(id));
    }

    @Operation(summary = "Update item")
    @PutMapping("/{id}")
    public ResponseEntity<SupplyItemResponseDTO> update(@PathVariable Long id,
                                                        @Valid @RequestBody SupplyItemUpdateDTO dto) {
        return ResponseEntity.ok(itemService.update(id, dto));
    }

    @Operation(summary = "Delete item")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        itemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
