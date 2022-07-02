package com.desafio.tecnico.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.desafio.tecnico.mode.enums.TransferStatusEnum;
import com.desafio.tecnico.model.dto.AccountDto;
import com.desafio.tecnico.model.dto.TransferDto;
import com.desafio.tecnico.model.entity.Account;
import com.desafio.tecnico.model.entity.Transfer;
import com.desafio.tecnico.repository.TransferRepository;

@Component
public class TransferService {
	
	@Autowired
	private TransferRepository repository;
	
	@Autowired
	private AccountService accountService;
	
	public List<Transfer> getAllTransfers() {
		return repository.findAll();
	}

	public List<Transfer> saveTransfer (TransferDto transfer) {
		Account sender = accountService.findByIdOrThrowNotFoundException(transfer.getSenderId());
		Account receiver = accountService.findByIdOrThrowNotFoundException(transfer.getReceiverId());
		List<Transfer> newTransfers = new ArrayList<>();
		if (transfer.getInstallments() == 1) {
			newTransfers.add(executeTransfer(transfer, sender, receiver));
		} else {
			newTransfers.addAll(scheduleTransfer(transfer, sender, receiver));
		}
		
		return newTransfers;
		
	}

	private List<Transfer> scheduleTransfer(TransferDto transfer, Account sender, Account receiver) {
		List<Transfer> newTransfers = new ArrayList<>();
		for (int i = 0; i < transfer.getInstallments(); i++) {
			Transfer newTransfer = new Transfer();
			newTransfer.setSender(sender);
			newTransfer.setReceiver(receiver);
			newTransfer.setAmount(transfer.getAmount());
			newTransfer.setTransactionDate(LocalDateTime.now());
			newTransfer.setScheduledDate(LocalDate.now().plusMonths(i));
			newTransfer.setTransferStatus(TransferStatusEnum.Scheduled);
			newTransfers.add(newTransfer);
		}
		return repository.saveAll(newTransfers);
	}

	private Transfer executeTransfer(TransferDto transfer, Account sender, Account receiver) {
		if (transfer.getAmount() > sender.getBalance()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insuficients sender funds");
		}
		sender.setBalance(sender.getBalance() - transfer.getAmount());
		receiver.setBalance(transfer.getAmount() + receiver.getBalance());
		Transfer newTransfer = new Transfer();
		newTransfer.setSender(sender);
		newTransfer.setReceiver(receiver);
		newTransfer.setAmount(transfer.getAmount());
		newTransfer.setTransactionDate(LocalDateTime.now());
		newTransfer.setScheduledDate(LocalDate.now());
		newTransfer.setTransferStatus(TransferStatusEnum.Done);
		accountService.updateAccount(sender.getCpf(), new AccountDto(sender));
		accountService.updateAccount(receiver.getCpf(), new AccountDto(receiver));
		return repository.save(newTransfer);
		
	}

	public void revertTransfer(Long id) {
		Transfer transferReverted = findByIdOrThrowNotFoundException(id);
		if (transferReverted.getTransferStatus().equals(TransferStatusEnum.Done)) {
			TransferDto invertedTransfer = new TransferDto(transferReverted.getReceiver().getCpf(), transferReverted.getSender().getCpf(), transferReverted.getAmount(), LocalDate.now(), 1);
			saveTransfer(invertedTransfer);
			transferReverted.setTransferStatus(TransferStatusEnum.Reverted);
			repository.save(transferReverted);
		} else {
			repository.delete(transferReverted);
		}
		
	}
	
	public Transfer findByIdOrThrowNotFoundException (Long id) {
		Optional<Transfer> transferFounded = repository.findById(id);
		if (transferFounded.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
		}
		return transferFounded.get();
	}

}
