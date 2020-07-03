package com.ing.kata.bank.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.ing.kata.bank.enums.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class StatementDto {

	private Long id;
	private LocalDateTime date;
	private BigDecimal amount;
	private BigDecimal balance;
	private TransactionType transactionType;
}
