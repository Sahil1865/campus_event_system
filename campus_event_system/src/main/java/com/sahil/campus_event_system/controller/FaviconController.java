package com.sahil.campus_event_system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FaviconController {
    @GetMapping("favicon.ico")
    public void favicon() {
        // do nothing, just ignore favicon requests
    }
}
