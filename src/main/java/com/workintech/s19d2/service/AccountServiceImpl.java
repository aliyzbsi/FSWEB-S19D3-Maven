package com.workintech.s19d2.service;

import com.workintech.s19d2.entity.Account;
import com.workintech.s19d2.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public Account findById(long id) {
        Optional<Account> accountOptional=accountRepository.findById(id);
        if(accountOptional.isPresent()){
            return accountOptional.get();
        }else{
            throw new RuntimeException(id+": id'sine sahip account bulunamadı!");
        }

    }

    @Override
    public Account save(Account account) {
        return accountRepository.save(account);
    }
    @Override
    public Account delete(long id) {
        Account willBeDeletedAccount=findById(id);
        accountRepository.delete(willBeDeletedAccount);
        return willBeDeletedAccount;
    }
}
