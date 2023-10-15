package com.ms.email.services;

import com.ms.email.enums.ReportType;
import com.ms.email.enums.StatusEmail;
import com.ms.email.models.EmailModel;
import com.ms.email.repositories.IEmailRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportService {
    public Workbook generateReport(Iterable<EmailModel> emails) {
        // Create a new Excel sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Email report");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("OwnerRef");
        headerRow.createCell(2).setCellValue("Email From");
        headerRow.createCell(3).setCellValue("Email To");
        headerRow.createCell(4).setCellValue("Subject");
        headerRow.createCell(5).setCellValue("Text");
        headerRow.createCell(6).setCellValue("Send Date Email");
        headerRow.createCell(7).setCellValue("Status Email");

        int rowNum = 1;
        for (EmailModel email : emails) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(email.getEmailId());
            row.createCell(1).setCellValue(email.getOwnerRef());
            row.createCell(2).setCellValue(email.getEmailFrom());
            row.createCell(3).setCellValue(email.getEmailTo());
            row.createCell(4).setCellValue(email.getSubject());
            row.createCell(5).setCellValue(email.getText());
            row.createCell(6).setCellValue(email.getSendDateEmail().toString());
            row.createCell(7).setCellValue(email.getStatusEmail().toString());
        }
        return workbook;
    }
}
