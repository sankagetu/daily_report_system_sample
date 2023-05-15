package com.techacademy.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.techacademy.entity.Employee;
import com.techacademy.service.EmployeeService;

@Controller
@RequestMapping("employee")
public class EmployeeController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    /** 一覧画面を表示 */
    @GetMapping("/list")
    public String getList(Model model) {
        // 全件検索結果をModelに登録
        model.addAttribute("employeelist", service.getEmployeeList());
        // employee/list.htmlに画面遷移
        return "employee/list";
    }

    @GetMapping("/detail/{id}/")
    public String getDetail(@PathVariable(name = "id", required = false) Integer id, Model model) {
        // codeが指定されていたら検索結果、無ければ空のクラスを設定
        Employee employee = id != null ? service.getEmployee(id) : new Employee();
        // Modelに登録
        model.addAttribute("employee", employee);
        // country/detail.htmlに画面遷移
        return "employee/detail";
    }

    /** Employee登録画面を表示 */
    @GetMapping("/register")
    public String getRegister(@ModelAttribute Employee employee) {
        // Employee登録画面に遷移
        return "employee/register";
    }

    /** Employee登録処理 */
    @PostMapping("/register")
    public String postRegister(@Validated Employee employee, BindingResult res, Model model) {
        if(res.hasErrors()) {
            // エラーあり
            return getRegister(employee);
        }
        if(employee.getAuthentication().getCode().equals("") ||
                employee.getName().equals("") ||
                employee.getAuthentication().getPassword().equals("") ||
                employee.getAuthentication().getRole() == null) {
            return "employee/register";
        }

        String password = employee.getAuthentication().getPassword();
        employee.getAuthentication().setPassword(passwordEncoder.encode(password));
        employee.getAuthentication().setEmployee(employee);
        employee.setDelete_flag(0);
        employee.setCreated_at(LocalDateTime.now());
        employee.setUpdated_at(LocalDateTime.now());

        try {
        // Employee登録
        service.saveEmployee(employee);
        } catch (Exception e) {
            return "employee/register";
        }
        // 一覧画面にリダイレクト
        return "redirect:/employee/list";
    }

    /** Employee更新画面を表示 */
    @GetMapping("/update/{id}/")
    public String getUpdate(@PathVariable("id") Integer id, Model model, Employee employee) {
        if (id == null) {
            model.addAttribute("employee", employee);
        } else {
         // Modelに登録
            Employee tableEmployee = service.getEmployee(id);
            tableEmployee.getAuthentication().setPassword("");
            model.addAttribute("employee", tableEmployee);
        }

        // Employee更新画面に遷移
        return "employee/update";
    }

    /** Employee更新処理 */
    @PostMapping("/update/{id}/")
    public String postEmployee(@PathVariable("id") Integer id, @Validated Employee employee, BindingResult res, Model model) {
        if(employee.getAuthentication().getCode().equals("") ||
                employee.getName().equals("") ||
                employee.getAuthentication().getRole() == null) {
            return "employee/update";
        }
        if(res.hasErrors()) {
            // エラーあり
            return getUpdate(null, model, employee);
        }
        Employee tableEmployee = service.getEmployee(id);
        if(!employee.getAuthentication().getPassword().equals("")) {
            String password = employee.getAuthentication().getPassword();
            tableEmployee.getAuthentication().setPassword(passwordEncoder.encode(password));
        }
        tableEmployee.setName(employee.getName());
        tableEmployee.getAuthentication().setRole(employee.getAuthentication().getRole());
        tableEmployee.setUpdated_at(LocalDateTime.now());
        // Employee登録
        service.saveEmployee(tableEmployee);
        // 一覧画面にリダイレクト
        return "redirect:/employee/list";
    }

    @GetMapping("/delete/{id}/")
    public String getDelete(@PathVariable("id") Integer id, @Validated Employee employee, BindingResult res, Model model) {

        Employee tableEmployee = service.getEmployee(id);
        tableEmployee.setDelete_flag(1);

        // Employee登録
        service.saveEmployee(tableEmployee);
        // 一覧画面にリダイレクト
        return "redirect:/employee/list";
    }
}
