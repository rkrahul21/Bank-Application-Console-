import service.BankService;
import service.impl.BankserviceImplementation;

import java.util.Scanner;

public class Main {

    public static  void openAccount(Scanner sc , BankService bankService){
//        System.out.println();
        System.out.print("Customer name : ");
        String name = sc.nextLine().trim() ;

        System.out.print("Customer email : ");
        String email = sc.nextLine().trim() ;

        System.out.println("Account Type (SAVINGS/CURRENT) : ");
        String type = sc.nextLine().trim() ;

        System.out.println("Initial deposit (optional, blank for 0) : ");
        String amountStr  = sc.nextLine().trim() ;
        Double initial = Double.valueOf(amountStr);

        String  createdAccount= bankService.openAccount(name,email,type,initial) ;
        if(initial > 0){
            bankService.deposit(createdAccount,initial,"Initial Deposit") ;
        }
        System.out.println("Account Created : "+ createdAccount);

    }

    private static void deposit(Scanner sc , BankService bankService) {
        System.out.println("Enter Account Number :");
        String accountNumber = sc.nextLine().trim() ;

        System.out.println("Amount :");
        Double amount = sc.nextDouble();

        bankService.deposit(accountNumber,amount,"Deposit") ;
        System.out.println("Deposited: ");
    }

    private static void withdraw(Scanner sc, BankService bankService) {
        System.out.println("Account Number :");
        String accountNumber = sc.nextLine().trim() ;
        System.out.println("Amount:");
        Double amount = Double.valueOf(sc.nextLine().trim());

        bankService.withdraw(accountNumber , amount ,"withdrawal") ;

        System.out.println("Withdrawn");
    }


    private static void transfer(Scanner sc ,BankService bankService) {
        System.out.println("From Account :");
        String From = sc.nextLine().trim() ;

        System.out.println("To Account");
        String ToAccount = sc.nextLine().trim() ;

        System.out.println("Amount");
        Double amount = Double.valueOf(sc.nextLine().trim()) ;

        bankService.transfer(From, ToAccount,amount,"Transfer");

    }


    private static void statement(Scanner sc,BankService bankService) {
        System.out.println("Account Number :");
        String accountNumber = sc.nextLine().trim() ;
        bankService.getStatement(accountNumber).forEach(t -> {
            System.out.println(t.getTimestamp() +" | "+ t.getType() +" | "+ t.getAmount() + " | " + t.getNote());
        }); ;


    }

    private static void listAccount(Scanner sc, BankService bankService) {
        // return list from BankServiceImplementaion file

        bankService.listAccounts().forEach(a -> {
            System.out.println(a.getAccountNumber() + " | "+a.getAccountType() +" | "+a.getBalance());
        });
    }

    private static void searchAccounts(Scanner sc,BankService bankService) {
        System.out.println("Customer name contains :");
        String q =  sc.nextLine().trim() ;
        bankService.searchAccountsByCustomerName(q).forEach(account ->
                System.out.println(account.getAccountNumber()+" | "+account.getAccountType()+" | "+account.getBalance()));
    }


    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in) ;

        BankService bankService = new BankserviceImplementation() ;

        boolean running = true;
        System.out.println("Welcome to Bank Console!");

        while(running) {
            System.out.println("""
                    1) Open Account
                    2) Deposit
                    3) Withdraw
                    4) Transfer
                    5) Account Statement
                    6) List Accounts
                    7) Search Account by Customer Name
                    0) Exit
                    """);

            System.out.print("Choose : ");
            String choice = sc.nextLine().trim();
//            System.out.print("Option choose is : " + choice);

            switch (choice){
                case "1" -> openAccount(sc,bankService) ;

                case "2" ->deposit(sc,bankService) ;
                case "3" ->withdraw(sc,bankService) ;
                case "4" ->transfer(sc,bankService) ;
                case "5" ->statement(sc,bankService) ;
                case "6" ->listAccount(sc,bankService) ;
                case "7" ->searchAccounts(sc,bankService) ;


                case "0" -> running = false ;

            }

        }

    }


}