package org.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ================= BASIC DETAILS =================

    @Column(name = "transaction_id", nullable = false)
    private String transactionId;

    @Column(name = "txn_timestamp")
    private String timestamp;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "currency")
    private String currency;

    @Column(name = "sender_account")
    private String senderAccount;

    @Column(name = "receiver_account")
    private String receiverAccount;

    @Column(name = "transaction_type")
    private String transactionType;

    @Column(name = "channel")
    private String channel;

    @Column(name = "device_id")
    private String deviceId;

    @Column(name = "location")
    private String location;   // mapped from country

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "success_status")
    private Boolean successStatus;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "user_mobile")
    private String userMobile;

    // ================= FRAUD DETAILS =================

    @Column(name = "fraud_status")
    private String fraudStatus;

    @Column(name = "risk_score")
    private Integer riskScore;

    @Column(name = "fraud_reasons")
    private String fraudReasons;

    // ================= BANKING STATUS =================

    @Column(name = "status")
    private String status;        // SUCCESS / FAILED / PENDING

    @Column(name = "status_reason")
    private String statusReason;

    // ================= ML DETAILS =================

    @Column(name = "ml_prediction")
    private Integer mlPrediction;   // 0 / 1

    @Column(name = "ml_probability")
    private Double mlProbability;   // 0.0 – 1.0

    public Transaction() {}

    // ================= GETTERS & SETTERS =================

    public Long getId() { return id; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getSenderAccount() { return senderAccount; }
    public void setSenderAccount(String senderAccount) { this.senderAccount = senderAccount; }

    public String getReceiverAccount() { return receiverAccount; }
    public void setReceiverAccount(String receiverAccount) { this.receiverAccount = receiverAccount; }

    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }

    public String getChannel() { return channel; }
    public void setChannel(String channel) { this.channel = channel; }

    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public Boolean getSuccessStatus() { return successStatus; }
    public void setSuccessStatus(Boolean successStatus) { this.successStatus = successStatus; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getUserMobile() { return userMobile; }
    public void setUserMobile(String userMobile) { this.userMobile = userMobile; }

    public String getFraudStatus() { return fraudStatus; }
    public void setFraudStatus(String fraudStatus) { this.fraudStatus = fraudStatus; }

    public Integer getRiskScore() { return riskScore; }
    public void setRiskScore(Integer riskScore) { this.riskScore = riskScore; }

    public String getFraudReasons() { return fraudReasons; }
    public void setFraudReasons(String fraudReasons) { this.fraudReasons = fraudReasons; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getStatusReason() { return statusReason; }
    public void setStatusReason(String statusReason) { this.statusReason = statusReason; }

    public Integer getMlPrediction() { return mlPrediction; }
    public void setMlPrediction(Integer mlPrediction) { this.mlPrediction = mlPrediction; }

    public Double getMlProbability() { return mlProbability; }
    public void setMlProbability(Double mlProbability) { this.mlProbability = mlProbability; }
}
