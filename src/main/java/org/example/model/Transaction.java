package org.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transactionId;
    private String timestamp;

    private Double amount;
    private String currency;

    private String senderAccount;
    private String receiverAccount;

    private String transactionType;
    private String channel;

    private String deviceId;
    private String location;
    private String ipAddress;

    private Boolean successStatus;

    private String userEmail;
    private String userMobile;

    // FRAUD FIELDS
    private String fraudStatus;
    private Integer riskScore;
    private String fraudReasons;

    // NEW STATUS FIELDS
    private String status;           // SUCCESS / FAILED / PENDING
    private String statusReason;     // reason if failed

    public Transaction() {}

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

    // NEW GETTERS + SETTERS
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getStatusReason() { return statusReason; }
    public void setStatusReason(String statusReason) { this.statusReason = statusReason; }
}
