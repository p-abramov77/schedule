package rbc.ru.schedule.controller;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import rbc.ru.schedule.entity.ProjectEntity;
import rbc.ru.schedule.service.ProjectServiceImpl;
import rbc.ru.schedule.validator.UserValidator;

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

    @GetMapping("month{dateString}")
    public String month(Model model,
                        Principal principal,
                        @RequestParam(defaultValue = "") String dateString) {

        LocalDate date;
        try {
            date = LocalDate.parse(dateString);
        } catch (DateTimeParseException e) {
            date = LocalDate.now();
        }

        LocalDate[][] list = new LocalDate[5][7];
        LocalDate d = date.minusDays(date.getDayOfMonth() - 1); // first day of the current month
        d = d.minusDays(d.getDayOfWeek().getValue() - 1); // found first Monday before the first day of the month

        LocalDateTime start = d.atStartOfDay();
        LocalDateTime stop = d.plusDays(7*5).atStartOfDay();
        Cell [][] cells = new Cell[5][7];

        Set<ProjectEntity> allProjectsInPeriod = projectService.findInPeriod(start, stop);

        for (int l = 0; l < 7; l++) {
            for (int w = 0; w < 5; w++) {
                LocalDate day = d.plusDays(w * 7 + l);
                cells[w][l] = new Cell();
                cells[w][l].setDate(day);
                cells[w][l].setList(projectService.findInDay(day, allProjectsInPeriod));
            }
        }
        model.addAttribute("userName", principal.getName());
        model.addAttribute("isAdmin", userValidator.isAdmin(principal.getName()));

        model.addAttribute("date", date);
        model.addAttribute("cells", cells);

        return "month";
    }
}

@Data
class Cell {
    LocalDate date;
    Set<ProjectEntity> list;
    public Cell() {
        this.list= new HashSet<>();
    }
}
