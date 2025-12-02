package ua.edu.viti.military.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.edu.viti.military.entity.SupplyCategory;

import java.util.Optional;

/**
 * Repository для роботи з категоріями матеріалів
 */
@Repository
public interface SupplyCategoryRepository extends JpaRepository<SupplyCategory, Long> {

    /**
     * Пошук категорії по назві
     */
    Optional<SupplyCategory> findByName(String name);

    /**
     * Перевірка існування категорії по назві
     */
    boolean existsByName(String name);

    /**
     * Пошук категорії по коду
     */
    Optional<SupplyCategory> findByCode(String code);

    /**
     * Перевірка існування категорії по коду
     */
    boolean existsByCode(String code);
}
