package rbc.ru.schedule.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import rbc.ru.schedule.entity.TagEntity;
import rbc.ru.schedule.service.TagServiceImpl;

import javax.validation.Valid;
import java.util.Set;

@Controller
@RequestMapping("/schedule")
public class TagController {
    @Autowired
    private TagServiceImpl tagService;

    @GetMapping("tags")
    public String list(Model model, @RequestParam(defaultValue = "") String name) {
        Set<TagEntity> list = tagService.getByName(name);

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
        if(!tagService.save(tagEntity)) {
            model.addAttribute("errorMessage", "Тэг с указанным именем существует");
            return "tag";
        }

        return "redirect:/schedule/tags?name=" + tagEntity.getName();

    }
    @GetMapping("editTag/{id}")
    public String edit(Model model, @PathVariable(value = "id") long id) {
        TagEntity tagEntity = tagService.getById(id);
        model.addAttribute("tag", tagEntity);
        return "tag";
    }
}
