package ua.edu.viti.military.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.edu.viti.military.entity.HazardClass;
import ua.edu.viti.military.entity.ItemStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO для відповіді з інформацією про матеріал
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplyItemResponseDTO {
    private Long id;
    private String name;
    private String batchNumber;
    
    // Вкладена CategoryDTO замість всього об'єкту Category
    private SupplyCategoryResponseDTO category;
    
    private Integer quantity;
    private String unit;
    private LocalDate expirationDate;
    private HazardClass hazardClass;
    private String storageConditions;
    
    // Тільки ID та назва складу (щоб уникнути циклів)
    private Long warehouseId;
    private String warehouseName;
    
    private ItemStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
