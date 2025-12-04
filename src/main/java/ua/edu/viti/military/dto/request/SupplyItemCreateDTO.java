package ua.edu.viti.military.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.edu.viti.military.entity.HazardClass;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplyItemCreateDTO {

    @NotBlank
    @Size(max = 200)
    private String name;

    @NotBlank
    @Size(max = 50)
    private String batchNumber;

    @NotNull
    @Positive
    private Long categoryId;

    @NotNull
    @Positive
    private Integer quantity;

    @Size(max = 20)
    private String unit = "sht";

    @Future
    private LocalDate expirationDate;

    @NotNull
    private HazardClass hazardClass;

    @Size(max = 200)
    private String storageConditions;

    private Long warehouseId;
}