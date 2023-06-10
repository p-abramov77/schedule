package rbc.ru.schedule.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rbc.ru.schedule.entity.GroupEntity;
import rbc.ru.schedule.entity.TagEntity;
import rbc.ru.schedule.repository.GroupRepo;

import java.util.HashSet;
import java.util.Set;

@Service
public class GroupServiceImpl implements GroupService{
    @Autowired
    GroupRepo groupRepo;
    @Override
    public Set<GroupEntity> getByName(String name) {
        return groupRepo.findAllByNameStartingWithOrderByName(name);
    }

    @Override
    public GroupEntity getById(Long id) {
        return groupRepo.findById(id).orElse(null);
    }

    @Override
    public boolean save(GroupEntity groupEntity) {
        try{
            groupRepo.save(groupEntity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Set<GroupEntity> available(Set<GroupEntity> list) {
        Set<Long> longs = new HashSet<>();
        for (GroupEntity group : list ) {
            longs.add(group.getId());
        }
        return groupRepo.findByIdNotIn(longs);
    }
}
