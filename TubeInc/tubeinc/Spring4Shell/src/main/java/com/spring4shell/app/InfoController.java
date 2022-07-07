package com.spring4shell.app;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class InfoController {
    @GetMapping("/info")
    public String getMethod(Model model) {
        return "info";
    }
}