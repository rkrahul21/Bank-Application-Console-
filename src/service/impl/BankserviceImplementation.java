package service.impl;

import domain.Account;
import domain.Customer;
import domain.Transaction;
import domain.Type;
import exception.AccountNotFoundException;
import exception.InsufficientFundsException;
import exception.ValidationException;
import repository.AccountRepository;
import repository.CustomerRepository;
import repository.TransactionRepository;
import service.BankService;
import utils.Validation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


//Main body start here
public class BankserviceImplementation implements BankService {


    private final AccountRepository accountRepository = new AccountRepository() ;
    private final TransactionRepository transactionRepository = new TransactionRepository() ;
    private final CustomerRepository customerRepository = new CustomerRepository() ;


    private final Validation<String> validateName = name -> {
        if (name == null || name.isBlank()) throw new ValidationException("Name is required");
    };

    private final Validation<String> validateEmail = email -> {
        if (email == null || !email.contains("@")) throw new ValidationException("Email is required");
    };

    private final Validation<String> validateType = type -> {
        if (type == null || !(type.equalsIgnoreCase("SAVINGS") || type.contains("CURRENT")))
            throw new ValidationException("Type must be SAVINGS or CURRENT");
    };

    private final Validation<Double> validateAmountPositive = amount -> {
        if (amount == null || amount < 0)
            throw new ValidationException("Please enter valid amount");
    };
    @Override
    public String openAccount(String name ,String email, String accountType,Double initial) {

        validateName.validate(name);
        validateEmail.validate(email);
        validateType.validate(accountType);
       //CREATE CUSTOMER
        String customerId = UUID.randomUUID().toString() ;
        Customer c = new Customer(customerId,name,email) ;
        customerRepository.save(c) ;

       // CREATE ACCOUNT NUMBER
        String accountNumber = getAccountNumber();
        Account account = new Account(accountNumber,accountType,initial,customerId) ;

        //SAVE
        accountRepository.save(account);

        return accountNumber;
//        return "account number please check" ;
    }

    private String getAccountNumber() {
        int size  = accountRepository.findAll().size() +1;
        return String.format("AC%06d",size);
    }


    @Override
    public List<Account> listAccounts() {
        return accountRepository.findAll().stream()
                .sorted(Comparator.comparing(Account::getAccountNumber))
                .collect(Collectors.toList());
    }

    @Override
    public void deposit(String accountNumber , Double amount, String note) {
        validateAmountPositive.validate(amount);
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account Not Found ?" + accountNumber)) ;

//        System.out.println(account.getBalance() + " " + account.getAccountType());
            account.setBalance(account.getBalance()+amount);

        Transaction transaction = new Transaction(account.getAccountNumber() ,amount ,
                UUID.randomUUID().toString(), note ,LocalDateTime.now(), Type.DEPOSIT ) ;

            transactionRepository.add(transaction) ;
    }

    @Override
    public void withdraw(String accountNumber, Double amount, String withdrawal) {
        validateAmountPositive.validate(amount);

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account Not Found ?" + accountNumber)) ;

        Double amountCurrent = account.getBalance() ;

        if(amountCurrent.compareTo(amount) < 0){
            throw new InsufficientFundsException("Your Balance is not Sufficient to Withdraw ") ;
        }

        account.setBalance(account.getBalance()-amount);

        Transaction transaction = new Transaction(account.getAccountNumber() ,amount ,
                UUID.randomUUID().toString(), withdrawal ,LocalDateTime.now(), Type.WITHDRAW ) ;

        transactionRepository.add(transaction) ;
    }

    @Override
    public void transfer(String from, String toAccount, Double amount , String transfer) {

        validateAmountPositive.validate(amount);
        
        if (from.equals(toAccount)){
            throw new RuntimeException("Cannot transfer to your own account") ;
        }
        Account account = accountRepository.findByAccountNumber(from)
                .orElseThrow(() -> new AccountNotFoundException("Account Not Found ?" + from)) ;


        if(account.getBalance().compareTo(amount) < 0){
            throw new InsufficientFundsException("Your Balance is not Sufficient to Transfer ") ;
        }

        Account account2 = accountRepository.findByAccountNumber(toAccount)
                .orElseThrow(() -> new AccountNotFoundException("Account Not Found ?" + toAccount)) ;

        account.setBalance(account.getBalance()-amount);
        account2.setBalance(account.getBalance()+amount);

        Transaction transaction = new Transaction(account.getAccountNumber() ,amount ,
                UUID.randomUUID().toString(), "Debited" ,LocalDateTime.now(), Type.TRANSFER_OUT ) ;

        transactionRepository.add(transaction) ;

        Transaction transaction2 = new Transaction(account2.getAccountNumber() ,amount ,
                UUID.randomUUID().toString(), "Credited" ,LocalDateTime.now(), Type.TRANSFER_IN ) ;

        transactionRepository.add(transaction2) ;

    }

    @Override
    public List<Transaction> getStatement(String accountNumber) {

        return  transactionRepository.getStatement(accountNumber).stream()
                .sorted(Comparator.comparing(Transaction::getTimestamp)).collect(Collectors.toList());
    }

    @Override
    public List<Account> searchAccountsByCustomerName(String q) {
        String User = (q== null ? " " : q.toLowerCase() ) ;
        List<Account> result = new ArrayList<>() ;
        for(Customer c:customerRepository.findAll()){
            if (c.getName().toLowerCase().contains(User)){
                result.addAll(accountRepository.findByCustomerId(c.getId())) ;
            }
        }

        result.sort(Comparator.comparing(Account::getAccountNumber));

        return result ;
    }


}
