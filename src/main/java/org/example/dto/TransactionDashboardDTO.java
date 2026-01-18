package org.example.dto;

import org.example.model.Transaction;

public class TransactionDashboardDTO {

    private Long id;
    private String transactionId;
    private String timestamp;

    private String senderAccount;
    private String receiverAccount;

    private Double amount;
    private String currency;

    private String channel;
    private String location;
    private String transactionType;
    private String deviceId;
    private String ipAddress;

    private String status;
    private String statusReason;

    private String fraudStatus;
    private Integer riskScore;

    // ✅ NEW: FRAUD / HIGH RISK REASON
    private String fraudReasons;

    private Integer mlPrediction;
    private Double mlProbability;

    // ================= EMAIL =================
    private Boolean emailSent;

    public TransactionDashboardDTO(Transaction tx) {
        this.id = tx.getId();
        this.transactionId = tx.getTransactionId();
        this.timestamp = tx.getTimestamp();

        this.senderAccount = tx.getSenderAccount();
        this.receiverAccount = tx.getReceiverAccount();
        this.amount = tx.getAmount();
        this.currency = tx.getCurrency();

        this.channel = tx.getChannel();
        this.location = tx.getLocation();
        this.transactionType = tx.getTransactionType();
        this.deviceId = tx.getDeviceId();
        this.ipAddress = tx.getIpAddress();

        this.status = tx.getStatus();
        this.statusReason = tx.getStatusReason();

        this.fraudStatus = tx.getFraudStatus();
        this.riskScore = tx.getRiskScore();

        // ✅ MAP REASON FROM ENTITY
        this.fraudReasons = tx.getFraudReasons();

        this.mlPrediction = tx.getMlPrediction();
        this.mlProbability = tx.getMlProbability();

        this.emailSent = tx.getEmailSent();
    }

    // ===== GETTERS =====
    public Long getId() { return id; }
    public String getTransactionId() { return transactionId; }
    public String getTimestamp() { return timestamp; }
    public String getSenderAccount() { return senderAccount; }
    public String getReceiverAccount() { return receiverAccount; }
    public Double getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public String getChannel() { return channel; }
    public String getLocation() { return location; }
    public String getTransactionType() { return transactionType; }
    public String getDeviceId() { return deviceId; }
    public String getIpAddress() { return ipAddress; }
    public String getStatus() { return status; }
    public String getStatusReason() { return statusReason; }
    public String getFraudStatus() { return fraudStatus; }
    public Integer getRiskScore() { return riskScore; }

    // ✅ NEW GETTER
    public String getFraudReasons() { return fraudReasons; }

    public Integer getMlPrediction() { return mlPrediction; }
    public Double getMlProbability() { return mlProbability; }
    public Boolean getEmailSent() { return emailSent; }
}
