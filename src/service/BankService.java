package service;

import domain.Account;

import java.util.List;

public interface BankService {
    String openAccount(String name, String email, String accountType);
    List<Account> listAccounts();
    void deposit(String accountNumber, Double amount, String note);

    void Withdraw(String accountNumber, Double amount, String withdrawn);

    void transfer(String from, String to, Double amount, String transferredSuccessfully);
}
