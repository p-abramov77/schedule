package rbc.ru.schedule.controller;

import org.springdoc.core.SpringDocUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import rbc.ru.schedule.entity.ProjectEntity;
import rbc.ru.schedule.entity.RoleEntity;
import rbc.ru.schedule.entity.UserEntity;
import rbc.ru.schedule.service.ProjectServiceImpl;
import rbc.ru.schedule.service.RoleServiceImpl;
import rbc.ru.schedule.service.UserServiceImpl;

import java.security.Principal;
import java.util.Set;

@Controller
@RequestMapping("/schedule")
public class RoleController {

    static {
        SpringDocUtils.getConfig().addRestControllers(RoleController.class);
    }
    @Autowired
    RoleServiceImpl roleService;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    ProjectServiceImpl projectService;

    @GetMapping("projectRoles/{project_id}")
    public String list(Model model, Principal principal,
                       @PathVariable(value = "project_id") long project_id) {

        ProjectEntity projectEntity = projectService.getById(project_id, principal.getName());

        model.addAttribute("isNotLastProducer", projectService.isNotLastProducer(projectEntity));
        model.addAttribute("project_id", project_id);
        model.addAttribute("project_name", projectEntity.getName());
        model.addAttribute("roles", projectEntity.getRoleEntities());
        model.addAttribute("countOfExecutors", roleService.countOfExecutors(project_id));

        Set<UserEntity> available_users;
        if(projectEntity.getRoleEntities().size() == 0) {
            available_users = userService.getAllUsers();
        } else {
            available_users = userService.available(project_id);
        }

        model.addAttribute("available_users", available_users);

        return "project_roles";
    }

    @GetMapping("projectRoles/add/{project_id}/{user_id}/{isProducer}")
    public String add(Model model,
                      Principal principal,
                      @PathVariable(value = "project_id") long project_id,
                      @PathVariable(value = "isProducer") boolean isProducer,
                      @PathVariable(value = "user_id") long user_id) {

        //TODO проверить права

        ProjectEntity projectEntity = projectService.getById(project_id, principal.getName());
        UserEntity user = userService.getById(user_id);

        RoleEntity role = new RoleEntity();
        role.setProject(projectEntity);
        role.setUser(user);
        role.setProducer(isProducer);

        roleService.save(role);
        //TODO отправить уведомление по почте
        return "redirect:/schedule/projectRoles/" + project_id;
    }

    @GetMapping("projectRoles/remove/{project_id}/{user_id}")
    public String remove(Model model,
                         Principal principal,
                         @PathVariable(value = "project_id") long project_id,
                         @PathVariable(value = "user_id") long user_id) {

        //TODO проверить права

        //Не удалять себя из списка заказчиков (продюсеров)
        if(user_id == userService.findUserByUsername(principal.getName()).getId())
            return "redirect:/schedule/projectRoles/" + project_id;

        roleService.removeByProjectAndUser(project_id, user_id);
        //TODO отправить уведомление по почте
        return "redirect:/schedule/projectRoles/" + project_id;
    }
}
