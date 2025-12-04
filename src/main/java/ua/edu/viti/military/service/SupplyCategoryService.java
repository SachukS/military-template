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

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SupplyCategoryService {

    private final SupplyCategoryRepository categoryRepository;

    @Transactional
    public SupplyCategoryResponseDTO create(SupplyCategoryCreateDTO dto) {
        if (categoryRepository.existsByCode(dto.getCode())) {
            throw new DuplicateResourceException("Код вже існує: " + dto.getCode());
        }

        SupplyCategory category = new SupplyCategory();
        category.setName(dto.getName());
        category.setCode(dto.getCode());
        category.setDescription(dto.getDescription());
        category.setRequiresColdStorage(dto.getRequiresColdStorage());

        SupplyCategory saved = categoryRepository.save(category);
        return toResponseDTO(saved);
    }

    public SupplyCategoryResponseDTO getById(Long id) {
        return categoryRepository.findById(id)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Категорію не знайдено: " + id));
    }

    public List<SupplyCategoryResponseDTO> getAll() {
        return categoryRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

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
