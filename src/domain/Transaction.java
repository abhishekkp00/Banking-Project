package domain;

import java.time.LocalDateTime;

public class Transaction {
    private String id;
    private Type type;
    private String accountNumber;
    private Double amount;
    private LocalDateTime timestamp;
    private String note;


    public Transaction(String id, String note, LocalDateTime timestamp, Double amount, String accountNumber, Type type) {
        this.id = id;
        this.note = note;
        this.timestamp = timestamp;
        this.amount = amount;
        this.accountNumber = accountNumber;
        this.type = type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Transaction(String accountNumber, Double amount, String string, String note, LocalDateTime now, Type type) {
    }
}
