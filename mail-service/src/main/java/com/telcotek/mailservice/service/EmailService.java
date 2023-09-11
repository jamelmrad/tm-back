package com.telcotek.mailservice.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    RestTemplate restTemplate;

    public void sendEmailVerification(String to, String subject, String text) {

        String userServiceUrl = "http://localhost:8084/api/users/verify";

        // authentication.getName(); // This gets the email of the authenticated user

        // Build the URL with request parameters
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(userServiceUrl)
                .queryParam("email",to);

        // Make the Get request to the user service
        Boolean isVerified =  restTemplate.getForObject(builder.toUriString(), Boolean.class);
        if (!isVerified) {
            try {
                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setText(text, true); // true indicates HTML content


                // Create a Thymeleaf context to populate the template
                Context context = new Context();
                //context.setVariable("name", name);
                //context.setVariable("message", message);

                // Process the HTML template with Thymeleaf
                String htmlContent = templateEngine.process("send-email",context);

                // Set the HTML content as the email body
                helper.setText(htmlContent, true);

                javaMailSender.send(mimeMessage);
            } catch (MessagingException e) {
                e.printStackTrace();
                // Handle exception
            }
        } else {
            try {
                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setText(text, true); // true indicates HTML content


                // Create a Thymeleaf context to populate the template
                Context context = new Context();
                //context.setVariable("name", name);
                //context.setVariable("message", message);

                // Process the HTML template with Thymeleaf
                String htmlContent = templateEngine.process("blank",context);

                // Set the HTML content as the email body
                helper.setText(htmlContent, true);

                javaMailSender.send(mimeMessage);
            } catch (MessagingException e) {
                e.printStackTrace();
                // Handle exception
            }
        }


    }
}

