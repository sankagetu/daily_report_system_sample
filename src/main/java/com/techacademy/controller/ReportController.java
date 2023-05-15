package com.techacademy.controller;

import java.time.LocalDateTime;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.techacademy.entity.Report;

import com.techacademy.service.ReportService;
import com.techacademy.service.UserDetail;

@Controller
@RequestMapping("report")
public class ReportController {

    private final ReportService service;
    public ReportController(ReportService service) {

        this.service = service;
    }
        /** 一覧画面を表示 */
    @GetMapping("/list")
    public String getList(Model model) {

        // 全件検索結果をModelに登録
        model.addAttribute("reportlist", service.getReportList());
        model.addAttribute("size", service.getReportList().size());
        return "report/list";
    }
    /**日報の登録画面を表示 */
    @GetMapping("/register")
    public String getRegister(@AuthenticationPrincipal UserDetail userDetail,@ModelAttribute Report report) {
        report.setEmployee(userDetail.getEmployee());
        // 登録画面に遷移
        return "report/register";
    }
    /*日報の登録処理 */
    @PostMapping("/register")
    public String postRegister(@AuthenticationPrincipal UserDetail userDetail,Report report) {
        report.setEmployee(userDetail.getEmployee());
        if(report.getReportDate() == null ||
                report.getTitle().equals("") ||
                report.getContent().equals("")) {
            return "report/register";
        }

        report.setEmployee(userDetail.getEmployee());
        LocalDateTime date = LocalDateTime.now();
        report.setUpdated_at(date);
        report.setCreated_at(date);

        service.saveReport(report);
        return "redirect:/report/list";
    }

    /** User更新画面を表示 */
    @GetMapping("/detail/{id}/")
    public String getReport(@AuthenticationPrincipal UserDetail userDetail,@PathVariable("id") Integer code, Model model) {

        Report report = service.getReport(code);
        int flag = 0;
        if(report.getEmployee().getId() == userDetail.getEmployee().getId()) {
            flag = 1;
        }
        model.addAttribute("flag",flag);
        // Modelに登録
        model.addAttribute("report", service.getReport(code));
        // User更新画面に遷移
        return "report/detail";
    }

    /** User更新処理 */
    @PostMapping("/report/list/")
    public String postReport(Report report) {
        // User登録
        service.saveReport(report);
        // 一覧画面にリダイレクト
        return "redirect:/report/list";
    }
    // ----- 追加:ここまで -----
    /** User更新画面を表示 */
    @GetMapping("/update/{id}/")
    public String getReport(@PathVariable("id") Integer id, Model model,@Validated Report report, BindingResult res) {

        if(id != null) {
            Report tableReport = service.getReport(id);
            model.addAttribute("report", tableReport);
        }else {
            model.addAttribute("report",report);
        }

        return "report/update";
    }

    /** report更新処理 */
    @PostMapping("/update/{id}/")
    public String postReport(@AuthenticationPrincipal UserDetail userDetail,@PathVariable("id") Integer id, @Validated Report report, BindingResult res, Model model) {

        report.setEmployee(userDetail.getEmployee());
        if(report.getReportDate() == null ||
                report.getTitle().equals("") ||
                report.getContent().equals("")) {
            return "report/update";
        }

        Report tableReport = service.getReport(id);
        LocalDateTime date = LocalDateTime.now();
        tableReport.setUpdated_at(date);
        tableReport.setReportDate(report.getReportDate());
        tableReport.setTitle(report.getTitle());
        tableReport.setContent(report.getContent());

        //User登録
        service.saveReport(tableReport);
     // 一覧画面にリダイレクト
        return "redirect:/report/list";
    }


}