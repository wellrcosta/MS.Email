package com.ms.email.repositories;

import com.ms.email.enums.ReportType;
import com.ms.email.enums.StatusEmail;
import com.ms.email.models.EmailModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface IEmailRepository extends JpaRepository<EmailModel, Long> {
    Iterable<EmailModel> findAllByStatusEmail(StatusEmail status);

    Iterable<EmailModel> findAllBySendDateEmailBetween(LocalDateTime startDate, LocalDateTime endDate);
}
