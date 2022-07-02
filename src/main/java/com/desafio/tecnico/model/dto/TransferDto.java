package com.desafio.tecnico.model.dto;

import java.time.LocalDate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class TransferDto {
	
	@NotBlank
	@Getter @Setter
	private String senderId;
	
	@NotBlank
	@Getter @Setter
	private String receiverId;
	
	@PositiveOrZero
	@Getter @Setter
	private Float amount;
	
	@NotNull
	@Getter @Setter
	private LocalDate transferDate;
	
	@Min(1)
	@Getter @Setter
	private Integer installments;
}
