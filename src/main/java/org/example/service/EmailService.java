package org.example.service;

import org.example.model.Transaction;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    // Constructor Injection
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Sends an alert email when a transaction is
     * marked as SUSPICIOUS or FRAUD.
     */
    public void sendFraudAlert(Transaction tx) {

        SimpleMailMessage message = new SimpleMailMessage();

        // Receiver email
        message.setTo(tx.getUserEmail());

        // Subject
        message.setSubject("⚠️ ALERT: Suspicious Banking Transaction");

        // Email content
        message.setText(
                "Dear Customer,\n\n" +
                        "Your transaction has been flagged as " + tx.getFraudStatus() + ".\n\n" +
                        "Transaction ID : " + tx.getTransactionId() + "\n" +
                        "Amount         : " + tx.getAmount() + " " + tx.getCurrency() + "\n" +
                        "Reason         : " + tx.getFraudReasons() + "\n\n" +
                        "If this was NOT you, please contact the bank immediately.\n\n" +
                        "Regards,\n" +
                        "Digital Banking Security Team"
        );

        // Send email
        mailSender.send(message);
    }
}
