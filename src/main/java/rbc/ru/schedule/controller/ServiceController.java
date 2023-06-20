package rbc.ru.schedule.controller;

import org.springdoc.core.SpringDocUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/schedule")
public class ServiceController {

    static {
        SpringDocUtils.getConfig().addRestControllers(ServiceController.class);
    }
    @GetMapping("/alive")
    public String alive(){

        return "alive";
    }

    @GetMapping("/notifying")
    public String notifying(){
        // TODO уведомление всех пользователей о задачах (вызывается снаружи заданием для системы Cron)
        return "notifying";
    }
}
