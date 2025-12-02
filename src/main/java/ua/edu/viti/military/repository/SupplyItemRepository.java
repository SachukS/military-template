package ua.edu.viti.military.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.edu.viti.military.entity.HazardClass;
import ua.edu.viti.military.entity.ItemStatus;
import ua.edu.viti.military.entity.SupplyItem;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository для роботи з матеріалами МТЗ
 */
@Repository
public interface SupplyItemRepository extends JpaRepository<SupplyItem, Long> {

    /**
     * Пошук матеріалу по номеру партії
     */
    Optional<SupplyItem> findByBatchNumber(String batchNumber);

    /**
     * Перевірка існування матеріалу по номеру партії
     */
    boolean existsByBatchNumber(String batchNumber);

    /**
     * Пошук матеріалів по категорії
     */
    List<SupplyItem> findByCategoryId(Long categoryId);

    /**
     * Пошук матеріалів по статусу
     */
    List<SupplyItem> findByStatus(ItemStatus status);

    /**
     * Підрахунок матеріалів по статусу
     */
    long countByStatus(ItemStatus status);

    /**
     * Пошук матеріалів з терміном придатності раніше заданої дати
     */
    List<SupplyItem> findByExpirationDateBefore(LocalDate date);

    /**
     * Пошук матеріалів по складу
     */
    List<SupplyItem> findByWarehouseId(Long warehouseId);

    /**
     * Пошук матеріалів по класу небезпеки
     */
    List<SupplyItem> findByHazardClass(HazardClass hazardClass);

    /**
     * Пошук матеріалів з кількістю менше заданого порогу
     */
    List<SupplyItem> findByQuantityLessThan(Integer threshold);

    /**
     * Пошук матеріалів по статусу з завантаженням категорії (JOIN FETCH для уникнення N+1)
     */
    @Query("SELECT i FROM SupplyItem i JOIN FETCH i.category WHERE i.status = :status")
    List<SupplyItem> findByStatusWithCategory(@Param("status") ItemStatus status);

    /**
     * Пошук матеріалів по категорії та статусу
     */
    List<SupplyItem> findByStatusAndCategoryId(ItemStatus status, Long categoryId);
}
