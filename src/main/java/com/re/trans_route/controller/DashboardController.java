package com.re.trans_route.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "page/admin/dashboard";
    }

    @GetMapping("/passenger/dashboard")
    public String passengerDashboard() {
        return "page/passenger/dashboard";
    }
}

