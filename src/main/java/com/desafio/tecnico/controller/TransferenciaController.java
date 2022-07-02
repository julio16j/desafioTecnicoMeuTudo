package com.desafio.tecnico.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.desafio.tecnico.model.dto.TransferDto;
import com.desafio.tecnico.model.entity.Transfer;
import com.desafio.tecnico.service.TransferService;

@RestController
@RequestMapping("transfers")
public class TransferenciaController {
	
	@Autowired
	private TransferService service;
	
	@GetMapping
	public ResponseEntity<List<Transfer>> getTransfers () {
		try {
			return ResponseEntity.ok(service.getAllTransfers());
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong");
		}
	}
	
	@PostMapping
	public ResponseEntity<List<Transfer>> saveTransfer (@RequestBody @Valid TransferDto transfer) {
		try {
			return ResponseEntity.status(HttpStatus.CREATED).body(service.saveTransfer(transfer));
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong");
		}
	}
	
	@PostMapping("revert/{id}")
	public ResponseEntity<?> revertTransfer (@PathVariable Long id) {
		try {
			service.revertTransfer(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong");
		}
	}
	
}
