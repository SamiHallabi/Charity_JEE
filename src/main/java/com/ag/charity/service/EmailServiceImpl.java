package com.ag.charity.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.math.BigDecimal;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendWelcomeEmail(String to, String name) {
        String subject = "Bienvenue sur Charity !";
        String body = "<h2>Bienvenue, " + name + " !</h2>"
                + "<p>Votre compte a été créé avec succès. Rejoignez des actions caritatives dès maintenant.</p>";
        sendHtml(to, subject, body);
    }

    @Override
    public void sendDonationConfirmation(String to, BigDecimal amount, String actionTitle) {
        String subject = "Confirmation de votre don";
        String body = "<h2>Merci pour votre générosité !</h2>"
                + "<p>Votre don de <strong>" + amount + " MAD</strong> à l'action <strong>"
                + actionTitle + "</strong> a bien été reçu.</p>";
        sendHtml(to, subject, body);
    }

    @Override
    public void sendOrgApprovalEmail(String to, String orgName, boolean approved, String reason) {
        String subject = approved ? "Votre organisation a été approuvée" : "Votre organisation a été rejetée";
        String body = approved
                ? "<h2>Félicitations !</h2><p>L'organisation <strong>" + orgName + "</strong> a été approuvée.</p>"
                : "<h2>Votre demande a été refusée.</h2><p>Motif : " + (reason != null ? reason : "Non précisé") + "</p>";
        sendHtml(to, subject, body);
    }

    @Override
    public void sendActionUpdateEmail(String to, String actionTitle, String update) {
        String subject = "Mise à jour : " + actionTitle;
        String body = "<h2>Mise à jour de l'action</h2><p><strong>" + actionTitle + "</strong> : " + update + "</p>";
        sendHtml(to, subject, body);
    }

    private void sendHtml(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            mailSender.send(message);
        } catch (Exception e) {
            // Log but don't fail the business operation
            System.err.println("Échec de l'envoi de l'email à " + to + " : " + e.getMessage());
        }
    }
}
