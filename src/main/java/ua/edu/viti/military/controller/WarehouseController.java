package ua.edu.viti.military.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.edu.viti.military.dto.request.WarehouseCreateDTO;
import ua.edu.viti.military.dto.response.WarehouseResponseDTO;
import ua.edu.viti.military.service.WarehouseService;

import java.util.List;

/**
 * REST Controller для управління складами
 */
@RestController
@RequestMapping("/api/warehouses")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Warehouses", description = "API для управління складами МТЗ")
public class WarehouseController {

    private final WarehouseService warehouseService;

    @PostMapping
    @Operation(
            summary = "Створити склад",
            description = "Створює новий склад. Код складу має бути унікальним."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Склад успішно створений"),
            @ApiResponse(responseCode = "400", description = "Помилка валідації вхідних даних"),
            @ApiResponse(responseCode = "409", description = "Склад з таким кодом вже існує")
    })
    public ResponseEntity<WarehouseResponseDTO> create(
            @Valid @RequestBody WarehouseCreateDTO dto) {

        log.info("REST request to create warehouse: {}", dto);
        WarehouseResponseDTO created = warehouseService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Отримати склад за ID",
            description = "Повертає детальну інформацію про склад за його ідентифікатором"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Склад знайдено"),
            @ApiResponse(responseCode = "404", description = "Склад не знайдено")
    })
    public ResponseEntity<WarehouseResponseDTO> getById(
            @Parameter(description = "ID складу", required = true)
            @PathVariable Long id) {

        log.info("REST request to get warehouse by ID: {}", id);
        return ResponseEntity.ok(warehouseService.getById(id));
    }

    @GetMapping
    @Operation(
            summary = "Отримати всі склади",
            description = "Повертає список всіх складів"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список успішно отримано")
    })
    public ResponseEntity<List<WarehouseResponseDTO>> getAll() {
        log.info("REST request to get all warehouses");
        return ResponseEntity.ok(warehouseService.getAll());
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Оновити склад",
            description = "Оновлює інформацію про існуючий склад"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Склад успішно оновлено"),
            @ApiResponse(responseCode = "400", description = "Помилка валідації"),
            @ApiResponse(responseCode = "404", description = "Склад не знайдено"),
            @ApiResponse(responseCode = "409", description = "Склад з таким кодом вже існує")
    })
    public ResponseEntity<WarehouseResponseDTO> update(
            @Parameter(description = "ID складу", required = true)
            @PathVariable Long id,
            @Valid @RequestBody WarehouseCreateDTO dto) {

        log.info("REST request to update warehouse {}: {}", id, dto);
        return ResponseEntity.ok(warehouseService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Видалити склад",
            description = "Видаляє склад з системи"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Склад успішно видалено"),
            @ApiResponse(responseCode = "404", description = "Склад не знайдено")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID складу", required = true)
            @PathVariable Long id) {

        log.info("REST request to delete warehouse: {}", id);
        warehouseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
