package rbc.ru.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rbc.ru.schedule.entity.EquipmentEntity;

import java.util.Set;

@Repository
public interface EquipmentRepo extends JpaRepository<EquipmentEntity, Long> {
    Set<EquipmentEntity> findAllByNameStartingWithOrderByName(String name);
    Set<EquipmentEntity> findByIdNotIn(Set<Long> equipments);

}
