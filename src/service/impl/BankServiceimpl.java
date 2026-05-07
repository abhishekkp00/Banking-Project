package service.impl;

import domain.Account;
import domain.Transaction;
import domain.Type;
import repository.AccountRepository;
import repository.TransactionRepository;
import service.BankService;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TransferQueue;
import java.util.stream.Collectors;

public class BankServiceimpl implements BankService {

    private final AccountRepository accountRepository= new AccountRepository();
    private final TransactionRepository transactionRepository= new TransactionRepository();

    @Override
    public String openAccount(String name, String email, String accountType){
        String customerId = UUID.randomUUID().toString(); //generated random id

        String accountNumber = getAccountNumber();

        Account ac = new Account(accountNumber, customerId, 0.0, accountType);

        //Saving the account data in object (not database for now)
        accountRepository.save(ac);

        return accountNumber;
    }

    @Override
    public List<Account> listAccounts() {
        return accountRepository.findAll().stream()
                .sorted(Comparator.comparing(Account::getAccountNumber))
                .collect(Collectors.toList());
    }

    @Override
    public void deposit(String accountNumber, Double amount, String note) {
        Account account = accountRepository.findByNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account Not Found: "+ accountNumber));
        account.setBalance(account.getBalance() + amount);
        Transaction transaction = new Transaction(account.getAccountNumber(),
                amount, UUID.randomUUID().toString(), note, LocalDateTime.now(), Type.DEPOSIT);
        transactionRepository.add(transaction);
    }

    @Override
    public void Withdraw(String accountNumber, Double amount, String note) {
        Account account = accountRepository.findByNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account Not Found: "+ accountNumber));
        if(account.getBalance().compareTo(amount) < 0)
            throw new RuntimeException("Insufficient Balance.");
        account.setBalance(account.getBalance() - amount);
        Transaction transaction = new Transaction(account.getAccountNumber(),
                amount, UUID.randomUUID().toString(), note, LocalDateTime.now(), Type.WITHDRAW);
        transactionRepository.add(transaction);
    }

    private String getAccountNumber() {
        int size = accountRepository.findAll().size() + 1;
        //6-digit account number trailing
        return String.format("AC%06d" , size);
    }
}
