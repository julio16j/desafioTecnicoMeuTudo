package com.desafio.tecnico.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.desafio.tecnico.model.dto.AccountDto;
import com.desafio.tecnico.model.entity.Account;
import com.desafio.tecnico.repository.AccountRepository;

@Component
public class AccountService {
	
	@Autowired
	private AccountRepository repository;
	
	public List<Account> getAllAccounts () {
		return repository.findAll();
	}

	public Account saveAccount(Account account) {
		Account newAccount = repository.save(account);
		return newAccount;
	}

	public void deleteAccount(Account account) {
		Account accountFounded = findByIdOrThrowNotFoundException(account.getCpf());
		repository.deleteById(accountFounded.getCpf());
		
	}

	public Account updateAccount(String cpf, AccountDto account) {
		Account accountFounded = findByIdOrThrowNotFoundException(cpf);
		if (account.getName() != null) {
			accountFounded.setName(account.getName());			
		}
		if (account.getBalance() != null) {
			accountFounded.setBalance(account.getBalance());			
		}
		return saveAccount(accountFounded);
	}
	
	public Account findByIdOrThrowNotFoundException (String cpf) {
		Optional<Account> accountFounded = repository.findById(cpf);
		if (accountFounded.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
		}
		return accountFounded.get();
	}

	public void deleteAccount(String cpf) {
		Account accountFounded = findByIdOrThrowNotFoundException(cpf);
		repository.deleteById(accountFounded.getCpf());
	}

	public Float getBalanceById(String cpf) {
		Account accountFounded = findByIdOrThrowNotFoundException(cpf);
		return accountFounded.getBalance();
	}
}
