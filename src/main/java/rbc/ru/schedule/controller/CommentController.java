package rbc.ru.schedule.controller;

import org.springdoc.core.SpringDocUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import rbc.ru.schedule.entity.CommentEntity;
import rbc.ru.schedule.entity.ToDoEntity;
import rbc.ru.schedule.service.CommentServiceImpl;
import rbc.ru.schedule.service.ToDoServiceImpl;
import rbc.ru.schedule.service.UserServiceImpl;
import rbc.ru.schedule.validator.UserValidator;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/schedule")
public class CommentController {
    static {
        SpringDocUtils.getConfig().addRestControllers(CommentController.class);
    }

    @Autowired
    ToDoServiceImpl toDoService;
    @Autowired
    CommentServiceImpl commentService;
    @Autowired
    UserValidator userValidator;
    @Autowired
    UserServiceImpl userService;

    @GetMapping("comments{id}")
    public String list(Model model, Principal principal,
                       @RequestParam(value = "id") long todo_id) {

        model.addAttribute("userName", principal.getName());
        model.addAttribute("isAdmin", userValidator.isAdmin(principal.getName()));

        ToDoEntity toDoEntity = toDoService.findById(todo_id);
        List<CommentEntity> list = commentService.findAll(toDoEntity);
        model.addAttribute("todo", toDoEntity);
        model.addAttribute("list", list);

        return "comments";
    }

    @GetMapping("newComment{id}")
    public String newOne(Model model,
                         Principal principal,
                         @RequestParam(value = "id") long todo_id) {

        ToDoEntity toDoEntity = toDoService.findById(todo_id);
        if(! userValidator.isTodoMember(principal.getName(), toDoEntity)) {
            return "redirect:/schedule/comments?id=" + todo_id;
        }

        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setToDoEntity(toDoService.findById(todo_id));
        commentEntity.setUserEntity(userService.findUserByUsername(principal.getName()));
        commentEntity.setDateTime(LocalDateTime.now());

        model.addAttribute("comment", commentEntity);

        return "comment";
    }

    @GetMapping("editComment{id}")
    public String edit(Model model,
                       Principal principal,
                       @RequestParam(value = "id") long id) {

        CommentEntity commentEntity = commentService.getById(id);
        commentEntity.setUserEntity(userService.findUserByUsername(principal.getName()));

        //TODO изменить может только создатель комментария

        commentEntity.setDateTime(LocalDateTime.now());

        model.addAttribute("comment", commentEntity);

        return "comment";
    }

    @PostMapping("saveComment")
    public String save(@ModelAttribute("todo") @Valid CommentEntity commentEntity,
                       BindingResult bindingResult,
                       Principal principal) {

        //TODO изменить может только создатель комментария

        if(bindingResult.hasErrors()) {
            System.out.println("comment save Errors");
            for(FieldError err : bindingResult.getFieldErrors()) {
                System.out.println(err.toString());
            }
            return "comment";
        }
        //TODO проверить права
        commentService.save(commentEntity);
        //TODO отправить уведомление по почте
        return "redirect:/schedule/comments?id=" + commentEntity.getToDoEntity().getId();
    }

}
