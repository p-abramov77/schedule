package rbc.ru.schedule.controller;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import rbc.ru.schedule.entity.GroupEntity;
import rbc.ru.schedule.entity.ProjectEntity;
import rbc.ru.schedule.entity.ToDoEntity;
import rbc.ru.schedule.service.GroupServiceImpl;
import rbc.ru.schedule.service.ProjectServiceImpl;
import rbc.ru.schedule.validator.UserValidator;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/schedule")
public class CalendarController {
    @Autowired
    UserValidator userValidator;
    @Autowired
    ProjectServiceImpl projectService;
    @Autowired
    GroupServiceImpl groupService;

    @GetMapping("month{dateString}{oneGroup}{group_id}")
    public String month(Model model,
                        Principal principal,
                        @RequestParam(name = "oneGroup", defaultValue = "") String justOneGroup,
                        @RequestParam(name = "group_id", defaultValue = "1") String groupIdString,
                        @RequestParam(name = "dateString", defaultValue = "") String dateString
    ) {

        LocalDate date;
        try {
            date = LocalDate.parse(dateString);
        } catch (DateTimeParseException e) {
            date = LocalDate.now();
        }

        Boolean oneGroup = Boolean.parseBoolean(justOneGroup); // return "false" when can't parse
        FilterParamDTO filterParam = new FilterParamDTO();
        filterParam.setOneGroup(oneGroup);

        Long group_id;
        try {
            group_id = Long.parseLong(groupIdString);
        } catch (NumberFormatException e) {
            group_id = 1L;
        }

        if (oneGroup)
            filterParam.setGroup(groupService.getById(group_id));
        else
            filterParam.setGroup(groupService.getById(1L));

        filterParam.setDate(date);


        LocalDate d = date.minusDays(date.getDayOfMonth() - 1); // first day of the current month
        d = d.minusDays(d.getDayOfWeek().getValue() - 1); // found first Monday before the first day of the month

        LocalDateTime start = d.atStartOfDay();
        LocalDateTime stop = d.plusDays(7 * 5).atStartOfDay();
        CellDTO[][] cells = new CellDTO[5][7];

        Set<ProjectEntity> projectsInPeriod = projectService.findInPeriod(start, stop); //TODO add filter by group

        for (int l = 0; l < 7; l++) {
            for (int w = 0; w < 5; w++) {
                LocalDate day = d.plusDays(w * 7 + l);
                cells[w][l] = new CellDTO();
                cells[w][l].setDate(day);
                cells[w][l].setList(projectService.findInDay(day, projectsInPeriod));
            }
        }

        model.addAttribute("userName", principal.getName());
        model.addAttribute("isAdmin", userValidator.isAdmin(principal.getName()));

        model.addAttribute("cells", cells);
        model.addAttribute("groups", groupService.getByName(""));
        model.addAttribute("filterParam", filterParam);

        return "month";
    }

    @PostMapping("monthParam")
    public String param(Model model,
                        @ModelAttribute("filterParam") FilterParamDTO filterParam) {

        if (filterParam.getOneGroup()) {
            return "redirect:/schedule/month?dateString=" + filterParam.getDate().toString() + "&oneGroup=true&group_id=" + filterParam.getGroup().getId();
        }
        return "redirect:/schedule/month?dateString=" + filterParam.getDate().toString() + "&oneGroup=false";
    }

    @GetMapping("minusMonth")
    public String minusMonth(Model model,
                             @RequestParam String dateString) {
        LocalDate date = LocalDate.parse(dateString);
        date = date.minusMonths(1);

        return "redirect:/schedule/month?dateString=" + date.toString();
    }

    @GetMapping("plusMonth")
    public String plusMonth(Model model,
                            @RequestParam String dateString) {
        LocalDate date = LocalDate.parse(dateString);
        date = date.plusMonths(1);

        return "redirect:/schedule/month?dateString=" + date.toString();
    }
}
@Data
class CellDTO {
    LocalDate date;
    Set<ProjectEntity> list;
    public CellDTO() {
        this.list= new HashSet<>();
    }
}

@Data
class FilterParamDTO {
    LocalDate date;
    Boolean oneGroup;
    GroupEntity group;

    @Override
    public String toString() {
        return "FilterParamDTO{" +
                "date=" + date +
                ", oneGroup=" + oneGroup +
                ", group=" + group.getName() +
                '}';
    }
}

