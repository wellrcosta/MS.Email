package com.ms.email.controllers;

import com.ms.email.enums.ReportType;
import com.ms.email.enums.StatusEmail;
import com.ms.email.models.EmailModel;
import com.ms.email.repositories.IEmailRepository;
import com.ms.email.services.ReportService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/report")
public class ReportController {
    private final IEmailRepository emailRepository;

    public ReportController(IEmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    @GetMapping("/search")
    public ResponseEntity<byte[]> searchEmails (
            HttpServletRequest response,
            @RequestParam(value = "reportType") ReportType reportType,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) throws IllegalArgumentException {

        Iterable<EmailModel> emails;


        try {
            switch (reportType) {
                case BY_DATE -> {
                    if (startDate == null || endDate == null) {
                        throw new IllegalArgumentException("Start date and end date are required");
                    }
                    emails = emailRepository.findAllBySendDateEmailBetween(startDate, endDate);
                }
                case BY_STATUS_SENT -> emails = emailRepository.findAllByStatusEmail(StatusEmail.SENT);
                case BY_STATUS_ERROR -> emails = emailRepository.findAllByStatusEmail(StatusEmail.ERROR);
                case ALL -> emails = emailRepository.findAll();
                default -> throw new IllegalStateException("Unexpected value: " + reportType);
            }
            Workbook workbook;
            try {
                workbook = new ReportService().generateReport(emails);
            } catch (Exception e) {
                throw new IllegalArgumentException("Error generating report");
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();

            HttpHeaders headers = new HttpHeaders();
            LocalDateTime now = LocalDateTime.now();
            var filename = String.format("email_report_%s.xlsx", now.toString());
            headers.add("Content-Disposition", "attachment; filename=" + filename);

            // Convert to byte array
            byte[] excelContent = outputStream.toByteArray();

            return new ResponseEntity<>(excelContent, headers, HttpStatus.OK);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error generating report: " + e.getMessage());
        }

    }
}
