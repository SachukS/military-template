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
import ua.edu.viti.military.dto.request.SupplyCategoryCreateDTO;
import ua.edu.viti.military.dto.response.SupplyCategoryResponseDTO;
import ua.edu.viti.military.service.SupplyCategoryService;

import java.util.List;

/**
 * REST Controller для управління категоріями матеріалів
 */
@RestController
@RequestMapping("/api/supply-categories")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Supply Categories", description = "API для управління категоріями матеріалів МТЗ")
public class SupplyCategoryController {

    private final SupplyCategoryService categoryService;

    @PostMapping
    @Operation(
            summary = "Створити категорію",
            description = "Створює нову категорію матеріалів. Код та назва мають бути унікальними."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Категорія успішно створена"),
            @ApiResponse(responseCode = "400", description = "Помилка валідації вхідних даних"),
            @ApiResponse(responseCode = "409", description = "Категорія з таким кодом або назвою вже існує")
    })
    public ResponseEntity<SupplyCategoryResponseDTO> create(
            @Valid @RequestBody SupplyCategoryCreateDTO dto) {

        log.info("REST request to create supply category: {}", dto);
        SupplyCategoryResponseDTO created = categoryService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Отримати категорію за ID",
            description = "Повертає детальну інформацію про категорію за її ідентифікатором"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Категорія знайдена"),
            @ApiResponse(responseCode = "404", description = "Категорію не знайдено")
    })
    public ResponseEntity<SupplyCategoryResponseDTO> getById(
            @Parameter(description = "ID категорії", required = true)
            @PathVariable Long id) {

        log.info("REST request to get supply category by ID: {}", id);
        return ResponseEntity.ok(categoryService.getById(id));
    }

    @GetMapping
    @Operation(
            summary = "Отримати всі категорії",
            description = "Повертає список всіх категорій матеріалів"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список успішно отримано")
    })
    public ResponseEntity<List<SupplyCategoryResponseDTO>> getAll() {
        log.info("REST request to get all supply categories");
        return ResponseEntity.ok(categoryService.getAll());
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Оновити категорію",
            description = "Оновлює інформацію про існуючу категорію"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Категорія успішно оновлена"),
            @ApiResponse(responseCode = "400", description = "Помилка валідації"),
            @ApiResponse(responseCode = "404", description = "Категорію не знайдено"),
            @ApiResponse(responseCode = "409", description = "Категорія з таким кодом або назвою вже існує")
    })
    public ResponseEntity<SupplyCategoryResponseDTO> update(
            @Parameter(description = "ID категорії", required = true)
            @PathVariable Long id,
            @Valid @RequestBody SupplyCategoryCreateDTO dto) {

        log.info("REST request to update supply category {}: {}", id, dto);
        return ResponseEntity.ok(categoryService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Видалити категорію",
            description = "Видаляє категорію з системи"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Категорія успішно видалена"),
            @ApiResponse(responseCode = "404", description = "Категорію не знайдено")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID категорії", required = true)
            @PathVariable Long id) {

        log.info("REST request to delete supply category: {}", id);
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
