package com.techacademy.service;

import java.util.List;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;
import com.techacademy.repository.ReportRepository;

@Service
public class ReportService {
    private final ReportRepository reportRepository;

    public ReportService(ReportRepository repository) {
        this.reportRepository = repository;
    }

    public List<Report> getReportList() {
        return reportRepository.findAll();
    }
    
    @Transactional
    public Report saveReport(Report report) {
        return reportRepository.save(report);
    }

    public Report getReport(Integer id) {
        return reportRepository.findById(id).get();
    }
    
    public List<Report> getEmployeeReportList(Employee employee) {
        return reportRepository.findByEmployee(employee);
    }

    }