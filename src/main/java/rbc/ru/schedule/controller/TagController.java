package rbc.ru.schedule.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import rbc.ru.schedule.entity.TagEntity;
import rbc.ru.schedule.service.TagServiceImpl;
import rbc.ru.schedule.validator.UserValidator;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Principal;
import java.util.Set;

@Controller
@RequestMapping("/schedule")
public class TagController {
    @Autowired
    private TagServiceImpl tagService;
    @Autowired
    UserValidator userValidator;

    @GetMapping("tags")
    public String list(Model model, Principal principal,
                       @RequestParam(defaultValue = "") String name) {
        Set<TagEntity> list = tagService.getByName(name);

        model.addAttribute("userName", principal.getName());
        model.addAttribute("isAdmin", userValidator.isAdmin(principal.getName()));
        model.addAttribute("name", name);
        model.addAttribute("list", list);

        return "tags";
    }
    @GetMapping("newTag")
    public String newOne(Model model) {
        TagEntity tagEntity = new TagEntity();
        model.addAttribute("tag", tagEntity);
        return "tag";
    }
    @PostMapping("saveTag")
    public String save(Model model, @ModelAttribute("tag") @Valid TagEntity tagEntity,
                               BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "tag";
        }
        //TODO проверить права

        if(!tagService.save(tagEntity)) {
            model.addAttribute("errorMessage", "Тэг с указанным именем существует");
            return "tag";
        }

        try {
            return "redirect:/schedule/tags?name=" + URLEncoder.encode(tagEntity.getName(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "redirect:/schedule/tags";
        }
    }
    @GetMapping("editTag/{id}")
    public String edit(Model model, @PathVariable(value = "id") long id) {
        TagEntity tagEntity = tagService.getById(id);
        model.addAttribute("tag", tagEntity);
        return "tag";
    }
}
