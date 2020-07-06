package com.ing.kata.bank.dto;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class AccountDto {
	
	private Long id;
	
	private BigDecimal currentBalance;
	
	@JsonIgnore
	private CustomerDto customer;
	
	@JsonIgnore
	private List<StatementDto> statements;

}
