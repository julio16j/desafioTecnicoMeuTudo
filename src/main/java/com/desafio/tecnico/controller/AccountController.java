package com.desafio.tecnico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.desafio.tecnico.model.dto.AccountDto;
import com.desafio.tecnico.model.entity.Account;
import com.desafio.tecnico.service.AccountService;

@RestController
@RequestMapping("accounts")
public class AccountController {
	
	@Autowired
	private AccountService service;
	
	@GetMapping("balance/{cpf}")
	public ResponseEntity<Float> getBalance (@PathVariable String cpf) {
		try {
			return ResponseEntity.ok(service.getBalanceById(cpf));
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong");
		}
	}
	
	@GetMapping
	public ResponseEntity<List<Account>> getAccounts () {
		try {
			return ResponseEntity.ok(service.getAllAccounts());
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong");
		}
	}
	
	@PostMapping
	public ResponseEntity<Account> saveAccount (@RequestBody Account account) {
		try {
			return ResponseEntity.status(HttpStatus.CREATED).body(service.saveAccount(account));
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong");
		}
	}
	
	@PutMapping("{cpf}")
	public ResponseEntity<Account> updateAccount (@PathVariable String cpf, @RequestBody AccountDto account) {
		try {
			return ResponseEntity.status(HttpStatus.CREATED).body(service.updateAccount(cpf , account));
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong");
		}
	}
	
	@DeleteMapping("{cpf}")
	public ResponseEntity<?> deleteAccount (@PathVariable String cpf) {
		try {
			service.deleteAccount(cpf);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong");
		}
	}
	
}
