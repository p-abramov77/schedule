package rbc.ru.schedule.controller;

import org.springdoc.core.SpringDocUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import rbc.ru.schedule.entity.GroupEntity;
import rbc.ru.schedule.entity.UserEntity;
import rbc.ru.schedule.service.GroupServiceImpl;
import rbc.ru.schedule.service.UserServiceImpl;

import java.security.Principal;
import java.util.Set;

@Controller
@RequestMapping("/schedule")
public class UserGroupController {
    static {
        SpringDocUtils.getConfig().addRestControllers(UserGroupController.class);
    }
    @Autowired
    UserServiceImpl userService;
    @Autowired
    GroupServiceImpl groupService;

    @GetMapping("userGroups/{user_id}")
    public String list(Model model,
                       Principal principal,
                       @PathVariable(value = "user_id") Long user_id) {

        UserEntity userEntity = userService.getById(user_id);

        model.addAttribute("user_id", user_id);
        model.addAttribute("user_name", userEntity.getUsername());
        model.addAttribute("groups", userEntity.getGroups());

        Set<GroupEntity> available_groups;
        if(userEntity.getGroups().size() == 0)
            available_groups = groupService.getByName("");
        else
            available_groups = groupService.available(userEntity.getGroups());

        model.addAttribute("available_groups", available_groups);

        return "user_groups";
    }
    @GetMapping("userGroups/add/{user_id}/{group_id}")
    public String add(Model model,
                      Principal principal,
                      @PathVariable(value = "user_id") Long user_id,
                      @PathVariable(value = "group_id") Long group_id) {

        //TODO проверить права

        UserEntity userEntity = userService.getById(user_id);

        userEntity.addGroup(groupService.getById(group_id));

        userService.save(userEntity);
        //TODO отправить уведомление по почте
        return "redirect:/schedule/userGroups/" + user_id;
    }
    @GetMapping("userGroups/remove/{user_id}/{group_id}")
    public String remove(Model model, Principal principal,
                         @PathVariable(value = "user_id") long user_id,
                         @PathVariable(value = "group_id") long group_id) {

        //TODO проверить права

        UserEntity userEntity = userService.getById(user_id);
        userEntity.removeGroup(groupService.getById(group_id));

        userService.save(userEntity);
        //TODO отправить уведомление по почте
        return "redirect:/schedule/userGroups/" + user_id;
    }

}
