package rbc.ru.schedule.controller;

import org.springdoc.core.SpringDocUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import rbc.ru.schedule.entity.ProjectEntity;
import rbc.ru.schedule.entity.TagEntity;
import rbc.ru.schedule.repository.ProjectRepo;
import rbc.ru.schedule.service.ProjectServiceImpl;
import rbc.ru.schedule.service.TagServiceImpl;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/schedule")
public class ProjectTagController {

    static {
        SpringDocUtils.getConfig().addRestControllers(ProjectTagController.class);
    }
    @Autowired
    ProjectServiceImpl projectService;
    @Autowired
    TagServiceImpl tagService;

    @GetMapping("projectTags/{project_id}")
    public String list(Model model, Principal principal,
                       @PathVariable(value = "project_id") long project_id) {

        ProjectEntity projectEntity = projectService.getById(project_id, principal.getName());
        System.out.println(projectEntity);

        model.addAttribute("project_id", project_id);
        model.addAttribute("project_name", projectEntity.getName());
        model.addAttribute("tags", projectEntity.getTags());

        Set<TagEntity> available_tags;
        if(projectEntity.getTags().size() == 0)
            available_tags = tagService.getByName("");
        else
            available_tags = tagService.available(projectEntity.getTags());
        model.addAttribute("available_tags", available_tags);

        return "project_tags";
    }
    @GetMapping("projectTags/add/{project_id}/{tag_id}")
    public String add(Model model, Principal principal,
                      @PathVariable(value = "project_id") long project_id,
                      @PathVariable(value = "tag_id") long tag_id) {
        //TODO проверить права
        System.out.println("project="+project_id+" tag="+tag_id);

        ProjectEntity projectEntity = projectService.getById(project_id, principal.getName());
        projectEntity.addTag(tagService.getById(tag_id));

        System.out.println("After add: "+ projectEntity);

        projectService.save(projectEntity);
        //TODO отправить уведомление по почте
        return "redirect:/schedule/projectTags/" + project_id;
    }
    @GetMapping("projectTags/remove/{project_id}/{tag_id}")
    public String remove(Model model, Principal principal,
                      @PathVariable(value = "project_id") long project_id,
                      @PathVariable(value = "tag_id") long tag_id) {
        //TODO проверить права
        System.out.println("project="+project_id+" tag="+tag_id);

        ProjectEntity projectEntity = projectService.getById(project_id, principal.getName());
        projectEntity.removeTag(tagService.getById(tag_id));

        projectService.save(projectEntity);
        //TODO отправить уведомление по почте
        return "redirect:/schedule/projectTags/" + project_id;
    }

}
