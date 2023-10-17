package com.ms.email.controllers;

import com.ms.email.dtos.EmailDto;
import com.ms.email.enums.StatusEmail;
import com.ms.email.models.EmailModel;
import com.ms.email.repositories.IEmailRepository;
import com.ms.email.services.EmailService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmailController {
    final
    EmailService emailService;

    final
    IEmailRepository emailRepository;

    private static final Logger Logger = LoggerFactory.getLogger(EmailController.class);

    public EmailController(EmailService emailService, IEmailRepository emailRepository) {
        this.emailService = emailService;
        this.emailRepository = emailRepository;
    }

    @PostMapping("/send-single")
    public ResponseEntity<EmailModel> sendingEmail(@RequestBody @Valid EmailDto emailDto) {
        EmailModel emailModel = new EmailModel();
        BeanUtils.copyProperties(emailDto, emailModel);
        emailService.sendEmail(emailModel);
        var email = ResponseEntity.created(null).body(emailModel);
        Logger.info("Email sent successfully! emailId=" + emailModel.getEmailId());
        return email;
    }

    @GetMapping("/info/{status}")
    public ResponseEntity<Iterable<EmailModel>> getEmails(@PathVariable StatusEmail status) {
        Iterable<EmailModel> emails = emailRepository.findAllByStatusEmail(status);
        Logger.info("Emails retrieved successfully");
        return ResponseEntity.ok(emails);
    }
}
