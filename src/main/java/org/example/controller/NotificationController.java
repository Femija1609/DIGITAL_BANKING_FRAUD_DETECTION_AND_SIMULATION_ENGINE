package org.example.controller;

import org.example.repository.TransactionRepository;
import org.example.service.TransactionService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:5173")
public class NotificationController {

    private final TransactionRepository repository;
    private final TransactionService transactionService;

    public NotificationController(TransactionRepository repository,
                                  TransactionService transactionService) {
        this.repository = repository;
        this.transactionService = transactionService;
    }
//update
    // ==========================================
    // 🔔 GET UNREAD NOTIFICATION COUNTS (FINAL FIX)
    // ==========================================
    @GetMapping("/count")
    public Map<String, Long> getNotificationCounts() {

        return Map.of(
                // 🟢 Transactions inbox → ALL unseen emails
                "transactions",
                repository.countByEmailSentTrueAndEmailSeenFalse(),

                // 🔴 Fraud Alerts inbox → ONLY FRAUD
                "fraud",
                repository.countByFraudStatusAndEmailSentTrueAndEmailSeenFalse("FRAUD"),

                // 🟡 High Risk inbox → ONLY SUSPICIOUS
                "highRisk",
                repository.countByFraudStatusAndEmailSentTrueAndEmailSeenFalse("SUSPICIOUS")
        );
    }

    // ==========================================
    // ✅ MARK NOTIFICATIONS AS SEEN (BY TYPE)
    // ==========================================
    @PostMapping("/mark-seen")
    public void markSeen(@RequestParam String type) {
        transactionService.markAlertsAsSeen(type);
    }
}
