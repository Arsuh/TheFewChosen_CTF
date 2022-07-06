package com.spring4shell.app;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class IndexController {
    @GetMapping("/")
    public String getMethod(Model model) {
        model.addAttribute("postModel", new PostModel());
        return "index";
    }

    @PostMapping("/")
    public String postMethod(@ModelAttribute PostModel postModel, Model model) {
        return "index";
    }

}