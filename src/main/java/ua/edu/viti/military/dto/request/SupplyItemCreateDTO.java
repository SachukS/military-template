package ua.edu.viti.military.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.edu.viti.military.entity.HazardClass;

import java.time.LocalDate;

/**
 * DTO для створення нового матеріалу
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplyItemCreateDTO {

    @NotBlank(message = "Назва матеріалу обов'язкова")
    @Size(max = 200, message = "Назва не може бути довшою за 200 символів")
    private String name;

    @NotBlank(message = "Номер партії обов'язковий")
    @Size(max = 50, message = "Номер партії не може бути довшим за 50 символів")
    private String batchNumber;

    @NotNull(message = "Категорія обов'язкова")
    @Positive(message = "ID категорії має бути позитивним")
    private Long categoryId;

    @NotNull(message = "Кількість обов'язкова")
    @Positive(message = "Кількість має бути позитивною")
    private Integer quantity;

    @Size(max = 20, message = "Одиниця виміру не може бути довшою за 20 символів")
    private String unit = "шт";

    @Future(message = "Термін придатності має бути в майбутньому")
    private LocalDate expirationDate;

    @NotNull(message = "Клас небезпеки обов'язковий")
    private HazardClass hazardClass;

    @Size(max = 200, message = "Умови зберігання не можуть бути довшими за 200 символів")
    private String storageConditions;

    @Positive(message = "ID складу має бути позитивним")
    private Long warehouseId;
}
