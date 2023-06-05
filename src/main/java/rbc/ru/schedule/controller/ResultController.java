package rbc.ru.schedule.controller;

import org.springdoc.core.SpringDocUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import rbc.ru.schedule.entity.ResultEntity;
import rbc.ru.schedule.entity.ToDoEntity;
import rbc.ru.schedule.service.*;
import rbc.ru.schedule.validator.UserValidator;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Set;

@Controller
@RequestMapping("/schedule")
public class ResultController {

    static {
        SpringDocUtils.getConfig().addRestControllers(ResultController.class);
    }

    @Autowired
    ResultServiceImpl resultService;
    @Autowired
    UserValidator userValidator;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    ToDoServiceImpl todoService;

    @GetMapping("results{todo_id}")
    public String list(Model model,
                       Principal principal,
                       @RequestParam(defaultValue = "") Long todo_id) {

        System.out.println("Try to read");
        Set<ResultEntity> list = resultService.findByTodoId(todo_id);
        System.out.println("results ="+ list);

        model.addAttribute("userName", principal.getName());
        model.addAttribute("isAdmin", userValidator.isAdmin(principal.getName()));
        model.addAttribute("isTodoProducer", userValidator.isTodoProducer(principal.getName(), todoService.findById(todo_id)));
        model.addAttribute("isTodoExecutor", userValidator.isTodoExecutor(principal.getName(), todoService.findById(todo_id)));
        model.addAttribute("list", list);

        ToDoEntity todo = todoService.findById(todo_id);
        model.addAttribute("todo", todo);

        return "results";
    }

    @GetMapping("newResult{todo_id}")
    public String newOne(Model model,
                        Principal principal,
                        @RequestParam(defaultValue = "") Long todo_id) {

        ToDoEntity toDoEntity = todoService.findById(todo_id);
        System.out.println("new result " + todo_id + "  TODO = " + toDoEntity);
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setDateTime(LocalDateTime.now());
        resultEntity.setTodo(toDoEntity);
        resultEntity.setApproved(false);

        model.addAttribute("result", resultEntity);
        return "result";
    }

    @PostMapping("saveResult")
    public String save(Model model,
                       Principal principal,
                       @ModelAttribute("result") @Valid ResultEntity resultEntity,
                       BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return "result";
        }
        //TODO проверить права
        resultService.save(resultEntity);
        //TODO по почте сообщить
        return "redirect:/schedule/results?todo_id=" + resultEntity.getTodo().getId();

    }

    @GetMapping("editResult/{id}")
    public String edit(Model model, @PathVariable(value = "id") long id) {

        ResultEntity resultEntity = resultService.findById(id);
        model.addAttribute("result", resultEntity);

        resultEntity.setApproved(false);
        resultEntity.setDateTime(LocalDateTime.now());

        model.addAttribute("result", resultEntity);

        return "result";
    }
    @GetMapping("approveResult/{id}")
    public String approve(Model model, @PathVariable(value = "id") long id) {
        ResultEntity resultEntity = resultService.findById(id);
        model.addAttribute("result", resultEntity);
        return "approve";
    }

}
