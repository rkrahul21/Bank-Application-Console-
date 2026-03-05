package repository;

import domain.Account;

import java.util.*;

public class AccountRepository {
    private  final Map<String, Account> accountByNumber = new HashMap<>() ;

    public void save(Account account){
        accountByNumber.put(account.getAccountNumber() , account) ;
    }
    public List<Account> findAll(){

        return new ArrayList<>(accountByNumber.values()) ;
    }

    public Optional<Account> findByAccountNumber(String accountNumber){
        return Optional.ofNullable(accountByNumber.get(accountNumber)) ;
    }

    public List<Account> findByCustomerId(String customerId) {

        List<Account> result = new ArrayList<>() ;
        for (Account ac : accountByNumber.values()){
            if(ac.getCustomerId().equals(customerId)){
                result.add(ac) ;
            }
        }

        return result ;
    }
}
