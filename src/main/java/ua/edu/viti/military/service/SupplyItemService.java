package ua.edu.viti.military.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.viti.military.dto.request.SupplyItemCreateDTO;
import ua.edu.viti.military.dto.request.SupplyItemUpdateDTO;
import ua.edu.viti.military.dto.response.SupplyCategoryResponseDTO;
import ua.edu.viti.military.dto.response.SupplyItemResponseDTO;
import ua.edu.viti.military.entity.ItemStatus;
import ua.edu.viti.military.entity.SupplyCategory;
import ua.edu.viti.military.entity.SupplyItem;
import ua.edu.viti.military.entity.Warehouse;
import ua.edu.viti.military.exception.BusinessLogicException;
import ua.edu.viti.military.exception.DuplicateResourceException;
import ua.edu.viti.military.exception.ResourceNotFoundException;
import ua.edu.viti.military.repository.SupplyCategoryRepository;
import ua.edu.viti.military.repository.SupplyItemRepository;
import ua.edu.viti.military.repository.WarehouseRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service для роботи з матеріалами МТЗ
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SupplyItemService {

    private final SupplyItemRepository itemRepository;
    private final SupplyCategoryRepository categoryRepository;
    private final WarehouseRepository warehouseRepository;

    /**
     * Створення нового матеріалу
     */
    @Transactional
    public SupplyItemResponseDTO create(SupplyItemCreateDTO dto) {
        log.info("Creating new supply item with batch number: {}", dto.getBatchNumber());

        // 1. Перевірка унікальності номеру партії
        if (itemRepository.existsByBatchNumber(dto.getBatchNumber())) {
            throw new DuplicateResourceException(
                    "Матеріал з партією " + dto.getBatchNumber() + " вже існує"
            );
        }

        // 2. Перевірка терміну придатності
        if (dto.getExpirationDate() != null && dto.getExpirationDate().isBefore(LocalDate.now())) {
            throw new BusinessLogicException(
                    "Не можна створити матеріал з минулим терміном придатності"
            );
        }

        // 3. Знайти категорію
        SupplyCategory category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Категорію з ID " + dto.getCategoryId() + " не знайдено"
                ));

        // 4. Знайти склад (якщо вказано)
        Warehouse warehouse = null;
        if (dto.getWarehouseId() != null) {
            warehouse = warehouseRepository.findById(dto.getWarehouseId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Склад з ID " + dto.getWarehouseId() + " не знайдено"
                    ));
        }

        // 5. Створити Entity
        SupplyItem item = new SupplyItem();
        item.setName(dto.getName());
        item.setBatchNumber(dto.getBatchNumber());
        item.setCategory(category);
        item.setQuantity(dto.getQuantity());
        item.setUnit(dto.getUnit());
        item.setExpirationDate(dto.getExpirationDate());
        item.setHazardClass(dto.getHazardClass());
        item.setStorageConditions(dto.getStorageConditions());
        item.setWarehouse(warehouse);
        item.setStatus(ItemStatus.IN_STOCK); // Початковий статус

        // 6. Зберегти
        SupplyItem saved = itemRepository.save(item);
        log.info("Supply item created with ID: {}", saved.getId());

        return toResponseDTO(saved);
    }

    /**
     * Отримання матеріалу за ID
     */
    public SupplyItemResponseDTO getById(Long id) {
        log.debug("Fetching supply item with ID: {}", id);

        SupplyItem item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Матеріал з ID " + id + " не знайдено"
                ));

        return toResponseDTO(item);
    }

    /**
     * Отримання всіх матеріалів з фільтрацією
     */
    public List<SupplyItemResponseDTO> getAll(ItemStatus status, Long categoryId) {
        log.debug("Fetching all items with status: {}, categoryId: {}", status, categoryId);

        List<SupplyItem> items;

        if (status != null && categoryId != null) {
            // Обидва фільтри
            items = itemRepository.findByStatusAndCategoryId(status, categoryId);
        } else if (status != null) {
            // Тільки по статусу
            items = itemRepository.findByStatus(status);
        } else if (categoryId != null) {
            // Тільки по категорії
            items = itemRepository.findByCategoryId(categoryId);
        } else {
            // Без фільтрів
            items = itemRepository.findAll();
        }

        return items.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Оновлення матеріалу
     */
    @Transactional
    public SupplyItemResponseDTO update(Long id, SupplyItemUpdateDTO dto) {
        log.info("Updating supply item with ID: {}", id);

        // 1. Знайти існуючий запис
        SupplyItem item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Матеріал з ID " + id + " не знайдено"
                ));

        // 2. Оновити тільки ті поля що передані (не null)
        if (dto.getName() != null) {
            item.setName(dto.getName());
        }
        if (dto.getQuantity() != null) {
            item.setQuantity(dto.getQuantity());
        }
        if (dto.getExpirationDate() != null) {
            if (dto.getExpirationDate().isBefore(LocalDate.now())) {
                throw new BusinessLogicException(
                        "Термін придатності не може бути в минулому"
                );
            }
            item.setExpirationDate(dto.getExpirationDate());
        }
        if (dto.getStorageConditions() != null) {
            item.setStorageConditions(dto.getStorageConditions());
        }
        if (dto.getWarehouseId() != null) {
            Warehouse warehouse = warehouseRepository.findById(dto.getWarehouseId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Склад не знайдено"
                    ));
            item.setWarehouse(warehouse);
        }
        if (dto.getStatus() != null) {
            item.setStatus(dto.getStatus());
        }

        // 3. Зберегти (Hibernate автоматично зробить UPDATE)
        SupplyItem updated = itemRepository.save(item);
        log.info("Supply item with ID {} updated successfully", id);

        return toResponseDTO(updated);
    }

    /**
     * Видалення матеріалу
     */
    @Transactional
    public void delete(Long id) {
        log.info("Deleting supply item with ID: {}", id);

        if (!itemRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Матеріал з ID " + id + " не знайдено"
            );
        }

        itemRepository.deleteById(id);
        log.info("Supply item with ID {} deleted successfully", id);
    }

    /**
     * Пошук матеріалів з терміном придатності що закінчується
     */
    public List<SupplyItemResponseDTO> findExpiringSoon(int daysThreshold) {
        log.info("Finding items expiring in {} days", daysThreshold);

        LocalDate thresholdDate = LocalDate.now().plusDays(daysThreshold);

        List<SupplyItem> expiring = itemRepository.findByExpirationDateBefore(thresholdDate);

        return expiring.stream()
                .filter(item -> item.getExpirationDate() != null && 
                               item.getExpirationDate().isAfter(LocalDate.now()))
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Маппінг Entity → ResponseDTO
     */
    private SupplyItemResponseDTO toResponseDTO(SupplyItem entity) {
        SupplyItemResponseDTO dto = new SupplyItemResponseDTO();

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setBatchNumber(entity.getBatchNumber());

        // Конвертація вкладеної Category
        dto.setCategory(toCategoryDTO(entity.getCategory()));

        dto.setQuantity(entity.getQuantity());
        dto.setUnit(entity.getUnit());
        dto.setExpirationDate(entity.getExpirationDate());
        dto.setHazardClass(entity.getHazardClass());
        dto.setStorageConditions(entity.getStorageConditions());

        // Warehouse - тільки ID та назва
        if (entity.getWarehouse() != null) {
            dto.setWarehouseId(entity.getWarehouse().getId());
            dto.setWarehouseName(entity.getWarehouse().getName());
        }

        dto.setStatus(entity.getStatus());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        return dto;
    }

    /**
     * Маппінг Category Entity → CategoryDTO
     */
    private SupplyCategoryResponseDTO toCategoryDTO(SupplyCategory entity) {
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
