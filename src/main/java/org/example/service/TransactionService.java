package org.example.service;

import org.example.model.Transaction;
import org.example.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class TransactionService {

    private final TransactionRepository repository;
    private final EmailService emailService;
    private final MLClientService mlClientService;

    // Fraud thresholds
    private static final double HIGH_VALUE_THRESHOLD = 50000.0;
    private static final double NIGHT_AMOUNT_THRESHOLD = 30000.0;

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    // Constructor
    public TransactionService(TransactionRepository repository,
                              EmailService emailService,
                              MLClientService mlClientService) {
        this.repository = repository;
        this.emailService = emailService;
        this.mlClientService = mlClientService;
    }

    // ==========================================================
    // MAIN TRANSACTION PROCESSOR
    // ==========================================================
    public Transaction evaluateAndSave(Transaction tx) {

        List<String> reasons = new ArrayList<>();
        int score = 0;

        // Ensure timestamp
        if (tx.getTimestamp() == null || tx.getTimestamp().isBlank()) {
            tx.setTimestamp(LocalDateTime.now().format(formatter));
        }

        // Ensure transactionId
        if (tx.getTransactionId() == null || tx.getTransactionId().isBlank()) {
            tx.setTransactionId(UUID.randomUUID().toString());
        }

        // =========================
        // RULE-BASED FRAUD LOGIC
        // =========================

        if (tx.getAmount() != null && tx.getAmount() > HIGH_VALUE_THRESHOLD) {
            reasons.add("HIGH_VALUE");
            score += 30;
        }

        if (tx.getLocation() != null && tx.getCurrency() != null) {

            String loc = tx.getLocation().toLowerCase();
            String cur = tx.getCurrency().toUpperCase();

            boolean indiaUsd = cur.equals("USD") && loc.contains("india");
            boolean usaInr = cur.equals("INR") &&
                    (loc.contains("usa") || loc.contains("new york"));

            if (indiaUsd || usaInr) {
                reasons.add("CURRENCY_LOCATION_MISMATCH");
                score += 20;
            }
        }

        try {
            LocalDateTime dt = LocalDateTime.parse(tx.getTimestamp(), formatter);
            int hour = dt.getHour();

            if (hour <= 4 &&
                    tx.getAmount() != null &&
                    tx.getAmount() > NIGHT_AMOUNT_THRESHOLD) {
                reasons.add("NIGHT_HIGH_AMOUNT");
                score += 20;
            }
        } catch (Exception ignored) {}

        // =========================
        // FRAUD STATUS DECISION
        // =========================
        if (reasons.isEmpty()) {
            tx.setFraudStatus("OK");
            tx.setRiskScore(0);
            tx.setFraudReasons(null);
        } else {
            score = Math.min(score, 100);
            tx.setRiskScore(score);
            tx.setFraudReasons(String.join(",", reasons));
            tx.setFraudStatus(score >= 60 ? "FRAUD" : "SUSPICIOUS");
        }

        // =========================
        // BANKING STATUS LOGIC
        // =========================
        tx.setStatus("PENDING");

        boolean valid =
                tx.getAmount() != null &&
                        tx.getAmount() > 0 &&
                        tx.getSenderAccount() != null &&
                        tx.getReceiverAccount() != null &&
                        !tx.getSenderAccount().equals(tx.getReceiverAccount());

        if (!valid) {
            tx.setStatus("FAILED");
            tx.setStatusReason("Invalid Transaction Data");
        } else {
            tx.setStatus("SUCCESS");
            tx.setStatusReason(null);
        }

        // =========================
        // ML FRAUD PREDICTION
        // =========================
        try {
            Map<String, Object> mlInput = new HashMap<>();

            mlInput.put("amount", tx.getAmount());
            mlInput.put("currency", tx.getCurrency());
            mlInput.put("hour",
                    LocalDateTime.parse(tx.getTimestamp()).getHour());
            mlInput.put("country",
                    tx.getLocation() != null &&
                            tx.getLocation().contains("USA") ? "USA" : "India");
            mlInput.put("transactionType", tx.getTransactionType());
            mlInput.put("channel", tx.getChannel());
            mlInput.put("deviceChanged", 0);
            mlInput.put("failedAttempts",
                    "FAILED".equals(tx.getStatus()) ? 1 : 0);
            mlInput.put("isNight",
                    LocalDateTime.parse(tx.getTimestamp()).getHour() <= 4 ? 1 : 0);

            Map<String, Object> mlResult =
                    mlClientService.predictFraud(mlInput);

            tx.setMlPrediction(
                    Integer.parseInt(mlResult.get("fraud_prediction").toString())
            );
            tx.setMlProbability(
                    Double.parseDouble(mlResult.get("fraud_probability").toString())
            );

        } catch (Exception e) {
            tx.setMlPrediction(null);
            tx.setMlProbability(null);
        }

        // =========================
        // SAVE
        // =========================
        Transaction saved = repository.save(tx);

        // =========================
        // EMAIL ALERT
        // =========================
        if ((saved.getFraudStatus().equals("FRAUD")
                || saved.getFraudStatus().equals("SUSPICIOUS"))
                && saved.getStatus().equals("SUCCESS")
                && saved.getUserEmail() != null
                && !saved.getUserEmail().isBlank()) {

            emailService.sendFraudAlert(saved);
        }

        return saved;
    }

    // ==========================================================
    // ✅ FILTER (FIXED)
    // ==========================================================
    public List<Transaction> filterData(
            String sender,
            String receiver,
            String fraudStatus,
            String status
    ) {
        return repository.findAll().stream()
                .filter(t -> sender == null || sender.isBlank()
                        || t.getSenderAccount().equalsIgnoreCase(sender))
                .filter(t -> receiver == null || receiver.isBlank()
                        || t.getReceiverAccount().equalsIgnoreCase(receiver))
                .filter(t -> fraudStatus == null || fraudStatus.isBlank()
                        || t.getFraudStatus().equalsIgnoreCase(fraudStatus))
                .filter(t -> status == null || status.isBlank()
                        || t.getStatus().equalsIgnoreCase(status))
                .toList();
    }

    // ==========================================================
    // ✅ SUMMARY (NO REPOSITORY METHODS NEEDED)
    // ==========================================================
    public Map<String, Long> summary() {

        List<Transaction> list = repository.findAll();

        long total = list.size();
        long success = list.stream()
                .filter(t -> "SUCCESS".equals(t.getStatus())).count();
        long failed = list.stream()
                .filter(t -> "FAILED".equals(t.getStatus())).count();
        long pending = list.stream()
                .filter(t -> "PENDING".equals(t.getStatus())).count();

        long fraud = list.stream()
                .filter(t -> "FRAUD".equals(t.getFraudStatus())).count();
        long suspicious = list.stream()
                .filter(t -> "SUSPICIOUS".equals(t.getFraudStatus())).count();

        return Map.of(
                "total", total,
                "success", success,
                "failed", failed,
                "pending", pending,
                "fraud", fraud,
                "suspicious", suspicious
        );
    }
}
