package com.ing.kata.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class CustomerDto {
	
	private Long id;
	private String name;
	private AccountDto account;

}
