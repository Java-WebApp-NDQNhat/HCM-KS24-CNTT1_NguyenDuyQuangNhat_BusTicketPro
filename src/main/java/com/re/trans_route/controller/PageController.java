package com.re.trans_route.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping({"/page/tmp", "/page/tmp.html"})
    public String tmpPage() {
        return "page/tmp";
    }
}

