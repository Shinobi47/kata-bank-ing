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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ing.kata.bank.constant.Constants;
import com.ing.kata.bank.entity.AccountEntity;
import com.ing.kata.bank.enums.TransactionType;
import com.ing.kata.bank.exception.TechnicalException;
import com.ing.kata.bank.service.BankAccountService;
import com.ing.kata.bank.utils.DataMapper;

import kata.ing.bank.repository.BankAccountRepository;
import kata.ing.bank.repository.StatementRepository;


@ExtendWith(MockitoExtension.class)
class TransactionTests {

	@Mock
	private BankAccountRepository bankAccountRepository;
	
	@Mock
	private StatementRepository statementRepository;
	
	private BankAccountService bankAccountService;
	
	private DataMapper dataMapper = new DataMapper();
	
	@BeforeEach
	public void init() {
		bankAccountService = new BankAccountService(bankAccountRepository, statementRepository, dataMapper);
	}
	
	@ParameterizedTest
	@EnumSource(TransactionType.class)
	public void should_throw_exception_when_performing_transaction_with_null_accountId(TransactionType transactionType) {
		//Arrange
		Long accountId = null;
		BigDecimal amount = new BigDecimal(1);
		
		//Act
		Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> 
		bankAccountService.executeTransaction(transactionType, accountId, amount));
		
		//Assert
		Assertions.assertTrue(exception.getMessage().contains("Cannot perform transaction"));
		verify(bankAccountRepository, never()).findById(accountId);
		verify(bankAccountRepository, never()).save(any());
		verify(statementRepository, never()).save(any());
		
	}
	
	
	@ParameterizedTest
	@EnumSource(TransactionType.class)
	public void should_throw_exception_when_performing_transaction_with_null_amount(TransactionType transactionType) {
		//Arrange
		Long accountId = 1L;
		BigDecimal amount = null;
		
		//Act
		Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> 
		bankAccountService.executeTransaction(transactionType, accountId, amount));
		
		//Assert
		Assertions.assertTrue(exception.getMessage().contains("Cannot perform transaction"));
		verify(bankAccountRepository, never()).findById(accountId);
		verify(bankAccountRepository, never()).save(any());
		verify(statementRepository, never()).save(any());
	}
	
	@ParameterizedTest
	@EnumSource(TransactionType.class)
	public void should_throw_exception_when_performing_transaction_with_negative_amount(TransactionType transactionType) {
		//Arrange
		Long accountId = 1L;
		BigDecimal negativeAmount = new BigDecimal(-10);
		
		//Act
		Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> 
		bankAccountService.executeTransaction(transactionType, accountId, negativeAmount));
		
		//Assert
		Assertions.assertTrue(exception.getMessage().contains("Cannot perform transaction with non positive amount"));
		verify(bankAccountRepository, never()).findById(accountId);
		verify(bankAccountRepository, never()).save(any());
		verify(statementRepository, never()).save(any());
	}
	
	@ParameterizedTest
	@EnumSource(TransactionType.class)
	public void should_throw_exception_when_performing_transaction_with_zero_amount(TransactionType transactionType) {
		//Arrange
		Long accountId = 1L;
		BigDecimal zeroAmount = BigDecimal.ZERO;
		
		//Act
		Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> 
		bankAccountService.executeTransaction(transactionType, accountId, zeroAmount));
		
		//Assert
		Assertions.assertTrue(exception.getMessage().contains("Cannot perform transaction with non positive amount"));
		verify(bankAccountRepository, never()).findById(accountId);
		verify(bankAccountRepository, never()).save(any());
		verify(statementRepository, never()).save(any());
	}
	
	@ParameterizedTest
	@EnumSource(TransactionType.class)
	public void should_throw_exception_when_account_doest_exist(TransactionType transactionType) {
		//Arrange
		Long accountId = 1L;
		AccountEntity accountFromRepo = null;
		
		when(bankAccountRepository.findById(accountId)).thenReturn(Optional.ofNullable(accountFromRepo));
		
		//Act
		Exception exception = Assertions.assertThrows(TechnicalException.class, () -> 
		bankAccountService.executeTransaction(transactionType, accountId, new BigDecimal(10)));
		
		//Assert
		Assertions.assertTrue(exception.getMessage().contains("No account available with the specified accountId"));
		verify(bankAccountRepository, times(1)).findById(accountId);
		verify(bankAccountRepository, never()).save(any());
		verify(statementRepository, never()).save(any());		
	}
	
	
	
	
	@Test
	public void should_throw_exception_when_withdrawing_with_overdraft_on_the_account() {
		//Arrange
		Long accountId = 1L;
		BigDecimal amountToWithdraw = new BigDecimal(10);
		BigDecimal currentBalance = new BigDecimal(0);
		
		AccountEntity accountFromRepo = AccountEntity.builder().id(accountId).currentBalance(currentBalance).build();
		when(bankAccountRepository.findById(accountId)).thenReturn(Optional.ofNullable(accountFromRepo));

		//Act
		Exception exception = Assertions.assertThrows(TechnicalException.class, () -> 
		bankAccountService.executeTransaction(TransactionType.WITHDRAW, accountId, amountToWithdraw));
		
		//Assert
		Assertions.assertTrue(exception.getMessage().contains("Withdraw not allowed because you are poor"));
		verify(bankAccountRepository, times(1)).findById(accountId);
		verify(bankAccountRepository, never()).save(any());
		verify(statementRepository, never()).save(any());
	}


	
	@Test
	public void should_withdraw_succesfully_when_withdraw_amount_is_less_than_balance() {
		//Arrange
		Long accountId = 1L;
		BigDecimal AmountToWithdraw = new BigDecimal(10);
		BigDecimal currentBalance = new BigDecimal(100);
		
		AccountEntity accountFromRepo = AccountEntity.builder().id(accountId).currentBalance(currentBalance).build();
		when(bankAccountRepository.findById(accountId)).thenReturn(Optional.ofNullable(accountFromRepo));
		
		//Act
		bankAccountService.executeTransaction(TransactionType.WITHDRAW, accountId, AmountToWithdraw);
		
		//Assert
		verify(bankAccountRepository, times(1)).findById(accountId);
		verify(bankAccountRepository, times(1)).save(any());
		verify(statementRepository, times(1)).save(any());

	}

	@Test
	public void should_withdraw_succesfully_when_withdraw_amount_is_equal_to_balance() {
		//Arrange
		Long accountId = 1L;
		BigDecimal AmountToWithdraw = new BigDecimal(10);
		BigDecimal currentBalance = new BigDecimal(10);
		
		AccountEntity accountFromRepo = AccountEntity.builder().id(accountId).currentBalance(currentBalance).build();
		when(bankAccountRepository.findById(accountId)).thenReturn(Optional.ofNullable(accountFromRepo));
		
		//Act
		bankAccountService.executeTransaction(TransactionType.WITHDRAW, accountId, AmountToWithdraw);
		
		//Assert
		verify(bankAccountRepository, times(1)).findById(accountId);
		verify(bankAccountRepository, times(1)).save(any());
		verify(statementRepository, times(1)).save(any());

	}
	
	
	@Test
	public void should_throw_exception_when_depositing_with_amount_less_than_minimum_allowed() {
		//Arrange
		Long accountId = 1L;
		BigDecimal amountLessThanMinimumAllowed = Constants.MINIMAL_DEPOSIT.subtract(new BigDecimal(0.001));
		
		AccountEntity accountFromRepo = AccountEntity.builder().id(accountId).currentBalance(new BigDecimal(10)).build();
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
	public void should_deposit_succesfully_when_depositing_with_allowed_amount() {
		//Arrange
		Long accountId = 1L;
		BigDecimal AmountToDeposit = Constants.MINIMAL_DEPOSIT.add(new BigDecimal(10));
		
		AccountEntity accountFromRepo = AccountEntity.builder().id(accountId).currentBalance(new BigDecimal(10)).build();
		when(bankAccountRepository.findById(accountId)).thenReturn(Optional.ofNullable(accountFromRepo));
		
		//Act
		bankAccountService.executeTransaction(TransactionType.DEPOSIT, accountId, AmountToDeposit);
		
		//Assert
		verify(bankAccountRepository, times(1)).findById(accountId);
		verify(bankAccountRepository, times(1)).save(any());
		verify(statementRepository, times(1)).save(any());

	}


}
