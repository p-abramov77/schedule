package rbc.ru.schedule.controller;

import org.springdoc.core.SpringDocUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import rbc.ru.schedule.entity.EquipmentEntity;
import rbc.ru.schedule.service.EquipmentServiceImpl;
import rbc.ru.schedule.validator.UserValidator;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Principal;
import java.util.Set;

@Controller
@RequestMapping("/schedule")
public class EquipmentController {

    static {
        SpringDocUtils.getConfig().addRestControllers(EquipmentController.class);
    }
    @Autowired
    private EquipmentServiceImpl equipmentService;
    @Autowired
    UserValidator userValidator;

    @GetMapping("equipments")
    public String list(Model model, Principal principal,
                       @RequestParam(defaultValue = "") String name) {
        //TODO проверить права
        Set<EquipmentEntity> list = equipmentService.getByName(name);

        model.addAttribute("userName", principal.getName());
        model.addAttribute("isAdmin", userValidator.isAdmin(principal.getName()));
        model.addAttribute("name", name);
        model.addAttribute("list", list);

        return "equipments";


    }
    @GetMapping("newEquipment")
    public String newOne(Model model) {
        EquipmentEntity equipmentEntity = new EquipmentEntity();
        model.addAttribute("equipment", equipmentEntity);
        return "equipment";
    }
    @PostMapping("saveEquipment")
    public String save(Model model, @ModelAttribute("equipments") @Valid EquipmentEntity equipment,
                       BindingResult bindingResult) {
        //TODO проверить права
        if(bindingResult.hasErrors()) {
            return "equipment";
        }
        if(!equipmentService.save(equipment)) {
            model.addAttribute("errorMessage", "Оборудование с указанным именем существует");
            return "equipment";
        }

        try {
            return "redirect:/schedule/equipments?name=" + URLEncoder.encode(equipment.getName(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "redirect:/schedule/equipments";
        }

    }
    @GetMapping("editEquipment/{id}")
    public String edit(Model model, @PathVariable(value = "id") long id) {
        EquipmentEntity equipmentEntity = equipmentService.getById(id);
        model.addAttribute("equipment", equipmentEntity);
        return "equipment";
    }
}
