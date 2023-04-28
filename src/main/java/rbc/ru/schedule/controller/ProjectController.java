package rbc.ru.schedule.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import rbc.ru.schedule.entity.ProjectEntity;
import rbc.ru.schedule.entity.UserEntity;
import rbc.ru.schedule.service.ProjectServiceImpl;
import rbc.ru.schedule.service.UserService;
import rbc.ru.schedule.service.UserServiceImpl;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/schedule")
public class ProjectController {
    @Autowired
    private ProjectServiceImpl projectService;
    @Autowired
    private UserServiceImpl userService;

    @GetMapping("projects")
    public String list(Model model,
                       @RequestParam(name = "tag",  defaultValue = "") String id,
                       @RequestParam(name = "user", defaultValue = "") String user,
                       @RequestParam(name = "name", defaultValue = "") String name) {
        Set<ProjectEntity> list;
        if(id.isEmpty()) {
            list = projectService.getByName(name);
        } else
        if (user.isEmpty()){
            System.out.println("id="+id);
            list = projectService.getByTag(Long.valueOf(id));
        }
        else {
            System.out.println("user = " + user);
            list = projectService.getByUsername(user);
        }

        model.addAttribute("name", name);
        model.addAttribute("list", list);

        return "projects";
    }
    @GetMapping("newProject")
    public String newOne(Model model, SessionStatus sessionStatus,
                         Principal principal) {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setCreator_id(userService.findUserByUsername(principal.getName()).getId());
        model.addAttribute("project", projectEntity);
        Set<String> listNames = userService.listNames();
        model.addAttribute("users", listNames);
        return "project";
    }
    @PostMapping("saveProject")
    public String save(@ModelAttribute("project") @Valid ProjectEntity projectEntity,
                       BindingResult bindingResult, SessionStatus sessionStatus,
                       Principal principal) {
        if(bindingResult.hasErrors()) {
            return "project";
        }
        projectService.save(projectEntity);

        return "redirect:/schedule/projects?name=" + projectEntity.getName();
    }
    @GetMapping("editProject/{id}")
    public String edit(Model model, @PathVariable(value = "id") long id) {
        ProjectEntity projectEntity = projectService.getById(id);
        model.addAttribute("project", projectEntity);
        return "project";
    }

}
