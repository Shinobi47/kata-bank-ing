package com.ing.kata.bank;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ing.kata.bank.dto.AccountDto;
import com.ing.kata.bank.dto.StatementDto;
import com.ing.kata.bank.entity.AccountEntity;
import com.ing.kata.bank.entity.StatementEntity;
import com.ing.kata.bank.enums.TransactionType;
import com.ing.kata.bank.utils.DataMapper;

/**
 * @author HBenayed
 *
 */
@ExtendWith(MockitoExtension.class)
public class DataMapperTest {
	
	private DataMapper dataMapper = new DataMapper();
	
	@Test
	public void should_map_AccountEntity_successfully_when_input_with_values() {
		//Arrange
		BigDecimal givenBalance = new BigDecimal(10);
		Long givenId = 1L;
		AccountEntity entity = AccountEntity.builder()
				.currentBalance(givenBalance)
				.id(givenId).build();
		
		//Act
		AccountDto dto = dataMapper.toDto(entity);
		
		//Assert
		Assertions.assertThat(dto.getCurrentBalance()).isEqualByComparingTo(givenBalance);
		Assertions.assertThat(dto.getId()).isEqualTo(givenId);

	}
	
	@Test
	public void should_map_AccountEntity_successfully_when_input_is_Null() {
		//Arrange
		AccountEntity entity = AccountEntity.builder()
				.currentBalance(null)
				.id(null).build();
		
		//Act
		AccountDto dto = dataMapper.toDto(entity);
		
		//Assert
		Assertions.assertThat(dto.getCurrentBalance()).isNull();;
		Assertions.assertThat(dto.getId()).isNull();

	}
	
	@ParameterizedTest
	@EnumSource(value = TransactionType.class)
	public void should_map_(TransactionType transactionType) {
		//Arrange
		BigDecimal givenAmount = new BigDecimal(10);
		BigDecimal givenBalance = new BigDecimal(20);
		LocalDateTime givenDateTime = LocalDateTime.now();
		Long givenId = 10L;
		StatementEntity entity = StatementEntity.builder()
				.amount(givenAmount)
				.balance(givenBalance)
				.date(givenDateTime)
				.transactionType(transactionType)
				.id(givenId).build();
		//Act
		StatementDto dto = dataMapper.toDto(entity);
		
		//Assert
		Assertions.assertThat(dto.getAmount()).isEqualByComparingTo(givenAmount);
		Assertions.assertThat(dto.getBalance()).isEqualByComparingTo(givenBalance);
		Assertions.assertThat(dto.getDate()).isEqualTo(givenDateTime);
		Assertions.assertThat(dto.getTransactionType()).isEqualTo(transactionType);
		Assertions.assertThat(dto.getId()).isEqualTo(givenId);
		
	}
	
	@Test
	public void should_maps_() {
		//Arrange
		StatementEntity entity = StatementEntity.builder()
				.amount(null)
				.balance(null)
				.date(null)
				.transactionType(null)
				.id(null).build();
		//Act
		StatementDto dto = dataMapper.toDto(entity);
		
		//Assert
		Assertions.assertThat(dto.getAmount()).isNull();
		Assertions.assertThat(dto.getBalance()).isNull();
		Assertions.assertThat(dto.getDate()).isNull();
		Assertions.assertThat(dto.getTransactionType()).isNull();
		Assertions.assertThat(dto.getId()).isNull();
		
	}
	
}
