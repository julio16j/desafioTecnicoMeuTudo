package com.desafio.tecnico.model.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Account implements Serializable {

	private static final long serialVersionUID = -781526420366319561L;
	
	@Id
	@Getter @Setter
	private String cpf;
	
	@Column
	@Getter @Setter
	private String name;
	
	@Column
	@Getter @Setter
	private Float balance;
	
	@OneToMany(mappedBy = "sender", cascade = CascadeType.REMOVE )
	private Set<Transfer> transfersSended;
	
	@OneToMany(mappedBy = "receiver", cascade = CascadeType.REMOVE )
	private Set<Transfer> transfersReceived;
	
	@Override
	public int hashCode() {
		return Objects.hash(balance, cpf, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		return Objects.equals(balance, other.balance) && Objects.equals(cpf, other.cpf)
				&& Objects.equals(name, other.name);
	}

	public Account(String cpf, String name, Float balance) {
		super();
		this.cpf = cpf;
		this.name = name;
		this.balance = balance;
	}
	
}
