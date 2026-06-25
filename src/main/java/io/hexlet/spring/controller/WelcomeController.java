package io.hexlet.spring.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

    @Value("${app.welcome-message}")
    private String welcomeMessage;

    @Value("${app.admin-email}")
    private String adminEmail;

    @Value("${app.page-size}")
    private Integer pageSize;

    @GetMapping("/welcome")
    public String welcome() {
        return welcomeMessage;
    }

    @GetMapping("/admin-info")
    public String adminInfo() {
        return "Admin email: " + adminEmail + ", Page size: " + pageSize;
    }
}
