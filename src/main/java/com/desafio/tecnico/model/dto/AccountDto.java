package com.desafio.tecnico.model.dto;

import java.io.Serializable;

import com.desafio.tecnico.model.entity.Account;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class AccountDto implements Serializable {

	private static final long serialVersionUID = -781526420366319561L;
	
	@Getter @Setter
	private String name;
	
	@Getter @Setter
	private Float balance;
	
	public AccountDto (Account account) {
		this.setName(account.getName());
		this.setBalance(account.getBalance());
	}
	
}
