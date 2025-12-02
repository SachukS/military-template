package ua.edu.viti.military.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для створення нового складу
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseCreateDTO {

    @NotBlank(message = "Назва складу обов'язкова")
    @Size(max = 100, message = "Назва не може бути довшою за 100 символів")
    private String name;

    @NotBlank(message = "Код складу обов'язковий")
    @Size(max = 20, message = "Код не може бути довшим за 20 символів")
    private String code;

    @Size(max = 200, message = "Адреса не може бути довшою за 200 символів")
    private String address;

    @PositiveOrZero(message = "Ємність не може бути від'ємною")
    private Integer capacity;

    @PositiveOrZero(message = "Поточна заповненість не може бути від'ємною")
    private Integer currentOccupancy = 0;

    private Boolean hasRefrigeration = false;
}
