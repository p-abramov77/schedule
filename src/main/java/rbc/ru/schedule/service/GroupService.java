package rbc.ru.schedule.service;

import org.springframework.stereotype.Service;
import rbc.ru.schedule.entity.GroupEntity;
import rbc.ru.schedule.entity.TagEntity;

import java.util.Set;

@Service
public interface GroupService {
    Set<GroupEntity> getByName(String name);
    GroupEntity getById(Long id);
    boolean save(GroupEntity groupEntity);
    Set<GroupEntity> available(Set<GroupEntity> list);
}
