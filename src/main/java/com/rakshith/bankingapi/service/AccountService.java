package com.rakshith.bankingapi.service;

import com.rakshith.bankingapi.exception.InsufficientFundsException;
import com.rakshith.bankingapi.model.Account;
import com.rakshith.bankingapi.repository.AccountRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository repository;

    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    public Account createAccount(Account account) {
        account.setBalance(account.getBalance() != null ? account.getBalance() : 0.0);
        return repository.save(account);
    }

    public List<Account> getAllAccounts() {
        return repository.findAll();
    }

    public Account getAccountById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
    }

    public Account deposit(Long id, Double amount) {
        Account account = getAccountById(id);
        account.setBalance(account.getBalance() + amount);
        return repository.save(account);
    }

    public Account withdraw(Long id, Double amount) {
        Account account = getAccountById(id);
        if (account.getBalance() < amount) {
            throw new InsufficientFundsException("Insufficient funds. Available: " + account.getBalance());
        }
        account.setBalance(account.getBalance() - amount);
        return repository.save(account);
    }

    public void deleteAccount(Long id) {
        repository.deleteById(id);
    }
}