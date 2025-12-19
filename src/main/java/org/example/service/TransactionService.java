package org.example.service;

import org.example.model.Transaction;
import org.example.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
// commit test line

@Service
public class TransactionService {

    private final TransactionRepository repository;
    private final EmailService emailService;

    private static final double HIGH_VALUE_THRESHOLD = 50000.0;
    private static final double NIGHT_AMOUNT_THRESHOLD = 30000.0;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public TransactionService(TransactionRepository repository, EmailService emailService) {
        this.repository = repository;
        this.emailService = emailService;
    }

    public Transaction evaluateAndSave(Transaction tx) {

        List<String> reasons = new ArrayList<>();
        int score = 0;

        if (tx.getTimestamp() == null || tx.getTimestamp().isBlank()) {
            tx.setTimestamp(LocalDateTime.now().format(formatter));
        }

        if (tx.getTransactionId() == null || tx.getTransactionId().isBlank()) {
            tx.setTransactionId(UUID.randomUUID().toString());
        }

        if (tx.getAmount() != null && tx.getAmount() > HIGH_VALUE_THRESHOLD) {
            reasons.add("HIGH_VALUE");
            score += 30;
        }

        if (tx.getLocation() != null && tx.getCurrency() != null) {

            String loc = tx.getLocation().toLowerCase();
            String cur = tx.getCurrency().toUpperCase();

            boolean india_USD = cur.equals("USD") && loc.contains("india");
            boolean usa_INR = cur.equals("INR") &&
                    (loc.contains("usa") || loc.contains("new york"));

            if (india_USD || usa_INR) {
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

        if (reasons.isEmpty()) {
            tx.setFraudStatus("OK");
            tx.setRiskScore(0);
            tx.setFraudReasons(null);

        } else {

            score = Math.min(score, 100);
            tx.setRiskScore(score);
            tx.setFraudReasons(String.join(",", reasons));

            if (score >= 60) {
                tx.setFraudStatus("FRAUD");
            } else {
                tx.setFraudStatus("SUSPICIOUS");
            }
        }

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

        Transaction saved = repository.save(tx);

        if ((saved.getFraudStatus().equals("FRAUD") ||
                saved.getFraudStatus().equals("SUSPICIOUS"))
                &&
                saved.getStatus().equals("SUCCESS")
                &&
                saved.getUserEmail() != null &&
                !saved.getUserEmail().isBlank()) {

            emailService.sendFraudAlert(saved);
        }

        return saved;
    }


    // =======================================================================
    // FILTER API IMPLEMENTATION (NULL SAFE)
    // =======================================================================
    public List<Transaction> filterData(
            String sAcc,
            String rAcc,
            String fStatus,
            String tStatus
    ) {

        return repository.findAll().stream()

                .filter(t -> sAcc == null ||
                        (t.getSenderAccount() != null &&
                                t.getSenderAccount().equalsIgnoreCase(sAcc)))

                .filter(t -> rAcc == null ||
                        (t.getReceiverAccount() != null &&
                                t.getReceiverAccount().equalsIgnoreCase(rAcc)))

                .filter(t -> fStatus == null ||
                        (t.getFraudStatus() != null &&
                                t.getFraudStatus().equalsIgnoreCase(fStatus)))

                .filter(t -> tStatus == null ||
                        (t.getStatus() != null &&
                                t.getStatus().equalsIgnoreCase(tStatus)))

                .toList();
    }


    // =======================================================================
    // SUMMARY API IMPLEMENTATION
    // =======================================================================
    public Map<String, Long> summary() {

        List<Transaction> list = repository.findAll();

        long total = list.size();
        long success = list.stream().filter(t -> "SUCCESS".equals(t.getStatus())).count();
        long failed = list.stream().filter(t -> "FAILED".equals(t.getStatus())).count();
        long pending = list.stream().filter(t -> "PENDING".equals(t.getStatus())).count();
        long fraud = list.stream().filter(t -> "FRAUD".equals(t.getFraudStatus())).count();
        long suspicious = list.stream().filter(t -> "SUSPICIOUS".equals(t.getFraudStatus())).count();

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
