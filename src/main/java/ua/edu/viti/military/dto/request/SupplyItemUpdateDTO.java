package ua.edu.viti.military.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplyItemUpdateDTO {

    private String name;

    @Size(max = 50)
    private String batchNumber;

    @Positive
    private Integer quantity;

    @Future
    private LocalDate expirationDate;

    @Size(max = 200)
    private String storageConditions;

    private Long warehouseId;

    private String status;
}