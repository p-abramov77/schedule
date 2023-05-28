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
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Controller
@RequestMapping("/schedule")
public class CalendarController {
    @Autowired
    UserValidator userValidator;
    @Autowired
    ProjectServiceImpl projectService;
    private LocalDate tryToConvert(String dateString) {
        LocalDate date;
        if (!dateString.equals("")) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                formatter = formatter.withLocale(Locale.US);
                date = LocalDate.parse(dateString, formatter);
            } catch (Exception e) {
                System.out.println(dateString + " exception: " + e.getMessage());
                date = LocalDateTime.now().toLocalDate();
            }
        } else {
            date = LocalDateTime.now().toLocalDate();
        }
        return date;
    }

    @GetMapping("minusMonth")
    public String minusMonth(Model model,
                            @RequestParam String dateString) {
        LocalDate date = tryToConvert(dateString);
        date = date.minusMonths(1);

        return "redirect:/schedule/month?dateString=" + date.toString();
    }

    @GetMapping("plusMonth")
    public String plusMonth(Model model,
                            @RequestParam String dateString) {
        LocalDate date = tryToConvert(dateString);
        date = date.plusMonths(1);

        return "redirect:/schedule/month?dateString=" + date.toString();
    }

    @GetMapping("month{dateString}")
    public String month(Model model,
                        Principal principal,
                        @RequestParam(defaultValue = "") String dateString) {

        LocalDate date = tryToConvert(dateString);

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
//                System.out.println(w + " " + l + ' ' + cells[w][l]);
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
