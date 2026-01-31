package org.example.securityservice.service;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    public void sendSimpleEmail(String to, String subject, String text) {
        log.info("[MOCK EMAIL] To: {}, Subject: {}, Body: {}", to, subject, text);
    }

    public void sendHtmlEmail(String to, String subject, String htmlContent) {
        log.info("[MOCK HTML EMAIL] To: {}, Subject: {}, HTML: {}", to, subject, htmlContent);
    }
}