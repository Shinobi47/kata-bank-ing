package com.ing.kata.bank.dto;

import java.math.BigDecimal;

import com.ing.kata.bank.enums.TransactionType;

import lombok.Data;

@Data
public class TransactionDto {
	
	private Long accountId;
	private BigDecimal amount;
	private TransactionType transactionType;

}
