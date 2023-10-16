package com.ms.email.controllers;

import com.ms.email.dtos.EmailDto;
import com.ms.email.enums.StatusEmail;
import com.ms.email.models.EmailModel;
import com.ms.email.repositories.IEmailRepository;
import com.ms.email.services.EmailService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmailController {
    final
    EmailService emailService;

    final
    IEmailRepository emailRepository;

    public EmailController(EmailService emailService, IEmailRepository emailRepository) {
        this.emailService = emailService;
        this.emailRepository = emailRepository;
    }

    @PostMapping("/send-single")
    public ResponseEntity<EmailModel> sendingEmail(@RequestBody @Valid EmailDto emailDto) {
        EmailModel emailModel = new EmailModel();
        BeanUtils.copyProperties(emailDto, emailModel);
        emailService.sendEmail(emailModel);
        return ResponseEntity.created(null).body(emailModel);
    }

    @GetMapping("/info/{status}")
    public ResponseEntity<Iterable<EmailModel>> getEmails(@PathVariable StatusEmail status) {
        Iterable<EmailModel> emails = emailRepository.findAllByStatusEmail(status);
        return ResponseEntity.ok(emails);
    }
}
