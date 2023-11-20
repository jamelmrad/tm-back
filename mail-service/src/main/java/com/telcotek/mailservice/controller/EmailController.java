package com.telcotek.mailservice.controller;

import com.telcotek.mailservice.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/mailing")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/mod-verif")
    public void sendEmailVerificationForModerator(
            @RequestParam("destination") String email,
            @RequestParam("subject") String subject,
            @RequestParam("random-link-identifier") String linkIdentifier
    ) {
        String text = "This is the email body.";

        emailService.sendEmailVerificationForModerator(email, subject, text,linkIdentifier);
    }

    @PostMapping("/send-email")
    public void sendEmailVerificationForClient(
            @RequestParam("destination") String email,
            @RequestParam("subject") String subject,
            @RequestParam("random-link-identifier") String linkIdentifier
    ) {
        String text = "This is the email body.";

        emailService.sendEmailVerificationForClient(email, subject, text,linkIdentifier);
    }
}
