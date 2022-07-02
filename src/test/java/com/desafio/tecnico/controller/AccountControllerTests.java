package com.desafio.tecnico.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.desafio.tecnico.model.dto.AccountDto;
import com.desafio.tecnico.model.entity.Account;
import com.desafio.tecnico.service.AccountService;
import com.desafio.tecnico.utils.TestUtilities;
import com.desafio.tecnico.utils.Utilities;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTests {
	
	public static final String BASE_URL = "/accounts";
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	private AccountService service;
	
	@Test
	void getBalanceTest () throws Exception {
		Account newAccount = new Account("77711188800", "Julio Cesar", 1000f);
		insertAccount(newAccount);
		MvcResult result = TestUtilities.performGetRequest(mockMvc, BASE_URL+"/balance/"+newAccount.getCpf());
		Float resultBody = Float.parseFloat(result.getResponse().getContentAsString());
		rollbackAccount(newAccount);
		assertEquals(1000f, resultBody, "Invalid result body");
	}
	
	@Test
	void getAllAccountsTest () throws Exception {
		MvcResult result = TestUtilities.performGetRequest(mockMvc, BASE_URL);
		String resultBody = result.getResponse().getContentAsString();
		assertTrue(resultBody.startsWith("["), "Invalid result body type");
	}

	@Test
	void createNewAccountTest () throws Exception {
		Account newAccount = new Account("77711188800", "Julio Cesar", 1000f);
	    String requestJson=Utilities.OBJECT_WRITTER.writeValueAsString(newAccount);
	    TestUtilities.performPostRequest(mockMvc, BASE_URL, requestJson);
		List<Account> filteredAccounts = service.getAllAccounts().stream().filter((accountFilter -> accountFilter.getCpf().equals(newAccount.getCpf()))).collect(Collectors.toList());
		rollbackAccount(newAccount);
		if (filteredAccounts.size() > 0) {
			assertEquals(filteredAccounts.get(0), newAccount, "Account invalid");
		} else {
			assertTrue(false, "Account not inserted");
		}
	}
	
	@Test
	void updateAccountTest () throws Exception {
		Account newAccount = new Account("77711188800", "Julio Cesar", 1000f);
		insertAccount(newAccount);
		AccountDto accountDto = new AccountDto(newAccount.getName() + " Urbano", newAccount.getBalance() + 1000f);
	    String requestJson=Utilities.OBJECT_WRITTER.writeValueAsString(accountDto);
	    TestUtilities.performUpdateRequest(mockMvc, BASE_URL+"/"+newAccount.getCpf(), requestJson);
		List<Account> filteredAccounts = service.getAllAccounts().stream().filter((accountFilter -> accountFilter.getCpf().equals(newAccount.getCpf()))).collect(Collectors.toList());
		rollbackAccount(newAccount);
		if (filteredAccounts.size() > 0) {
			Account listedAccount = filteredAccounts.get(0);
			assertEquals(accountDto.getName(), listedAccount.getName(), "Account not found");
			assertEquals(accountDto.getBalance(), listedAccount.getBalance(), "Account not found");
		} else {
			assertTrue(false, "Account not found");
		}
	}
	
	@Test
	void deleteAccountTest () throws Exception {
		Account newAccount = new Account("77711188800", "Julio Cesar", 1000f);
		insertAccount(newAccount);
		TestUtilities.performDeleteRequest(mockMvc, BASE_URL+"/"+newAccount.getCpf());
		List<Account> filteredAccounts = service.getAllAccounts().stream().filter((accountFilter -> accountFilter.getCpf().equals(newAccount.getCpf()))).collect(Collectors.toList());
		assertTrue(filteredAccounts.size() == 0, "Account not deleted");
	}

	@Test
	void getBalanceCpfNotFoundTest () throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL+"/balance/111111"))
		.andExpect(MockMvcResultMatchers.status().is4xxClientError());
	}
		
	private void insertAccount(Account newAccount) {
		service.saveAccount(newAccount);
		
	}
	
	private void rollbackAccount(Account newAccount) {
		service.deleteAccount(newAccount);
	}
	
	
}
