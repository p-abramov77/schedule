package rbc.ru.schedule.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import rbc.ru.schedule.entity.EquipmentEntity;
import rbc.ru.schedule.service.EquipmentServiceImpl;

import javax.validation.Valid;
import java.util.Set;

@Controller
@RequestMapping("/schedule")
public class EquipmentController {
    @Autowired
    private EquipmentServiceImpl equipmentService;

    @GetMapping("equipments")
    public String list(Model model, @RequestParam(defaultValue = "") String name) {
        Set<EquipmentEntity> list = equipmentService.getByName(name);

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
        if(bindingResult.hasErrors()) {
            return "equipment";
        }
        if(!equipmentService.save(equipment)) {
            model.addAttribute("errorMessage", "Оборудование с указанным именем существует");
            return "equipment";
        }

        return "redirect:/schedule/equipments?name=" + equipment.getName();

    }
    @GetMapping("editEquipment/{id}")
    public String edit(Model model, @PathVariable(value = "id") long id) {
        EquipmentEntity equipmentEntity = equipmentService.getById(id);
        model.addAttribute("equipment", equipmentEntity);
        return "equipment";
    }
}
