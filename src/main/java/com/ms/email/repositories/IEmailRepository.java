package com.ms.email.repositories;

import com.ms.email.enums.StatusEmail;
import com.ms.email.models.EmailModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEmailRepository extends JpaRepository<EmailModel, Long> {
    Iterable<EmailModel> findAllByStatusEmail(StatusEmail status);
}
