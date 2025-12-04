package ua.edu.viti.military.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.viti.military.dto.request.SupplyItemCreateDTO;
import ua.edu.viti.military.dto.request.SupplyItemUpdateDTO;
import ua.edu.viti.military.dto.response.SupplyCategoryResponseDTO;
import ua.edu.viti.military.dto.response.SupplyItemResponseDTO;
import ua.edu.viti.military.entity.SupplyCategory;
import ua.edu.viti.military.entity.ItemStatus;
import ua.edu.viti.military.entity.SupplyItem;
import ua.edu.viti.military.entity.Warehouse;
import ua.edu.viti.military.exception.DuplicateResourceException;
import ua.edu.viti.military.exception.ResourceNotFoundException;
import ua.edu.viti.military.repository.SupplyCategoryRepository;
import ua.edu.viti.military.repository.SupplyItemRepository;
import ua.edu.viti.military.repository.WarehouseRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SupplyItemService {

    private final SupplyItemRepository itemRepository;
    private final SupplyCategoryRepository categoryRepository;
    private final WarehouseRepository warehouseRepository;

    @Transactional
    public SupplyItemResponseDTO create(SupplyItemCreateDTO dto) {
        log.info("Creating supply item with batch {}", dto.getBatchNumber());

        if (itemRepository.existsByBatchNumber(dto.getBatchNumber())) {
            throw new DuplicateResourceException("Партія вже існує: " + dto.getBatchNumber());
        }

        SupplyCategory category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Категорію не знайдено: " + dto.getCategoryId()));

        Warehouse warehouse = null;
        if (dto.getWarehouseId() != null) {
            warehouse = warehouseRepository.findById(dto.getWarehouseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Склад не знайдено: " + dto.getWarehouseId()));
        }

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
        item.setStatus(ua.edu.viti.military.entity.ItemStatus.IN_STOCK);

        SupplyItem saved = itemRepository.save(item);
        return toResponseDTO(saved);
    }

    public SupplyItemResponseDTO getById(Long id) {
        return itemRepository.findById(id)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Матеріал не знайдено: " + id));
    }

    public List<SupplyItemResponseDTO> getAll() {
        return itemRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public SupplyItemResponseDTO update(Long id, SupplyItemUpdateDTO dto) {
        SupplyItem item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Матеріал не знайдено: " + id));

        if (dto.getName() != null) {
            item.setName(dto.getName());
        }
        if (dto.getQuantity() != null) {
            item.setQuantity(dto.getQuantity());
        }
        if (dto.getExpirationDate() != null) {
            item.setExpirationDate(dto.getExpirationDate());
        }
        if (dto.getStorageConditions() != null) {
            item.setStorageConditions(dto.getStorageConditions());
        }
        if (dto.getWarehouseId() != null) {
            Warehouse warehouse = warehouseRepository.findById(dto.getWarehouseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Склад не знайдено: " + dto.getWarehouseId()));
            item.setWarehouse(warehouse);
        }
        if (dto.getStatus() != null) {
            item.setStatus(ItemStatus.valueOf(dto.getStatus()));
        }

        SupplyItem updated = itemRepository.save(item);
        return toResponseDTO(updated);
    }

    @Transactional
    public void delete(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new ResourceNotFoundException("Матеріал не знайдено: " + id);
        }
        itemRepository.deleteById(id);
    }

    private SupplyItemResponseDTO toResponseDTO(SupplyItem entity) {
        SupplyItemResponseDTO dto = new SupplyItemResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setBatchNumber(entity.getBatchNumber());

        SupplyCategoryResponseDTO cat = new SupplyCategoryResponseDTO();
        cat.setId(entity.getCategory().getId());
        cat.setName(entity.getCategory().getName());
        cat.setCode(entity.getCategory().getCode());
        cat.setDescription(entity.getCategory().getDescription());
        cat.setRequiresColdStorage(entity.getCategory().getRequiresColdStorage());
        cat.setCreatedAt(entity.getCategory().getCreatedAt());
        cat.setUpdatedAt(entity.getCategory().getUpdatedAt());
        dto.setCategory(cat);

        dto.setQuantity(entity.getQuantity());
        dto.setUnit(entity.getUnit());
        dto.setExpirationDate(entity.getExpirationDate());
        dto.setHazardClass(entity.getHazardClass());
        dto.setStorageConditions(entity.getStorageConditions());
        if (entity.getWarehouse() != null) {
            dto.setWarehouseId(entity.getWarehouse().getId());
            dto.setWarehouseName(entity.getWarehouse().getName());
        }
        dto.setStatus(entity.getStatus());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
