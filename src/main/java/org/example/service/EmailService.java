package org.example.service;

import org.example.model.Transaction;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendFraudAlert(Transaction tx) {

        if (tx.getUserEmail() == null || tx.getUserEmail().isBlank()) {
            System.out.println("⚠ Email not sent: User email missing");
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(tx.getUserEmail());
            message.setSubject("🚨 FRAUD ALERT – Suspicious Transaction Detected");

            message.setText(
                    "Dear Customer,\n\n" +
                            "A transaction has been flagged as FRAUD.\n\n" +
                            "Transaction ID : " + tx.getTransactionId() + "\n" +
                            "Amount         : " + tx.getAmount() + " " + tx.getCurrency() + "\n" +
                            "Risk Score     : " + tx.getRiskScore() + "\n" +
                            "Location       : " + tx.getLocation() + "\n" +
                            "Channel        : " + tx.getChannel() + "\n\n" +
                            "If this was NOT you, contact the bank immediately.\n\n" +
                            "Regards,\n" +
                            "Digital Banking Security Team"
            );

            mailSender.send(message);
            System.out.println("✅ Fraud alert email sent to " + tx.getUserEmail());

        } catch (Exception e) {
            System.out.println("❌ Email sending failed");
            e.printStackTrace();
        }
    }
}
