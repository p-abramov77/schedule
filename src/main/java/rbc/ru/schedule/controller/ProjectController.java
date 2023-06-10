package rbc.ru.schedule.controller;

import org.springdoc.core.SpringDocUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import rbc.ru.schedule.entity.ProjectEntity;
import rbc.ru.schedule.entity.RoleEntity;
import rbc.ru.schedule.entity.UserEntity;
import rbc.ru.schedule.service.*;
import rbc.ru.schedule.validator.UserValidator;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Set;

@Controller
@RequestMapping("/schedule")
public class ProjectController {

    static {
        SpringDocUtils.getConfig().addRestControllers(ProjectController.class);
    }
    @Value("${max-length-of-period}")
    public int maxLengthOfPeriod;

    @Autowired
    private ProjectServiceImpl projectService;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private RoleServiceImpl roleService;
    @Autowired
    private TagServiceImpl tagService;
    @Autowired
    UserValidator userValidator;

    @ModelAttribute("principal")
    public UserEntity getUser(Principal principal) {
        return userService.findUserByUsername(principal.getName());
    }
    @GetMapping("projects")
    public String list(Model model, Principal principal,
                       @RequestParam(name = "tag",  defaultValue = "") String id,
                       @RequestParam(name = "user", defaultValue = "") String user,
                       @RequestParam(name = "name", defaultValue = "") String name,
                       @RequestParam(name = "start", defaultValue = "") String startString,
                       @RequestParam(name = "stop" , defaultValue = "") String stopString
    ) {
        model.addAttribute("userName", principal.getName());
        model.addAttribute("isAdmin", userValidator.isAdmin(principal.getName()));

        String message;
        if(name.isEmpty())
            message = "";

        else
            message  = "Имя события начинается с : " + name;

        LocalDate startDate, stopDate;

        try {
            startDate = LocalDate.parse(startString);
            stopDate = LocalDate.parse(stopString);
        } catch (DateTimeParseException e) {
            startDate = LocalDate.now();
            stopDate = LocalDate.now().plusDays(maxLengthOfPeriod);
            message = "Фильтр: с " + startDate + " по " + stopDate + "; " + message;
            model.addAttribute("start", startDate);
            model.addAttribute("stop", stopDate);
            return "redirect:/schedule/projects?start="+startDate+"&stop="+stopDate;
        }
        if(startDate.isAfter(stopDate)) {
            stopDate = startDate;
            model.addAttribute("start", startDate);
            model.addAttribute("stop", stopDate);
            return "redirect:/schedule/projects?start="+startDate+"&stop="+stopDate;
        }
        if(startDate.until(stopDate, ChronoUnit.DAYS) > maxLengthOfPeriod) {
            stopDate = startDate.plusDays(maxLengthOfPeriod);
            model.addAttribute("start", startDate);
            model.addAttribute("stop", stopDate);
            return "redirect:/schedule/projects?start="+startDate+"&stop="+stopDate;
        }

        model.addAttribute("start", startDate);
        model.addAttribute("stop", stopDate);

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime stop = stopDate.plusDays(1).atStartOfDay();

        Set<ProjectEntity> list = projectService.getByName(name, start, stop, principal.getName());

        if (!id.isEmpty()){
            list = projectService.getByTag(Long.valueOf(id), start, stop, principal.getName());
            message = "Тэг события : " + tagService.getById(Long.valueOf(id)).getName();
        }

        if (!user.isEmpty()){
            list = projectService.getByUsername(user, start, stop, principal.getName());
            message = "События, в которых участвует : " + user;
        }

        message = "Фильтр: с " + startDate + " по " + stopDate + ";  " + message;

        model.addAttribute("message", message);
        model.addAttribute("name", name);
        model.addAttribute("list", list);

        return "projects";
    }
    @GetMapping("newProject")
    public String newOne(Model model,
                         Principal principal) {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setCreator_id(userService.findUserByUsername(principal.getName()).getId());
        Set<String> listNames = userService.listNames();

        model.addAttribute("project", projectEntity);
        model.addAttribute("users", listNames);

        return "project";
    }
    @PostMapping("saveProject")
    public String save( Model model,
                        @ModelAttribute("project") @Valid ProjectEntity projectEntity,
                        BindingResult bindingResult, SessionStatus sessionStatus,
                        Principal principal) {

        if (!projectService.isPeriod(projectEntity)) {
            model.addAttribute("errorMessage", "Начало периода превышает конец периода");
            return "project";
        }

        if(bindingResult.hasErrors()) {
            return "project";
        }
        //TODO проверить права

        // project_id == 0 when newProject was called
        if(projectEntity.getId() == null ) {
            Long project_id = projectService.save(projectEntity);
            projectEntity.setId(project_id);

            RoleEntity roleEntity = new RoleEntity();
            roleEntity.setProducer(true);
            roleEntity.setUser(userService.findUserByUsername(principal.getName()));
            roleEntity.setProject(projectEntity);

            roleService.save(roleEntity);
        } else {
            projectService.save(projectEntity);
        }
        //TODO отправить уведомление по почте


        try {
            return "redirect:/schedule/projects?name=" + URLEncoder.encode(projectEntity.getName(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "redirect:/schedule/projects";
        }


    }
    @GetMapping("editProject/{id}")
    public String edit(Model model,
                       Principal principal,
                       @PathVariable(value = "id") long id) {

        ProjectEntity projectEntity = projectService.getById(id, principal.getName());
        model.addAttribute("project", projectEntity);
        return "project";
    }

}
