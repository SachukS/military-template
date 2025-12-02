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
import ua.edu.viti.military.dto.request.SupplyItemCreateDTO;
import ua.edu.viti.military.dto.request.SupplyItemUpdateDTO;
import ua.edu.viti.military.dto.response.SupplyItemResponseDTO;
import ua.edu.viti.military.entity.ItemStatus;
import ua.edu.viti.military.service.SupplyItemService;

import java.util.List;

/**
 * REST Controller для управління матеріалами МТЗ
 */
@RestController
@RequestMapping("/api/supply-items")
@RequiredArgsConstructor
@Slf4j
@Tag(
        name = "Supply Items",
        description = "API для управління матеріалами МТЗ. Підтримує CRUD операції, фільтрацію та контроль термінів придатності."
)
public class SupplyItemController {

    private final SupplyItemService itemService;

    @PostMapping
    @Operation(
            summary = "Створити новий матеріал",
            description = """
                    Створює новий матеріал в системі МТЗ.
                    
                    **Бізнес-правила:**
                    - Номер партії має бути унікальним
                    - Категорія має існувати в системі
                    - Кількість має бути більше 0
                    - Термін придатності має бути в майбутньому (якщо вказано)
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Матеріал успішно створено"),
            @ApiResponse(responseCode = "400", description = "Помилка валідації вхідних даних"),
            @ApiResponse(responseCode = "404", description = "Категорію або склад не знайдено"),
            @ApiResponse(responseCode = "409", description = "Матеріал з таким номером партії вже існує")
    })
    public ResponseEntity<SupplyItemResponseDTO> create(
            @Valid @RequestBody SupplyItemCreateDTO dto) {

        log.info("REST request to create supply item: {}", dto);
        SupplyItemResponseDTO created = itemService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Отримати матеріал за ID",
            description = "Повертає детальну інформацію про матеріал за його ідентифікатором"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Матеріал знайдено"),
            @ApiResponse(responseCode = "404", description = "Матеріал не знайдено")
    })
    public ResponseEntity<SupplyItemResponseDTO> getById(
            @Parameter(description = "ID матеріалу", required = true)
            @PathVariable Long id) {

        log.info("REST request to get supply item by ID: {}", id);
        return ResponseEntity.ok(itemService.getById(id));
    }

    @GetMapping
    @Operation(
            summary = "Отримати список матеріалів",
            description = """
                    Повертає список всіх матеріалів з можливістю фільтрації по статусу та категорії.
                    
                    **Приклади використання:**
                    - GET /api/supply-items - всі матеріали
                    - GET /api/supply-items?status=IN_STOCK - тільки на складі
                    - GET /api/supply-items?categoryId=1 - тільки категорія 1
                    - GET /api/supply-items?status=IN_STOCK&categoryId=1 - обидва фільтри
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список успішно отримано")
    })
    public ResponseEntity<List<SupplyItemResponseDTO>> getAll(
            @Parameter(description = "Фільтр по статусу (опційно)")
            @RequestParam(required = false) ItemStatus status,

            @Parameter(description = "Фільтр по ID категорії (опційно)")
            @RequestParam(required = false) Long categoryId) {

        log.info("REST request to get all items with status: {}, categoryId: {}", status, categoryId);
        List<SupplyItemResponseDTO> items = itemService.getAll(status, categoryId);
        return ResponseEntity.ok(items);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Оновити матеріал",
            description = """
                    Оновлює інформацію про існуючий матеріал.
                    Оновлюються тільки передані поля (partial update).
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Матеріал успішно оновлено"),
            @ApiResponse(responseCode = "400", description = "Помилка валідації"),
            @ApiResponse(responseCode = "404", description = "Матеріал не знайдено")
    })
    public ResponseEntity<SupplyItemResponseDTO> update(
            @Parameter(description = "ID матеріалу", required = true)
            @PathVariable Long id,

            @Valid @RequestBody SupplyItemUpdateDTO dto) {

        log.info("REST request to update supply item {}: {}", id, dto);
        SupplyItemResponseDTO updated = itemService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Видалити матеріал",
            description = "Видаляє матеріал з системи"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Матеріал успішно видалено"),
            @ApiResponse(responseCode = "404", description = "Матеріал не знайдено")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID матеріалу", required = true)
            @PathVariable Long id) {

        log.info("REST request to delete supply item: {}", id);
        itemService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/expiring-soon")
    @Operation(
            summary = "Матеріали з терміном що закінчується",
            description = """
                    Повертає список матеріалів з терміном придатності що закінчується в найближчі N днів.
                    
                    **Приклади:**
                    - GET /api/supply-items/expiring-soon?days=7 - закінчуються за 7 днів
                    - GET /api/supply-items/expiring-soon?days=30 - закінчуються за місяць
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список успішно отримано")
    })
    public ResponseEntity<List<SupplyItemResponseDTO>> getExpiringSoon(
            @Parameter(description = "Кількість днів (за замовчуванням 30)")
            @RequestParam(defaultValue = "30") int days) {

        log.info("REST request to get items expiring in {} days", days);
        List<SupplyItemResponseDTO> items = itemService.findExpiringSoon(days);
        return ResponseEntity.ok(items);
    }
}
