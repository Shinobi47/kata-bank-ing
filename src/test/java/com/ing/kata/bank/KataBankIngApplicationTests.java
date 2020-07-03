package com.ing.kata.bank;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ing.kata.bank.constant.Constants;
import com.ing.kata.bank.dto.AccountDto;
import com.ing.kata.bank.enums.TransactionType;
import com.ing.kata.bank.exception.TechnicalException;
import com.ing.kata.bank.service.BankAccountService;

import kata.ing.bank.repository.BankAccountRepository;
import kata.ing.bank.repository.StatementRepository;


@ExtendWith(MockitoExtension.class)
class KataBankIngApplicationTests {

	@Mock
	private BankAccountRepository bankAccountRepository;
	
	@Mock
	private StatementRepository statementRepository;
	
	private BankAccountService bankAccountService;
	
	@BeforeEach
	public void init() {
		bankAccountService = new BankAccountService(bankAccountRepository, statementRepository);
	}
	
	@Test
	public void should_throw_exception_when_depositing_with_null_accountId() {
		//Arrange
		Long accountId = null;
		BigDecimal amount = new BigDecimal(1);
		
		//Act
		Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> 
		bankAccountService.executeTransaction(TransactionType.DEPOSIT, accountId, amount));
		
		//Assert
		Assertions.assertTrue(exception.getMessage().contains("Cannot perform transaction"));
		verify(bankAccountRepository, never()).findById(accountId);
		verify(bankAccountRepository, never()).save(any());
		verify(statementRepository, never()).save(any());
		
	}
	
	@Test
	public void should_throw_exception_when_depositing_with_null_amount() {
		//Arrange
		Long accountId = 1L;
		BigDecimal amount = null;
		
		//Act
		Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> 
		bankAccountService.executeTransaction(TransactionType.DEPOSIT, accountId, amount));
		
		//Assert
		Assertions.assertTrue(exception.getMessage().contains("Cannot perform transaction"));
		verify(bankAccountRepository, never()).findById(accountId);
		verify(bankAccountRepository, never()).save(any());
		verify(statementRepository, never()).save(any());
	}
	
	@Test
	public void should_throw_exception_when_depositing_with_amount_less_than_minimum_allowed() {
		//Arrange
		Long accountId = 1L;
		BigDecimal amountLessThanMinimumAllowed = Constants.MINIMAL_DEPOSIT.subtract(new BigDecimal(0.001));
		AccountDto accountFromRepo = AccountDto.builder().id(accountId).currentBalance(new BigDecimal(10)).build();
		
		when(bankAccountRepository.findById(accountId)).thenReturn(Optional.ofNullable(accountFromRepo));
		
		//Act
		Exception exception = Assertions.assertThrows(TechnicalException.class, () -> 
		bankAccountService.executeTransaction(TransactionType.DEPOSIT, accountId, amountLessThanMinimumAllowed));
		
		//Assert
		Assertions.assertTrue(exception.getMessage().contains("Deposit amount is less than minimum allowed"));
		verify(bankAccountRepository, times(1)).findById(accountId);
		verify(bankAccountRepository, never()).save(any());
		verify(statementRepository, never()).save(any());
	}
	
	@Test
	public void should_throw_exception_when_account_doest_exist() {
		//Arrange
		Long accountId = 1L;
		BigDecimal AmountToDeposit = Constants.MINIMAL_DEPOSIT.add(new BigDecimal(10));
		AccountDto accountFromRepo = null;
		
		when(bankAccountRepository.findById(accountId)).thenReturn(Optional.ofNullable(accountFromRepo));
		
		//Act
		Exception exception = Assertions.assertThrows(TechnicalException.class, () -> 
		bankAccountService.executeTransaction(TransactionType.DEPOSIT, accountId, AmountToDeposit));
		
		//Assert
		Assertions.assertTrue(exception.getMessage().contains("No account available with the specified accountId"));
		verify(bankAccountRepository, times(1)).findById(accountId);
		verify(bankAccountRepository, never()).save(any());
		verify(statementRepository, never()).save(any());		
	}
	
	@Test
	public void should_deposit_succesfully_when_depositing_with_allowed_amount() {
		//Arrange
		Long accountId = 1L;
		BigDecimal AmountToDeposit = Constants.MINIMAL_DEPOSIT.add(new BigDecimal(10));
		AccountDto accountFromRepo = AccountDto.builder().id(accountId).currentBalance(new BigDecimal(10)).build();
		
		when(bankAccountRepository.findById(accountId)).thenReturn(Optional.ofNullable(accountFromRepo));
		
		//Act
		bankAccountService.executeTransaction(TransactionType.DEPOSIT, accountId, AmountToDeposit);
		
		//Assert
		verify(bankAccountRepository, times(1)).findById(accountId);
		verify(bankAccountRepository, times(1)).save(any());
		verify(statementRepository, times(1)).save(any());

	}

}
