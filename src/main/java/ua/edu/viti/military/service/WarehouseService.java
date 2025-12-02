package ua.edu.viti.military.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.viti.military.dto.request.WarehouseCreateDTO;
import ua.edu.viti.military.dto.response.WarehouseResponseDTO;
import ua.edu.viti.military.entity.Warehouse;
import ua.edu.viti.military.exception.DuplicateResourceException;
import ua.edu.viti.military.exception.ResourceNotFoundException;
import ua.edu.viti.military.repository.WarehouseRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service для роботи зі складами
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;

    /**
     * Створення нового складу
     */
    @Transactional
    public WarehouseResponseDTO create(WarehouseCreateDTO dto) {
        log.info("Creating new warehouse with code: {}", dto.getCode());

        // Перевірка унікальності коду
        if (warehouseRepository.existsByCode(dto.getCode())) {
            throw new DuplicateResourceException(
                    "Склад з кодом " + dto.getCode() + " вже існує"
            );
        }

        // Створення Entity
        Warehouse warehouse = new Warehouse();
        warehouse.setName(dto.getName());
        warehouse.setCode(dto.getCode());
        warehouse.setAddress(dto.getAddress());
        warehouse.setCapacity(dto.getCapacity());
        warehouse.setCurrentOccupancy(dto.getCurrentOccupancy());
        warehouse.setHasRefrigeration(dto.getHasRefrigeration());

        // Збереження
        Warehouse saved = warehouseRepository.save(warehouse);
        log.info("Warehouse created with ID: {}", saved.getId());

        return toResponseDTO(saved);
    }

    /**
     * Отримання складу за ID
     */
    public WarehouseResponseDTO getById(Long id) {
        log.debug("Fetching warehouse with ID: {}", id);

        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Склад з ID " + id + " не знайдено"
                ));

        return toResponseDTO(warehouse);
    }

    /**
     * Отримання всіх складів
     */
    public List<WarehouseResponseDTO> getAll() {
        log.debug("Fetching all warehouses");

        return warehouseRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Оновлення складу
     */
    @Transactional
    public WarehouseResponseDTO update(Long id, WarehouseCreateDTO dto) {
        log.info("Updating warehouse with ID: {}", id);

        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Склад з ID " + id + " не знайдено"
                ));

        // Перевірка унікальності коду (якщо змінюється)
        if (!warehouse.getCode().equals(dto.getCode()) && warehouseRepository.existsByCode(dto.getCode())) {
            throw new DuplicateResourceException(
                    "Склад з кодом " + dto.getCode() + " вже існує"
            );
        }

        // Оновлення полів
        warehouse.setName(dto.getName());
        warehouse.setCode(dto.getCode());
        warehouse.setAddress(dto.getAddress());
        warehouse.setCapacity(dto.getCapacity());
        warehouse.setCurrentOccupancy(dto.getCurrentOccupancy());
        warehouse.setHasRefrigeration(dto.getHasRefrigeration());

        Warehouse updated = warehouseRepository.save(warehouse);
        log.info("Warehouse with ID {} updated successfully", id);

        return toResponseDTO(updated);
    }

    /**
     * Видалення складу
     */
    @Transactional
    public void delete(Long id) {
        log.info("Deleting warehouse with ID: {}", id);

        if (!warehouseRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Склад з ID " + id + " не знайдено"
            );
        }

        warehouseRepository.deleteById(id);
        log.info("Warehouse with ID {} deleted successfully", id);
    }

    /**
     * Маппінг Entity → ResponseDTO
     */
    private WarehouseResponseDTO toResponseDTO(Warehouse entity) {
        WarehouseResponseDTO dto = new WarehouseResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCode(entity.getCode());
        dto.setAddress(entity.getAddress());
        dto.setCapacity(entity.getCapacity());
        dto.setCurrentOccupancy(entity.getCurrentOccupancy());
        dto.setHasRefrigeration(entity.getHasRefrigeration());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
