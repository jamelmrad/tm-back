package com.telcotek.mailservice.controller;

import com.telcotek.mailservice.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send-email")
    public void sendEmail(
            @RequestParam("destination") String email,
            @RequestParam("subject") String subject
    ) {
        String text = "This is the email body.";

        emailService.sendEmail(email, subject, text);
    }
}
