package ua.edu.viti.military.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.viti.military.dto.request.SupplyCategoryCreateDTO;
import ua.edu.viti.military.dto.response.SupplyCategoryResponseDTO;
import ua.edu.viti.military.entity.SupplyCategory;
import ua.edu.viti.military.exception.DuplicateResourceException;
import ua.edu.viti.military.exception.ResourceNotFoundException;
import ua.edu.viti.military.repository.SupplyCategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service для роботи з категоріями матеріалів
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SupplyCategoryService {

    private final SupplyCategoryRepository categoryRepository;

    /**
     * Створення нової категорії
     */
    @Transactional
    public SupplyCategoryResponseDTO create(SupplyCategoryCreateDTO dto) {
        log.info("Creating new supply category with code: {}", dto.getCode());

        // Перевірка унікальності коду
        if (categoryRepository.existsByCode(dto.getCode())) {
            throw new DuplicateResourceException(
                    "Категорія з кодом " + dto.getCode() + " вже існує"
            );
        }

        // Перевірка унікальності назви
        if (categoryRepository.existsByName(dto.getName())) {
            throw new DuplicateResourceException(
                    "Категорія з назвою " + dto.getName() + " вже існує"
            );
        }

        // Створення Entity
        SupplyCategory category = new SupplyCategory();
        category.setName(dto.getName());
        category.setCode(dto.getCode());
        category.setDescription(dto.getDescription());
        category.setRequiresColdStorage(dto.getRequiresColdStorage());

        // Збереження
        SupplyCategory saved = categoryRepository.save(category);
        log.info("Supply category created with ID: {}", saved.getId());

        return toResponseDTO(saved);
    }

    /**
     * Отримання категорії за ID
     */
    public SupplyCategoryResponseDTO getById(Long id) {
        log.debug("Fetching supply category with ID: {}", id);

        SupplyCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Категорію з ID " + id + " не знайдено"
                ));

        return toResponseDTO(category);
    }

    /**
     * Отримання всіх категорій
     */
    public List<SupplyCategoryResponseDTO> getAll() {
        log.debug("Fetching all supply categories");

        return categoryRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Оновлення категорії
     */
    @Transactional
    public SupplyCategoryResponseDTO update(Long id, SupplyCategoryCreateDTO dto) {
        log.info("Updating supply category with ID: {}", id);

        SupplyCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Категорію з ID " + id + " не знайдено"
                ));

        // Перевірка унікальності коду (якщо змінюється)
        if (!category.getCode().equals(dto.getCode()) && categoryRepository.existsByCode(dto.getCode())) {
            throw new DuplicateResourceException(
                    "Категорія з кодом " + dto.getCode() + " вже існує"
            );
        }

        // Перевірка унікальності назви (якщо змінюється)
        if (!category.getName().equals(dto.getName()) && categoryRepository.existsByName(dto.getName())) {
            throw new DuplicateResourceException(
                    "Категорія з назвою " + dto.getName() + " вже існує"
            );
        }

        // Оновлення полів
        category.setName(dto.getName());
        category.setCode(dto.getCode());
        category.setDescription(dto.getDescription());
        category.setRequiresColdStorage(dto.getRequiresColdStorage());

        SupplyCategory updated = categoryRepository.save(category);
        log.info("Supply category with ID {} updated successfully", id);

        return toResponseDTO(updated);
    }

    /**
     * Видалення категорії
     */
    @Transactional
    public void delete(Long id) {
        log.info("Deleting supply category with ID: {}", id);

        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Категорію з ID " + id + " не знайдено"
            );
        }

        categoryRepository.deleteById(id);
        log.info("Supply category with ID {} deleted successfully", id);
    }

    /**
     * Маппінг Entity → ResponseDTO
     */
    private SupplyCategoryResponseDTO toResponseDTO(SupplyCategory entity) {
        SupplyCategoryResponseDTO dto = new SupplyCategoryResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCode(entity.getCode());
        dto.setDescription(entity.getDescription());
        dto.setRequiresColdStorage(entity.getRequiresColdStorage());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
