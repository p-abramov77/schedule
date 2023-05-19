package rbc.ru.schedule.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rbc.ru.schedule.entity.EquipmentEntity;
import rbc.ru.schedule.repository.EquipmentRepo;

import java.util.HashSet;
import java.util.Set;

@Service
public class EquipmentServiceImpl implements EquipmentService{
    @Autowired
    EquipmentRepo equipmentRepo;
    @Override
    public Set<EquipmentEntity> getByName(String name) {
        return equipmentRepo.findAllByNameStartingWithOrderByName(name);
    }

    @Override
    public EquipmentEntity getById(Long id) {
        return equipmentRepo.findById(id).orElse(null);
    }

    @Override
    public boolean save(EquipmentEntity equipmentEntity) {
        try{
            equipmentRepo.save(equipmentEntity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Set<EquipmentEntity> available(Set<EquipmentEntity> list) {
        Set<Long> longs = new HashSet<>();
        for (EquipmentEntity equipment : list ) {
            longs.add(equipment.getId());
        }
        return equipmentRepo.findByIdNotIn(longs);
    }
}
