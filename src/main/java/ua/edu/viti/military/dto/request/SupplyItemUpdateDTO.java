package ua.edu.viti.military.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.edu.viti.military.entity.ItemStatus;

import java.time.LocalDate;

/**
 * DTO для оновлення матеріалу (тільки поля що можна змінювати)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplyItemUpdateDTO {

    @Size(max = 200, message = "Назва не може бути довшою за 200 символів")
    private String name;

    @Positive(message = "Кількість має бути позитивною")
    private Integer quantity;

    @Future(message = "Термін придатності має бути в майбутньому")
    private LocalDate expirationDate;

    @Size(max = 200, message = "Умови зберігання не можуть бути довшими за 200 символів")
    private String storageConditions;

    @Positive(message = "ID складу має бути позитивним")
    private Long warehouseId;

    private ItemStatus status;
}
