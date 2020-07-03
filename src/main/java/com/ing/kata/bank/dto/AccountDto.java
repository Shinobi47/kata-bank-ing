package com.ing.kata.bank.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class AccountDto {
	
	private Long id;
	private BigDecimal currentBalance;
	private CustomerDto customer;

}
