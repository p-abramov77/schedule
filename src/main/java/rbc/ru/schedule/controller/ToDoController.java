package rbc.ru.schedule.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import rbc.ru.schedule.entity.ProjectEntity;
import rbc.ru.schedule.entity.RoleEntity;
import rbc.ru.schedule.entity.ToDoEntity;
import rbc.ru.schedule.service.ProjectServiceImpl;
import rbc.ru.schedule.service.ToDoServiceImpl;
import rbc.ru.schedule.service.UserServiceImpl;
import rbc.ru.schedule.validator.UserValidator;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Set;

@Controller
@RequestMapping("/schedule")
public class ToDoController {
    @Autowired
    ToDoServiceImpl toDoService;
    @Autowired
    ProjectServiceImpl projectService;
    @Autowired
    UserValidator userValidator;
    @Autowired
    UserServiceImpl userService;

    @GetMapping("todos{id}")
    public String list(Model model, Principal principal,
                       @RequestParam(value = "id") long project_id) {

        model.addAttribute("userName", principal.getName());
        model.addAttribute("isAdmin", userValidator.isAdmin(principal.getName()));

        Set<ToDoEntity> toDoEntities = toDoService.findAllByProjectId(project_id);
        model.addAttribute("project", projectService.getById(project_id, principal.getName()));
        model.addAttribute("list", toDoEntities);

        return "todos";
    }

    @GetMapping("newTODO{id}")
    public String newOne(Model model,
                         Principal principal,
                         @RequestParam(value = "id") long project_id) {

        ToDoEntity toDoEntity = new ToDoEntity();
        toDoEntity.setProject(projectService.getById(project_id,principal.getName()));
        toDoEntity.setChangedByUser(userService.findUserByUsername(principal.getName()).getId());
        toDoEntity.setStart(LocalDateTime.now());
        toDoEntity.setStop(LocalDateTime.now());
        toDoEntity.setDateTime(LocalDateTime.now());

        model.addAttribute("todo", toDoEntity);

        return "todo";
    }

    @GetMapping("todoEdit{id}")
    public String edit(Model model,
                         Principal principal,
                         @RequestParam(value = "id") long id) {

        ToDoEntity toDoEntity = toDoService.findById(id);
        toDoEntity.setChangedByUser(userService.findUserByUsername(principal.getName()).getId());
        toDoEntity.setDateTime(LocalDateTime.now());

        model.addAttribute("todo", toDoEntity);

        return "todo";
    }

    @PostMapping("saveTODO")
    public String save(@ModelAttribute("todo") @Valid ToDoEntity toDoEntity,
                       BindingResult bindingResult,
                       Principal principal) {
        //TODO проверить права

        if(bindingResult.hasErrors()) {
            System.out.println("todo save Errors");
            for(FieldError err : bindingResult.getFieldErrors()) {
                System.out.println(err.toString());
            }
            return "todo";
        }

        toDoService.save(toDoEntity);
        //TODO отправить уведомление по почте
        return "redirect:/schedule/todos?id=" + toDoEntity.getProject().getId();
    }

}
