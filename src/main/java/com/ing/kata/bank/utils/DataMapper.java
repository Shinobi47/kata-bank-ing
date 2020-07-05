package com.ing.kata.bank.utils;

import org.springframework.stereotype.Component;

import com.ing.kata.bank.dto.AccountDto;
import com.ing.kata.bank.dto.StatementDto;
import com.ing.kata.bank.entity.AccountEntity;
import com.ing.kata.bank.entity.StatementEntity;

@Component
public class DataMapper {

	public AccountDto toDto(AccountEntity account) {
		return AccountDto.builder()
				.currentBalance(account.getCurrentBalance())
				.id(account.getId()).build();
	}
	
	public StatementDto toDto(StatementEntity entity) {
		return StatementDto.builder()
		.amount(entity.getAmount())
		.balance(entity.getBalance())
		.date(entity.getDate())
		.transactionType(entity.getTransactionType())
		.id(entity.getId()).build();
	}
}
