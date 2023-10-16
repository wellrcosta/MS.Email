package com.ms.email;

import com.ms.email.controllers.ReportController;
import com.ms.email.enums.ReportType;
import com.ms.email.enums.StatusEmail;
import com.ms.email.models.EmailModel;
import com.ms.email.repositories.IEmailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ReportControllerTests {
    private ReportController reportController;
    private IEmailRepository emailRepository;

    @BeforeEach
    public void setup() {
        emailRepository = Mockito.mock(IEmailRepository.class);
        reportController = new ReportController(emailRepository);
    }

    @Test
    public void testSearchEmailsHappyPath() {
        // Mock data for the happy path test
        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        LocalDateTime endDate = LocalDateTime.now();
        List<EmailModel> mockEmails = Collections.singletonList(new EmailModel());

        // Mocking repository responses
        when(emailRepository.findAllBySendDateEmailBetween(startDate, endDate)).thenReturn(mockEmails);

        // Perform the test
        ResponseEntity<byte[]> response = reportController.searchEmails(null, ReportType.ALL, null, null);

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testSearchEmailsErrorPath() {
        // Mocking a scenario where an error occurs in generating the report
        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        LocalDateTime endDate = LocalDateTime.now();

        // Mocking repository responses
        when(emailRepository.findAllBySendDateEmailBetween(startDate, endDate)).thenThrow(new IllegalArgumentException("Error generating report"));

        // Perform the test and capture the exception
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reportController.searchEmails(null, ReportType.BY_DATE, startDate, endDate);
        });

        // Verify the exception message
        assertEquals("Error generating report: Error generating report", exception.getMessage());
    }
}
