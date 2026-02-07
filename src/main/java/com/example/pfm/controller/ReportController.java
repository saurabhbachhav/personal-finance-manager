package com.example.pfm.controller;

import com.example.pfm.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

     private final ReportService reportService;

     public ReportController(ReportService reportService) {
          this.reportService = reportService;
     }

     @GetMapping("/monthly/{year}/{month}")
     public ResponseEntity<Map<String, Object>> getMonthlyReport(
               @PathVariable int year,
               @PathVariable int month,
               Authentication authentication) {
          if (month < 1 || month > 12) {
               throw new IllegalArgumentException("Month must be between 1 and 12");
          }
          return ResponseEntity.ok(reportService.getMonthlyReport(authentication.getName(), year, month));
     }

     @GetMapping("/yearly/{year}")
     public ResponseEntity<Map<String, Object>> getYearlyReport(
               @PathVariable int year,
               Authentication authentication) {
          return ResponseEntity.ok(reportService.getYearlyReport(authentication.getName(), year));
     }
}
