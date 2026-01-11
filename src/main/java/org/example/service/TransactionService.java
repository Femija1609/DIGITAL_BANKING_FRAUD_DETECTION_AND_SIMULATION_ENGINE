package org.example.service;

import org.example.dto.TransactionSimulateRequest;
import org.example.model.Transaction;
import org.example.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
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
    // TRANSACTION SIMULATION
    // ============================
    public Transaction simulate(TransactionSimulateRequest req) {

        Transaction tx = new Transaction();

        // ----------------------------
        // BASE DETAILS
        // ----------------------------
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

        // ✅ CORRECT MAPPING
        tx.setLocation(req.getCountry());

        tx.setUserEmail("femijajohn@gmail.com");
        tx.setUserMobile("9999999999");

        // ============================
        // RULE 1: SELF TRANSFER
        // ============================
        if (req.getSenderAccount().equalsIgnoreCase(req.getReceiverAccount())) {
            tx.setStatus("FAILED");
            tx.setFraudStatus("SUSPICIOUS");
            tx.setRiskScore(70);
            tx.setStatusReason("Sender and Receiver accounts cannot be the same");
            tx.setSuccessStatus(false);
            return repository.save(tx);
        }

        // ============================
        // RULE-BASED RISK SCORING
        // ============================
        int riskScore = 0;

        if (req.getAmount() > 50000) riskScore += 30;
        if ("USD".equalsIgnoreCase(req.getCurrency())) riskScore += 40;

        if ("WEB".equalsIgnoreCase(req.getChannel())
                && "CARD".equalsIgnoreCase(req.getTransactionType())) {
            riskScore += 20;
        }

        // ✅ COUNTRY BASED CHECK
        if ("USA".equalsIgnoreCase(req.getCountry())
                || "Unknown".equalsIgnoreCase(req.getCountry())) {
            riskScore += 30;
        }

        // ============================
        // ML PREDICTION (OPTIONAL)
        // ============================
        int mlPrediction = 0;
        double mlProbability = 0.1;

        try {
            HashMap<String, Object> features = new HashMap<>();
            features.put("amount", req.getAmount());
            features.put("currency", req.getCurrency());
            features.put("channel", req.getChannel());
            features.put("transactionType", req.getTransactionType());

            Map<String, Object> ml =
                    mlClientService.predictFraud(features);

            mlPrediction =
                    ((Number) ml.getOrDefault("prediction", 0)).intValue();
            mlProbability =
                    ((Number) ml.getOrDefault("probability", 0.1)).doubleValue();

            if (mlPrediction == 1 && mlProbability >= 0.7) {
                riskScore += 30;
            }

        } catch (Exception ignored) {}

        // ============================
        // FINAL DECISION
        // ============================
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
        }

        tx.setRiskScore(riskScore);
        tx.setFraudStatus(fraudStatus);
        tx.setStatus(status);
        tx.setStatusReason("OK");
        tx.setSuccessStatus(status.equals("SUCCESS"));
        tx.setMlPrediction(fraudStatus.equals("FRAUD") ? 1 : 0);
        tx.setMlProbability(fraudStatus.equals("FRAUD") ? 0.85 : 0.1);

        Transaction saved = repository.save(tx);

        // ============================
        // EMAIL ALERT
        // ============================
        if ("FRAUD".equals(fraudStatus)) {
            emailService.sendFraudAlert(saved);
        }

        return saved;
    }
}
