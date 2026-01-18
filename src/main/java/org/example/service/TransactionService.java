package org.example.service;

import org.example.dto.TransactionSimulateRequest;
import org.example.model.Transaction;
import org.example.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class TransactionService {

    private final TransactionRepository repository;
    private final MLClientService mlClientService;
    private final EmailService emailService;

    public TransactionService(TransactionRepository repository,
                              MLClientService mlClientService,
                              EmailService emailService) {
        this.repository = repository;
        this.mlClientService = mlClientService;
        this.emailService = emailService;
    }

    // ============================
    // FILTER SUPPORT
    // ============================
    public List<Transaction> filterData(
            String sender,
            String receiver,
            String fraudStatus,
            String status
    ) {
        return repository.findAll()
                .stream()
                .filter(t -> sender == null || sender.isBlank()
                        || sender.equalsIgnoreCase(t.getSenderAccount()))
                .filter(t -> receiver == null || receiver.isBlank()
                        || receiver.equalsIgnoreCase(t.getReceiverAccount()))
                .filter(t -> fraudStatus == null || fraudStatus.isBlank()
                        || fraudStatus.equalsIgnoreCase(t.getFraudStatus()))
                .filter(t -> status == null || status.isBlank()
                        || status.equalsIgnoreCase(t.getStatus()))
                .toList();
    }

    // ============================
    // SUMMARY
    // ============================
    public Map<String, Long> summary() {
        return Map.of(
                "total", repository.count(),
                "success", repository.countByStatus("SUCCESS"),
                "failed", repository.countByStatus("FAILED"),
                "pending", repository.countByStatus("PENDING"),
                "fraud", repository.countByFraudStatus("FRAUD"),
                "suspicious", repository.countByFraudStatus("SUSPICIOUS")
        );
    }

    // ============================
    // 🔔 MARK NOTIFICATIONS AS SEEN
    // ============================
    @Transactional
    public void markAlertsAsSeen(String type) {

        List<Transaction> list = repository.findAll();

        for (Transaction tx : list) {

            if (!Boolean.TRUE.equals(tx.getEmailSent())) continue;
            if (Boolean.TRUE.equals(tx.getEmailSeen())) continue;

            if ("transactions".equalsIgnoreCase(type)
                    && "OK".equalsIgnoreCase(tx.getFraudStatus())) {
                tx.setEmailSeen(true);
            }

            if ("fraud".equalsIgnoreCase(type)
                    && "FRAUD".equalsIgnoreCase(tx.getFraudStatus())) {
                tx.setEmailSeen(true);
            }

            if ("highRisk".equalsIgnoreCase(type)
                    && "SUSPICIOUS".equalsIgnoreCase(tx.getFraudStatus())) {
                tx.setEmailSeen(true);
            }
        }

        repository.saveAll(list);
    }

    // ============================
    // TRANSACTION SIMULATION
    // ============================
    public Transaction simulate(TransactionSimulateRequest req) {

        Transaction tx = new Transaction();

        tx.setTransactionId("SIM-" + System.currentTimeMillis());
        tx.setTimestamp(LocalDateTime.now().toString());

        tx.setSenderAccount(req.getSenderAccount());
        tx.setReceiverAccount(req.getReceiverAccount());
        tx.setAmount(req.getAmount());
        tx.setCurrency(req.getCurrency());
        tx.setChannel(req.getChannel());
        tx.setTransactionType(req.getTransactionType());
        tx.setDeviceId(req.getDeviceId());
        tx.setIpAddress(req.getIpAddress());
        tx.setLocation(req.getCountry());

        tx.setUserEmail("femijajohn@gmail.com");
        tx.setUserMobile("9999999999");

        // ================= SELF TRANSFER =================
        if (req.getSenderAccount().equalsIgnoreCase(req.getReceiverAccount())) {
            tx.setStatus("FAILED");
            tx.setFraudStatus("SUSPICIOUS");
            tx.setRiskScore(70);
            tx.setFraudReasons("Sender and receiver accounts are the same");
            tx.setSuccessStatus(false);

            tx.setEmailSent(true);
            tx.setEmailSeen(false);

            return repository.save(tx);
        }

        // ================= RISK SCORE =================
        int riskScore = 0;
        StringBuilder reason = new StringBuilder();

        if (req.getAmount() > 50000) {
            riskScore += 30;
            reason.append("High transaction amount; ");
        }

        if ("USD".equalsIgnoreCase(req.getCurrency())) {
            riskScore += 40;
            reason.append("Foreign currency transaction; ");
        }

        if ("WEB".equalsIgnoreCase(req.getChannel())
                && "CARD".equalsIgnoreCase(req.getTransactionType())) {
            riskScore += 20;
            reason.append("Web-based card transaction; ");
        }

        if ("USA".equalsIgnoreCase(req.getCountry())
                || "Unknown".equalsIgnoreCase(req.getCountry())) {
            riskScore += 30;
            reason.append("High-risk transaction location; ");
        }

        // ================= ML =================
        try {
            Map<String, Object> ml = mlClientService.predictFraud(
                    Map.of(
                            "amount", req.getAmount(),
                            "currency", req.getCurrency(),
                            "channel", req.getChannel(),
                            "transactionType", req.getTransactionType()
                    )
            );
            if (((Number) ml.getOrDefault("prediction", 0)).intValue() == 1) {
                riskScore += 30;
                reason.append("ML model flagged transaction; ");
            }
        } catch (Exception ignored) {}

        // ================= FINAL DECISION =================
        String fraudStatus;
        String status;

        if (riskScore >= 80) {
            fraudStatus = "FRAUD";
            status = "FAILED";
        } else if (riskScore >= 50) {
            fraudStatus = "SUSPICIOUS";
            status = "PENDING";
        } else {
            fraudStatus = "OK";
            status = "SUCCESS";
            reason.append("Normal transaction");
        }

        tx.setFraudStatus(fraudStatus);
        tx.setStatus(status);
        tx.setRiskScore(riskScore);
        tx.setFraudReasons(reason.toString());
        tx.setSuccessStatus("SUCCESS".equals(status));

        // ================= EMAIL / INBOX =================
        if ("FRAUD".equals(fraudStatus) || "SUSPICIOUS".equals(fraudStatus)) {
            try {
                emailService.sendFraudAlert(tx);
            } catch (Exception ignored) {}
        }

        tx.setEmailSent(true);
        tx.setEmailSeen(false);

        return repository.save(tx);
    }
}
