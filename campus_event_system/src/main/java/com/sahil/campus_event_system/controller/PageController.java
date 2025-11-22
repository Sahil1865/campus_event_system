package com.sahil.campus_event_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String homePage() {
        return "index";
    }



    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/create-event")
    public String createEventPage() {
        return "create-event";
    }

    @GetMapping("/my-registrations")
    public String myRegistrationsPage() {
        return "my-registrations";
    }
}
