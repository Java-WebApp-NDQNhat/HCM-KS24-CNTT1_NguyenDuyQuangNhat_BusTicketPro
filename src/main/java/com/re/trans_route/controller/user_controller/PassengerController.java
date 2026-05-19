package com.re.trans_route.controller.user_controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/passenger")
public class PassengerController {
    @GetMapping("/dashboard")
    public String passengerDashboard() {
        return "page/passenger/dashboard";
    }
}
