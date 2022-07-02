package com.desafio.tecnico.model.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.desafio.tecnico.mode.enums.TransferStatusEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Transfer implements Serializable {
	
	private static final long serialVersionUID = -2474811137757406580L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter @Setter
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "sender_id", nullable = false)
	@Getter @Setter
	private Account sender;
	
	@ManyToOne
	@JoinColumn(name = "receiver_id", nullable = false)
	@Getter @Setter
	private Account receiver;
	
	@Column
	@Getter @Setter
	private Float amount;
	
	@Column
	@Getter @Setter
	private LocalDateTime transactionDate;
	
	@Column
	@Getter @Setter
	private LocalDate scheduledDate;
	
	@Column
	@Enumerated(EnumType.STRING)
	@Getter @Setter
	private TransferStatusEnum transferStatus;
	
	public Transfer (Transfer another) {
		this.id = another.getId();
		this.sender = another.getSender();
		this.receiver = another.getReceiver();
		this.amount = another.getAmount();
		this.transactionDate = another.getTransactionDate();
		this.transferStatus = another.getTransferStatus();
	}
	
}
