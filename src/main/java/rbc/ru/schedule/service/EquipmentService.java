package rbc.ru.schedule.service;

import org.springframework.stereotype.Service;
import rbc.ru.schedule.entity.EquipmentEntity;

import java.util.Set;

@Service
public interface EquipmentService {
    Set<EquipmentEntity> getByName(String name);
    EquipmentEntity getById(Long id);
    boolean save(EquipmentEntity equipmentEntity);
    Set<EquipmentEntity> available(Set<EquipmentEntity> list);
}
