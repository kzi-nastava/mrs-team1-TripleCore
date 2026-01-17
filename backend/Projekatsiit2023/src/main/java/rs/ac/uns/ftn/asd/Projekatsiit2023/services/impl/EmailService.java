package rs.ac.uns.ftn.asd.Projekatsiit2023.services.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final String fromEmail;

    public EmailService(JavaMailSender mailSender,
                        @Value("${app.email.from:noreply@carpooling.com}") String fromEmail) {
        this.mailSender = mailSender;
        this.fromEmail = fromEmail;
    }

    public void sendActivationEmail(String toEmail, String activationLink) {
        try {
            SimpleMailMessage message = getAccountActivationMailMessage(toEmail, activationLink);

            mailSender.send(message);
            System.out.println("Email sent to Mailpit: " + toEmail);
            System.out.println("View emails at: http://localhost:8025");

        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());

            System.out.println("\nACTIVATION LINK (Fallback):");
            System.out.println(activationLink);
        }
    }

    private SimpleMailMessage getAccountActivationMailMessage(String toEmail, String activationLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Activate Your Account");

        String text = """
            Dear User,
            Please activate your account by clicking this link:
            %s
            
            This link expires in 24 hours.
            
            If you didn't register, please ignore this email.
            """.formatted(activationLink);

        message.setText(text);
        return message;
    }

    public void sendPasswordResetEmail(String toEmail, String resetLink) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Reset Your Password");

            String text = """
            Dear User,
            
            You have requested to reset your password.
            Click the link below to reset your password:
            %s
            
            This link will expire in 24 hours.
            
            If you didn't request this, please ignore this email.
            """.formatted(resetLink);

            message.setText(text);
            mailSender.send(message);

        } catch (Exception e) {
            System.err.println("Failed to send password reset email: " + e.getMessage());
        }
    }
}
