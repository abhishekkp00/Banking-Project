package app;

import service.BankService;
import service.impl.BankServiceimpl;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BankService bankservice = new BankServiceimpl();
        boolean running = true;
        System.out.println("--------------------------------------------------");
        System.out.println("Welcome to the Application");
        System.out.println("-------------------------------------------------");


        while(running) {
            System.out.println("""
                    1) Open Account
                    2) Deposit
                    3) Withdraw
                    4) Transfer
                    5) Account Statement
                    6) List Account
                    7) Search Accounts by Customer Name
                    0) Exit
                    """);
            System.out.println("----------------------------------------");
            System.out.println("Enter your choice: ");
            String choice = sc.nextLine().trim();
            System.out.println("You have selected: " + choice);

            switch(choice) {
                case "1" -> openAccount(sc, bankservice);
                case "2" -> deposit(sc, bankservice);
                case "3" -> withdraw(sc);
                case "4" -> transfer(sc);
                case "5" -> statement(sc);
                case "6" -> listAccounts(sc, bankservice);
                case "7" -> searchAccount(sc);                
                case "0" -> running = false;
            }
        }
    }

    private static void openAccount(Scanner sc, BankService bankService) {
        System.out.println("Customer Name");
        String name = sc.nextLine().trim();
        System.out.println("Customer email");
        String email = sc.nextLine().trim();
        System.out.println("Account Type (SAVINGS/CURRENT)");
        String type = sc.nextLine().trim();
        System.out.println("Initial deosit (optional, blank for 0)");
        String amountStr = sc.nextLine().trim();
        Double initial = Double.valueOf(amountStr);
        bankService.openAccount(name, email, type);
        String accountNumber = bankService.openAccount(name, email, type);
        if(initial > 0){
            bankService.deposit(accountNumber, initial, "INITIAL DEPOSIT");
        }
        System.out.println("Account Opened: " + accountNumber);

    }

    private static void deposit(Scanner sc, BankService bankService) {
        System.out.println("Account number: ");
        String accountNumber = sc.nextLine().trim();
        System.out.println("Amount: ");
        Double amount = Double.valueOf(sc.nextLine().trim());
        bankService.deposit(accountNumber, amount, "Deposit");
        System.out.println("Deposited");



    }

    private static void withdraw(Scanner sc) {
    }

    private static void transfer(Scanner sc) {
    }

    private static void statement(Scanner sc) {
    }

    private static void listAccounts(Scanner sc, BankService bankService) {
        bankService.listAccounts().forEach(a ->{
            System.out.println(a.getAccountNumber() + " | " + a.getAccountType() + " | " + a.getBalance());
        });

    }

    private static void searchAccount(Scanner sc) {
    }
}
