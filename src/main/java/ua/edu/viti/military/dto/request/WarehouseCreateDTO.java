package ua.edu.viti.military.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseCreateDTO {

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(max = 20)
    private String code;

    @Size(max = 200)
    private String address;

    private Integer capacity;

    private Integer currentOccupancy;

    private Boolean hasRefrigeration = false;

    // Explicit getters for compilation when Lombok not available
    public String getName() { return name; }
    public String getCode() { return code; }
    public String getAddress() { return address; }
    public Integer getCapacity() { return capacity; }
    public Integer getCurrentOccupancy() { return currentOccupancy; }
    public Boolean getHasRefrigeration() { return hasRefrigeration; }
}
