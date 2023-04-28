package rbc.ru.schedule.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rbc.ru.schedule.entity.TagEntity;
import rbc.ru.schedule.repository.TagRepo;

import java.util.HashSet;
import java.util.Set;

@Service
public class TagServiceImpl implements TagService{

    @Autowired
    TagRepo tagRepo;

    @Override
    public Set<TagEntity> getByName(String name) {
        return tagRepo.findAllByNameStartingWithOrderByName(name);
    }

    @Override
    public TagEntity getById(Long id) {
        return  tagRepo.findById(id).orElse(null);
    }

    @Override
    public boolean save(TagEntity tagEntity) {
        try{
            tagRepo.save(tagEntity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Set<TagEntity> available(Set<TagEntity> list) {
        Set<Long> longs = new HashSet<>();
        for (TagEntity tag : list ) {
            longs.add(tag.getId());
        }
        return tagRepo.findByIdNotIn(longs);
    }
}
