package com.re.trans_route.controller;

import com.re.trans_route.dto.RegisterDTO;
import com.re.trans_route.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("registerDTO", new RegisterDTO());
        return "/auth/register";
    }

    @PostMapping("/register")
    public String register(RegisterDTO registerDTO, Model model) {
        try {
            authService.register(registerDTO);
            return "redirect:/auth/login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "/auth/register";
        }
    }
}
