package ua.edu.viti.military.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplyCategoryCreateDTO {

    @NotBlank(message = "Назва категорії обов'язкова")
    @Size(max = 100)
    private String name;

    @NotBlank(message = "Код категорії обов'язковий")
    @Size(max = 20)
    private String code;

    @Size(max = 500)
    private String description;

    private Boolean requiresColdStorage = false;
}
