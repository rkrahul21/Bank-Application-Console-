package domain;

public class Account {
    private String accountNumber ;
    private String customerId;
    private Double balance;
    private String accountType ;


    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Account(String accountNumber, String accountType, Double balance , String customerId) {
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.balance = balance;
        this.accountType = accountType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
    public Double getBalance() {
        return balance;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
