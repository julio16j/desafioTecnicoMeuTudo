package com.desafio.tecnico.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.desafio.tecnico.mode.enums.TransferStatusEnum;
import com.desafio.tecnico.model.dto.TransferDto;
import com.desafio.tecnico.model.entity.Account;
import com.desafio.tecnico.model.entity.Transfer;
import com.desafio.tecnico.service.AccountService;
import com.desafio.tecnico.service.TransferService;
import com.desafio.tecnico.utils.TestUtilities;
import com.desafio.tecnico.utils.Utilities;

@SpringBootTest
@AutoConfigureMockMvc
public class TransferControllerTests {
	
public static final String BASE_URL = "/transfers";
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	private TransferService service;
	
	@Autowired
	private AccountService accountService;
	
	@Test
	void getAllTransfersTest () throws Exception {
		MvcResult result = TestUtilities.performGetRequest(mockMvc, BASE_URL);
		String resultBody = result.getResponse().getContentAsString();
		assertTrue(resultBody.startsWith("["), "Invalid result body type");
	}
	
	@Test
	void newTransferTest () throws Exception {
		Account newSenderAccount = new Account("77711188801", "Julio Cesar", 1000f);
		Account newReceiverAccount = new Account("77711188802", "Matheus", 1000f);
		insertAccount(newSenderAccount);
		insertAccount(newReceiverAccount);
		TransferDto transferDto = new TransferDto(newSenderAccount.getCpf(), newReceiverAccount.getCpf(), 500f, LocalDate.now(), 1);
		String requestJson=Utilities.OBJECT_WRITTER.writeValueAsString(transferDto);
	    TestUtilities.performPostRequest(mockMvc, BASE_URL, requestJson);
	    List<Transfer> filteredTransfer = service.getAllTransfers().stream().filter(filterTransfer(transferDto)).collect(Collectors.toList());
		rollbackTransfer(transferDto);
		assertTrue(filteredTransfer.size() > 0, "Transfer not submitted");
		
	}
	
	@Test
	void scheduleTransferTest () throws Exception {
		Account newSenderAccount = new Account("77711188801", "Julio Cesar", 1000f);
		Account newReceiverAccount = new Account("77711188802", "Matheus", 1000f);
		insertAccount(newSenderAccount);
		insertAccount(newReceiverAccount);
		TransferDto transferDto = new TransferDto(newSenderAccount.getCpf(), newReceiverAccount.getCpf(), 500f, LocalDate.now(), 5);
		String requestJson=Utilities.OBJECT_WRITTER.writeValueAsString(transferDto);
	    TestUtilities.performPostRequest(mockMvc, BASE_URL, requestJson);
	    List<Transfer> filteredTransfer = service.getAllTransfers().stream().filter(filterTransfer(transferDto)).collect(Collectors.toList());
		rollbackTransfer(transferDto);
		assertTrue(filteredTransfer.size() > 0, "Transfer not submitted");
		
	}
	
	@Test
	void revertDoneTransferTest () throws Exception {
		Transfer transfer = newMockTransfer(TransferStatusEnum.Done);
		TestUtilities.performPostRequest(mockMvc, BASE_URL + "/revert/" + transfer.getId(), "{}");
		List<Transfer> transfers = service.getAllTransfers();
		Boolean isThereARevertedTransfer = false;
		for (Transfer transferItem : transfers) {
			if (transferItem.getId().equals(transfer.getId())) {
				assertEquals(TransferStatusEnum.Reverted, transferItem.getTransferStatus(), "Transfer Status not reverted");
			} else if (transferItem.getReceiver().getCpf().equals(transfer.getSender().getCpf())
					&& transferItem.getSender().getCpf().equals(transfer.getReceiver().getCpf())
					&& transferItem.getAmount().equals(transfer.getAmount())
					&& transferItem.getTransactionDate().isAfter(transfer.getTransactionDate())
					&& transferItem.getTransferStatus().equals(TransferStatusEnum.Done)) {
				isThereARevertedTransfer = true;
			}
		}
		rollbackTransfer(transfer);
		assertTrue(isThereARevertedTransfer, "Transfer not Reverted");
	}
	
	@Test
	void revertScheduledTransferTest () throws Exception {
		Transfer transfer = newMockTransfer(TransferStatusEnum.Scheduled);
		TestUtilities.performPostRequest(mockMvc, BASE_URL + "/revert/" + transfer.getId(), "{}");
		List<Transfer> transfers = service.getAllTransfers();
		rollbackTransfer(transfer);
		assertFalse(transfers.contains(transfer), "Transfer Scheduled must be excluded");
	}
	
	private Transfer newMockTransfer(TransferStatusEnum transferStatus) {
		Account newSenderAccount = new Account("77711188801", "Julio Cesar", 1000f);
		Account newReceiverAccount = new Account("77711188802", "Matheus", 1000f);
		insertAccount(newSenderAccount);
		insertAccount(newReceiverAccount);
		TransferDto transferDto = new TransferDto(newSenderAccount.getCpf(), newReceiverAccount.getCpf(), 500f, LocalDate.now(),
				TransferStatusEnum.Done.equals(transferStatus) ? 1 : 2);
		return service.saveTransfer(transferDto).get(0);
	}

	private void rollbackTransfer(TransferDto transferDto) {
		accountService.deleteAccount(transferDto.getSenderId());
		accountService.deleteAccount(transferDto.getReceiverId());
		
	}
	
	private void rollbackTransfer(Transfer transferDto) {
		accountService.deleteAccount(transferDto.getSender());
		accountService.deleteAccount(transferDto.getReceiver());
		
	}

	private Predicate<? super Transfer> filterTransfer (TransferDto expectedTransfer) {
		return transfer -> {
			if (!transfer.getReceiver().getCpf().equals(expectedTransfer.getReceiverId())) return false;
			if (!transfer.getSender().getCpf().equals(expectedTransfer.getSenderId())) return false;
			if (!transfer.getAmount().equals(expectedTransfer.getAmount() / expectedTransfer.getInstallments())) return false;
			if (!transfer.getTransactionDate().toLocalDate().equals(expectedTransfer.getTransferDate())) return false;
			return true;
			
		};
	}
	
	private void insertAccount(Account newAccount) {
		accountService.saveAccount(newAccount);
		
	}
}
