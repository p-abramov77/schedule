package rbc.ru.schedule.service;

import org.springframework.stereotype.Service;
import rbc.ru.schedule.entity.TagEntity;

import java.util.Set;

@Service
public interface TagService {
    Set<TagEntity> getByName(String name);
    TagEntity getById(Long id);
    boolean save(TagEntity tagEntity);
    Set<TagEntity> available(Set<TagEntity> list);
}
