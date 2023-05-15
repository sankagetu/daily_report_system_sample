package com.techacademy.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.techacademy.service.ReportService;
import com.techacademy.service.UserDetail;

@Controller
public class IndexController {
    private final ReportService service;

    public IndexController(ReportService service) {

        this.service = service;

    }

    @GetMapping("/")
    public String index(@AuthenticationPrincipal UserDetail userDetail,Model model) {

        model.addAttribute("reportlist", service.getEmployeeReportList(userDetail.getEmployee()));
        model.addAttribute("size", service.getEmployeeReportList(userDetail.getEmployee()).size());

        return "index";
    }
}
