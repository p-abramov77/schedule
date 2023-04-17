package rbc.ru.schedule.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import rbc.ru.schedule.entity.TagEntity;
import rbc.ru.schedule.entity.UserEntity;
import rbc.ru.schedule.service.TagServiceImpl;
import rbc.ru.schedule.service.UserServiceImpl;

import javax.validation.Valid;
import java.util.Set;

@Controller
@RequestMapping("/schedule")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @GetMapping("users")
    public String list(Model model, @RequestParam(defaultValue = "") String name) {
        Set<UserEntity> list = userService.listUsers(name);

        model.addAttribute("name", name);
        model.addAttribute("list", list);

        return "users";
    }
    @GetMapping("newUser")
    public String newOne(Model model) {
        UserEntity userEntity = new UserEntity();
        model.addAttribute("user", userEntity);
        return "user";
    }
    @PostMapping("saveUser")
    public String save(Model model, @ModelAttribute("user") @Valid UserEntity userEntity,
                       BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "user";
        }
        if(!userService.save(userEntity)) {
            model.addAttribute("errorMessage", "Пользователь с указанным именем существует");
            return "user";
        }

        return "redirect:/schedule/users?name=" + userEntity.getUsername();

    }
    @GetMapping("editUser/{id}")
    public String edit(Model model, @PathVariable(value = "id") long id) {
        UserEntity userEntity = userService.getById(id);
        model.addAttribute("user", userEntity);
        return "user";
    }
}
