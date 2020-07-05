package com.ing.kata.bank;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ing.kata.bank.entity.AccountEntity;
import com.ing.kata.bank.entity.StatementEntity;
import com.ing.kata.bank.exception.TechnicalException;
import com.ing.kata.bank.service.BankAccountService;
import com.ing.kata.bank.utils.DataMapper;

import kata.ing.bank.repository.BankAccountRepository;
import kata.ing.bank.repository.StatementRepository;

@ExtendWith(MockitoExtension.class)
public class DisplayOperationsTest {

	@Mock
	private BankAccountRepository bankAccountRepository;
	
	@Mock
	private StatementRepository statementRepository;
	
	private DataMapper dataMapper = new DataMapper();
	
	private BankAccountService bankAccountService;
	
	@BeforeEach
	public void init() {
		bankAccountService = new BankAccountService(bankAccountRepository, statementRepository, dataMapper);
	}
	
	@Test
	public void should_throw_exception_when_getting_account_with_null_id() {
		//Arrange
		Long accountId = null;
		
		//Act
		Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> 
		bankAccountService.fetchAccount(accountId));
		
		//Assert
		Assertions.assertTrue(exception.getMessage().contains("Cannot find account with null accountId"));
		verify(bankAccountRepository, never()).findById(accountId);
	}
	
	@Test
	public void should_return_accountDto_when_it_exits() {
		//Arrange
		Long accountId = 1L;
		
		AccountEntity accountFromRepo = AccountEntity.builder().id(accountId).build();
		when(bankAccountRepository.findById(accountId)).thenReturn(Optional.ofNullable(accountFromRepo));

		//Act
		bankAccountService.fetchAccount(accountId);
		
		//Assert
		verify(bankAccountRepository, times(1)).findById(accountId);
	}
	
	@Test
	public void should_throw_ewception_when_it_doesnt_exits() {
		//Arrange
		Long accountId = 1L;
		when(bankAccountRepository.findById(accountId)).thenReturn(Optional.empty());

		//Act
		Exception exception = Assertions.assertThrows(TechnicalException.class, () -> 
		bankAccountService.fetchAccount(accountId));
		
		//Assert
		Assertions.assertTrue(exception.getMessage().contains("No account with the specified id"));
		verify(bankAccountRepository, times(1)).findById(accountId);
	}
	
	
	@Test
	public void should_throw_exception_when_getting_transactions_history_with_null_id() {
		//Arrange
		Long accountId = null;
		
		//Act
		Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> 
		bankAccountService.fetchTransactionsHistory(accountId));
		
		//Assert
		Assertions.assertTrue(exception.getMessage().contains("Cannot find transactions history with null accountId"));
		verify(statementRepository, never()).findByAccount_id(accountId);
	}
	
	@Test
	public void should_return_transaction_history_when_it_exits() {
		//Arrange
		Long accountId = 1L;
		
		when(statementRepository.findByAccount_id(accountId)).thenReturn(Arrays.asList(StatementEntity.builder().build()));

		//Act
		bankAccountService.fetchTransactionsHistory(accountId);
		
		//Assert
		verify(statementRepository, times(1)).findByAccount_id(accountId);
	}
}
