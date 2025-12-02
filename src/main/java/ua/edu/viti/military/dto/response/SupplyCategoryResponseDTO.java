package ua.edu.viti.military.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO для відповіді з інформацією про категорію матеріалів
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplyCategoryResponseDTO {
    private Long id;
    private String name;
    private String code;
    private String description;
    private Boolean requiresColdStorage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
