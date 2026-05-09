package service.impl;

import domain.Account;
import domain.Customer;
import domain.Transaction;
import domain.Type;
import exceptions.AccountNotFoundException;
import exceptions.InsufficientFundsException;
import exceptions.ValidationException;
import repository.AccountRepository;
import repository.CustomerRepository;
import repository.TransactionRepository;
import service.BankService;
import util.Validation;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static jdk.javadoc.internal.doclets.formats.html.markup.HtmlStyle.exceptions;

public class BankServiceImpl implements BankService {

    private final AccountRepository accountRepository =
            new AccountRepository();

    private final TransactionRepository transactionRepository =
            new TransactionRepository();

    private final CustomerRepository customerRepository =
            new CustomerRepository();

    // ================= VALIDATIONS =================

    private final Validation<String> validateName = name -> {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Name is required");
        }
    };

    private final Validation<String> validateEmail = email -> {
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new ValidationException("Invalid Email");
        }
    };

    private final Validation<String> validateType = type -> {

        if (type == null ||
                !(type.equalsIgnoreCase("SAVINGS")
                        || type.equalsIgnoreCase("CURRENT"))) {

            throw new ValidationException(
                    "Account Type must be SAVINGS or CURRENT"
            );
        }
    };

    private final Validation<Double> validateAmountPositive = amount -> {

        if (amount == null || amount <= 0) {
            throw new ValidationException(
                    "Amount must be greater than 0"
            );
        }
    };

    // ================= OPEN ACCOUNT =================

    @Override
    public String openAccount(
            String name,
            String email,
            String accountType
    ) {

        validateName.validate(name);
        validateEmail.validate(email);
        validateType.validate(accountType);

        // Create Customer ID
        String customerId = UUID.randomUUID().toString();

        // CREATE CUSTOMER
        // CHECK YOUR CONSTRUCTOR ORDER CAREFULLY
        Customer customer = new Customer(
                customerId,
                name,
                email
        );

        // SAVE CUSTOMER
        customerRepository.save(customer);

        // Generate Account Number
        String accountNumber = getAccountNumber();

        // CREATE ACCOUNT
        Account account = new Account(
                accountNumber,
                accountType.toUpperCase(),
                0.0,
                customerId
        );

        // SAVE ACCOUNT
        accountRepository.save(account);

        return accountNumber;
    }

    // ================= LIST ACCOUNTS =================

    @Override
    public List<Account> listAccounts() {

        return accountRepository.findAll().stream()
                .sorted(
                        Comparator.comparing(
                                Account::getAccountNumber
                        )
                )
                .collect(Collectors.toList());
    }

    // ================= DEPOSIT =================

    @Override
    public void deposit(
            String accountNumber,
            Double amount,
            String note
    ) {

        validateAmountPositive.validate(amount);

        Account account = accountRepository
                .findByNumber(accountNumber)
                .orElseThrow(() ->
                        new AccountNotFoundException(
                                "Account Not Found: "
                                        + accountNumber
                        )
                );

        // Update Balance
        account.setBalance(
                account.getBalance() + amount
        );

        // Create Transaction
        Transaction transaction = new Transaction(
                account.getAccountNumber(),
                amount,
                UUID.randomUUID().toString(),
                note,
                LocalDateTime.now(),
                Type.DEPOSIT
        );

        // Save Transaction
        transactionRepository.add(transaction);
    }

    // ================= WITHDRAW =================

    @Override
    public void withdraw(
            String accountNumber,
            Double amount,
            String note
    ) {

        validateAmountPositive.validate(amount);

        Account account = accountRepository
                .findByNumber(accountNumber)
                .orElseThrow(() ->
                        new AccountNotFoundException(
                                "Account Not Found: "
                                        + accountNumber
                        )
                );

        if (account.getBalance() < amount) {
            throw new InsufficientFundsException(
                    "Insufficient Balance"
            );
        }

        // Update Balance
        account.setBalance(
                account.getBalance() - amount
        );

        // Create Transaction
        Transaction transaction = new Transaction(
                account.getAccountNumber(),
                amount,
                UUID.randomUUID().toString(),
                note,
                LocalDateTime.now(),
                Type.WITHDRAW
        );

        // Save Transaction
        transactionRepository.add(transaction);
    }

    // ================= TRANSFER =================

    @Override
    public void transfer(
            String fromAcc,
            String toAcc,
            Double amount,
            String note
    ) {

        validateAmountPositive.validate(amount);

        if (fromAcc.equals(toAcc)) {
            throw new ValidationException(
                    "Cannot transfer to same account"
            );
        }

        Account from = accountRepository
                .findByNumber(fromAcc)
                .orElseThrow(() ->
                        new AccountNotFoundException(
                                "Account Not Found: "
                                        + fromAcc
                        )
                );

        Account to = accountRepository
                .findByNumber(toAcc)
                .orElseThrow(() ->
                        new AccountNotFoundException(
                                "Account Not Found: "
                                        + toAcc
                        )
                );

        if (from.getBalance() < amount) {
            throw new InsufficientFundsException(
                    "Insufficient Balance"
            );
        }

        // Update Balances
        from.setBalance(
                from.getBalance() - amount
        );

        to.setBalance(
                to.getBalance() + amount
        );

        // OUT Transaction
        transactionRepository.add(
                new Transaction(
                        from.getAccountNumber(),
                        amount,
                        UUID.randomUUID().toString(),
                        note,
                        LocalDateTime.now(),
                        Type.TRANSFER_OUT
                )
        );

        // IN Transaction
        transactionRepository.add(
                new Transaction(
                        to.getAccountNumber(),
                        amount,
                        UUID.randomUUID().toString(),
                        note,
                        LocalDateTime.now(),
                        Type.TRANSFER_IN
                )
        );
    }

    // ================= STATEMENT =================

    @Override
    public List<Transaction> getStatement(
            String account
    ) {

        return transactionRepository
                .findByAccount(account)
                .stream()
                .sorted(
                        Comparator.comparing(
                                Transaction::getTimestamp
                        )
                )
                .collect(Collectors.toList());
    }

    // ================= SEARCH =================

    @Override
    public List<Account> searchAccountsByCustomerName(
            String q
    ) {

        String query =
                (q == null)
                        ? ""
                        : q.toLowerCase();

        return customerRepository.findAll()
                .stream()

                .filter(customer ->
                        customer.getName()
                                .toLowerCase()
                                .contains(query)
                )

                .flatMap(customer ->
                        accountRepository
                                .findByCustomerId(
                                        customer.getId()
                                )
                                .stream()
                )

                .sorted(
                        Comparator.comparing(
                                Account::getAccountNumber
                        )
                )

                .collect(Collectors.toList());
    }

    // ================= ACCOUNT NUMBER =================

    private String getAccountNumber() {

        int size =
                accountRepository.findAll().size() + 1;

        return String.format(
                "AC%06d",
                size
        );
    }
}