package ua.edu.viti.military.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO для відповіді з інформацією про склад
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseResponseDTO {
    private Long id;
    private String name;
    private String code;
    private String address;
    private Integer capacity;
    private Integer currentOccupancy;
    private Boolean hasRefrigeration;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
