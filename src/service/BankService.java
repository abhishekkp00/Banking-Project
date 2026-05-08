package service;

import domain.Account;
import domain.Transaction;

import java.util.List;
import java.util.Map;

public interface BankService {
    String openAccount(String name, String email, String accountType);
    List<Account> listAccounts();
    void deposit(String accountNumber, Double amount, String note);

    void Withdraw(String accountNumber, Double amount, String withdrawn);

    void transfer(String from, String to, Double amount, String transferredSuccessfully);

    List<Transaction> getStatement(String account);

    List<Account> searchAccountsByCustomerName(String q);
}
