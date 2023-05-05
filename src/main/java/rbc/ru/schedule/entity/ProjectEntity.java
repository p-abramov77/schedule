package rbc.ru.schedule.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name="project")
public class ProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotNull
    private Long creator_id;
    private String name;
    private String comment;
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "project_tag",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<TagEntity> tags = new HashSet<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private Set<ToDoEntity> toDoEntitySet = new HashSet<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private Set<RoleEntity> roleEntities = new HashSet<>();

    public void addTag(TagEntity tag){
        this.tags.add(tag);
        tag.getProjectEntities().add(this);
    }
    public void removeTag(TagEntity tag){
        this.tags.remove(tag);
        tag.getProjectEntities().remove(this);
    }
//    @Transactional
//    public void addRole(RoleEntity role){
//        this.roleEntities.add(role);
//        role.setProject(this);
//    }
//    @Transactional
//    public void removeRole(RoleEntity role){
//        this.roleEntities.remove(role);
//        role.setProject(null);
//    }
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ProjectEntity)) return false;
        return id != null && id.equals(((ProjectEntity) o).getId());
    }
    @Override
    public int hashCode() {
        return 31;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Set<TagEntity> getTags() {
        return tags;
    }

    public Long getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(Long creator_id) {
        this.creator_id = creator_id;
    }

    public void setTags(Set<TagEntity> tags) {
        this.tags = tags;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        String entities = "";
        for (RoleEntity ent: roleEntities) {
            entities += ent.getUser().getUsername() + " " + ent.isProducer() + " ";
        }
        return "Name: " + name + " roleEntities = " + entities;
    }
}
