package com.reportsaathi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ReportSaathi — Main Application Entry Point
 *
 * @SpringBootApplication is a shortcut for three annotations:
 *   @Configuration      — this class registers beans
 *   @EnableAutoConfiguration — let Spring Boot auto-configure based on classpath
 *   @ComponentScan      — scan all classes in this package and sub-packages
 */
@SpringBootApplication
public class ReportSaathiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReportSaathiApplication.class, args);
        System.out.println("\n" +
            "╔════════════════════════════════════════╗\n" +
            "║    ReportSaathi Backend is Running!    ║\n" +
            "║    Health Check: GET /api/health       ║\n" +
            "╚════════════════════════════════════════╝\n");
    }
}
