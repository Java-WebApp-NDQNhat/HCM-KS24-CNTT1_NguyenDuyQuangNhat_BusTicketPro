package com.re.trans_route.controller;

import com.re.trans_route.context.UserContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/", ""})
public class HomeController {
    @GetMapping
    public String home() {
        if (UserContext.currentUser != null) {
            return "redirect:/user/dashboard";
        }
        return "redirect:/auth/login";
    }
}
