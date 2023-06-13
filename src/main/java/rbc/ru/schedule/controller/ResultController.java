package rbc.ru.schedule.controller;

import org.springdoc.core.SpringDocUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import rbc.ru.schedule.entity.ResultEntity;
import rbc.ru.schedule.entity.ToDoEntity;
import rbc.ru.schedule.service.ResultServiceImpl;
import rbc.ru.schedule.service.ToDoServiceImpl;
import rbc.ru.schedule.service.UserServiceImpl;
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

    @GetMapping("results{id}")
    public String list(Model model, Principal principal,
                       @RequestParam(value = "todo_id") Long todo_id) {

        model.addAttribute("userName", principal.getName());
        model.addAttribute("isAdmin", userValidator.isAdmin(principal.getName()));

        ToDoEntity toDoEntity = todoService.findById(todo_id);
        Set<ResultEntity> list = resultService.findByTodoId(todo_id);
        model.addAttribute("todo", toDoEntity);
        model.addAttribute("list", list);

        return "results";
    }

    @GetMapping("newResult{todo_id}")
    public String newOne(Model model,
                         Principal principal,
                         @RequestParam(value = "todo_id") Long todo_id) {

        ToDoEntity toDoEntity = todoService.findById(todo_id);

        if(! userValidator.isTodoExecutor(principal.getName(), toDoEntity)) { //TODO  EXECUTOR???
            return "redirect:/schedule/results?id=" + todo_id;
        }

        ResultEntity resultEntity = new ResultEntity();

        resultEntity.setTodo(todoService.findById(todo_id));
        resultEntity.setDateTime(LocalDateTime.now());
        resultEntity.setApproved(false);

        model.addAttribute("result", resultEntity);

        return "result";
    }

    @GetMapping("editResult{id}")
    public String edit(Model model,
                       Principal principal,
                       @RequestParam(value = "id") Long id) {

        ResultEntity resultEntity = resultService.findById(id);

        //TODO изменить может только создатель

        resultEntity.setDateTime(LocalDateTime.now());
        resultEntity.setApproved(false);

        model.addAttribute("result", resultEntity);

        return "result";
    }

    @PostMapping("saveResult")
    public String save(@ModelAttribute("todo") @Valid ResultEntity resultEntity,
                       BindingResult bindingResult,
                       Principal principal) {

        //TODO изменить может только создатель

        if(bindingResult.hasErrors()) {
            System.out.println("comment save Errors");
            for(FieldError err : bindingResult.getFieldErrors()) {
                System.out.println(err.toString());
            }
            return "result";
        }

        //TODO проверить права
        resultService.save(resultEntity);
        //TODO отправить уведомление по почте
        return "redirect:/schedule/results?todo_id=" + resultEntity.getTodo().getId();
    }
    @GetMapping("approveResult{id}")
    public String approve(Model model,
                          Principal principal,
                          @RequestParam(value = "id") Long id) {

        ResultEntity resultEntity = resultService.findById(id);

        if(resultEntity.getApproved() == null) resultEntity.setApproved(false);

        //TODO изменить может только Producer
        //TODO проверить права
        resultEntity.setApproved(!resultEntity.getApproved());
        resultService.save(resultEntity);
        //TODO отправить уведомление по почте
        return "redirect:/schedule/results?todo_id=" + resultEntity.getTodo().getId();
    }

}
