package com.re.trans_route.controller.user_controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "page/admin/dashboard";
    }

    @GetMapping("/fleet")
    public String fleetManagement() {
        return "fragments/admin/bus-management";
    }

    @GetMapping("/fleet/add")
    public String addBusForm() {
        return "fragments/admin/bus-add-form";
    }

    @GetMapping("/routes")
    public String routePlanning() {
        return "fragments/admin/route-planning";
    }

    @GetMapping("/staff")
    public String staffDirectory() {
        return "fragments/admin/staff-directory";
    }
}
