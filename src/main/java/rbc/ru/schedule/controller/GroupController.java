package rbc.ru.schedule.controller;

import org.springdoc.core.SpringDocUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import rbc.ru.schedule.entity.GroupEntity;
import rbc.ru.schedule.service.GroupServiceImpl;
import rbc.ru.schedule.validator.UserValidator;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Principal;
import java.util.Set;

@Controller
@RequestMapping("/schedule")
public class GroupController {
    static {
        SpringDocUtils.getConfig().addRestControllers(GroupController.class);
    }
    @Autowired
    GroupServiceImpl groupService;

    @Autowired
    UserValidator userValidator;

    @GetMapping("groups")
    public String list(Model model, Principal principal,
                       @RequestParam(defaultValue = "") String name) {
        Set<GroupEntity> list = groupService.getByName(name);

        model.addAttribute("userName", principal.getName());
        model.addAttribute("isAdmin", userValidator.isAdmin(principal.getName()));
        model.addAttribute("name", name);
        model.addAttribute("list", list);

        return "groups";
    }
    @GetMapping("newGroup")
    public String newOne(Model model) {
        GroupEntity groupEntity = new GroupEntity();
        model.addAttribute("group", groupEntity);
        return "group";
    }
    @PostMapping("saveGroup")
    public String save(Model model, @ModelAttribute("group") @Valid GroupEntity groupEntity,
                       BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "group";
        }
        //TODO проверить права

        if(!groupService.save(groupEntity)) {
            model.addAttribute("errorMessage", "Группа с указанным именем существует");
            return "group";
        }

        try {
            return "redirect:/schedule/groups?name=" + URLEncoder.encode(groupEntity.getName(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "redirect:/schedule/groups";
        }
    }
    @GetMapping("editGroup/{id}")
    public String edit(Model model, @PathVariable(value = "id") long id) {
        GroupEntity groupEntity = groupService.getById(id);
        model.addAttribute("group", groupEntity);
        return "group";
    }

}
