package service;

import domain.Account;
import domain.Transaction;

import java.util.List;

public interface BankService {

    String openAccount(String name ,String email, String type,Double initial) ;
    List<Account> listAccounts() ;

    void deposit(String accountNumber ,Double amount,String note) ;

    void withdraw(String accountNumber, Double amount, String withdrawal);

    void transfer(String from, String toAccount, Double amount ,String Note);

    List<Transaction> getStatement(String accountNumber);

    List<Account> searchAccountsByCustomerName(String q);
}
