package com.re.trans_route.controller.user_controller;

import com.re.trans_route.entity.Route;
import com.re.trans_route.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final RouteService routeService;

    @Autowired
    public AdminController(RouteService routeService) {
        this.routeService = routeService;
    }

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
    public String routePlanning(Model model,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "8") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Route> routePage = routeService.getAllRoutes(pageable);

        model.addAttribute("routePage", routePage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", routePage.getTotalPages());
        model.addAttribute("totalItems", routePage.getTotalElements());

        return "fragments/admin/route-planning";
    }

    @GetMapping("/staff")
    public String staffDirectory() {
        return "fragments/admin/staff-directory";
    }
}
