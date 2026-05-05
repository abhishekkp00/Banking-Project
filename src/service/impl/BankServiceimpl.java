package service.impl;

import domain.Account;
import repository.AccountRepository;
import service.BankService;

import java.util.UUID;

public class BankServiceimpl implements BankService {

    private final AccountRepository accountRepository= new AccountRepository();

    @Override
    public String openAccount(String name, String email, String accountType){
        String customerId = UUID.randomUUID().toString(); //generted random id
        //Change later : --> 10 + 1 = AC11
        String accountNumber = UUID.randomUUID().toString();
        Account ac = new Account(accountNumber, accountType, (double) 0, customerId);

        //Saving the account data in object (not database for now)
        accountRepository.save(ac);

        return accountNumber;
    }
}
