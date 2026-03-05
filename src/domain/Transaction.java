package domain;

import java.time.LocalDateTime;

public class Transaction {
    private String id;
    private Type type ;
    private String accountNumber;
    private Double amount ;
    private LocalDateTime timestamp;
    private String note ;

    public String getAccountNumber() {
        return accountNumber;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public Double getAmount() {
        return amount;
    }

    public String getNote() {
        return note;
    }

    public Transaction(String accountNumber, Double amount, String id, String note , LocalDateTime timestamp, Type type ) {
        this.id = id;
        this.type = type;
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.timestamp = timestamp;
        this.note = note;
    }


}
