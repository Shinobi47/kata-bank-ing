package com.ing.kata.bank.service;

import static com.ing.kata.bank.enums.TransactionType.DEPOSIT;
import static com.ing.kata.bank.enums.TransactionType.WITHDRAW;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import static com.ing.kata.bank.constant.Constants.MINIMAL_DEPOSIT;
import com.ing.kata.bank.dto.AccountDto;
import com.ing.kata.bank.dto.StatementDto;
import com.ing.kata.bank.enums.TransactionType;
import com.ing.kata.bank.exception.TechnicalException;

import kata.ing.bank.repository.BankAccountRepository;
import kata.ing.bank.repository.StatementRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class BankAccountService {
	
	private BankAccountRepository bankAccountRepository;
	private StatementRepository statementRepository;
	
	public AccountDto fetchAccount(Long customerId) {
		return new AccountDto();
		
	}
	
	public List<StatementDto> fetchTransactionsHistory(Long accountId){
		return new ArrayList<StatementDto>();
	}
	
	public void executeTransaction(TransactionType transactionType, Long accountId, BigDecimal amount) {
		
		Assert.notNull(accountId, "Cannot perform transaction with null accountId");
		Assert.notNull(amount, "Cannot perform transaction with null amount");
		Assert.isTrue(isPositive(amount), "Cannot perform transaction with negative amount");
		
		AccountDto account = bankAccountRepository.findById(accountId).orElseThrow(() -> new TechnicalException("No account available with the specified accountId"));
		
		if(DEPOSIT.equals(transactionType)) {
			depositAmount(amount, account);
		}
		else if(WITHDRAW.equals(transactionType)) {
			withdrawAmount(amount, account);
		}
	}
	
	private void withdrawAmount(BigDecimal amount, AccountDto account) {
		BigDecimal currentBalance = account.getCurrentBalance();
		
		if(amountToWithdrawDoesntCauseOverdraft(amount, currentBalance)) {
			log.info("Withdrawing the amount : {} from the account : {}", amount, account.toString());
			BigDecimal newBalance = currentBalance.subtract(amount);
			account.setCurrentBalance(newBalance);
			
			StatementDto statement = StatementDto.builder()
				.amount(amount)
				.date(LocalDateTime.now())
				.transactionType(WITHDRAW)
				.balance(newBalance).build();
			bankAccountRepository.save(account);
			statementRepository.save(statement);
		}
		
		else {
			throw new TechnicalException("Withdraw not allowed because you are poor");
		}
	}

	private boolean isPositive(BigDecimal amount) {
		return amount.compareTo(BigDecimal.ZERO) > 0;
	}

	private boolean amountToWithdrawDoesntCauseOverdraft(BigDecimal amount, BigDecimal currentBalance) {
		return currentBalance.compareTo(amount) == 1 || currentBalance.compareTo(amount) == 0;
	}
	
	private void depositAmount(BigDecimal amount, AccountDto account) {
		
		if(amount.compareTo(MINIMAL_DEPOSIT) == 1) {
			log.info("Depositing the amount : {} on the account : {}", amount, account.toString());
			BigDecimal newBalance = account.getCurrentBalance().add(amount);
			account.setCurrentBalance(newBalance);
			StatementDto statement = StatementDto.builder()
				.amount(amount)
				.date(LocalDateTime.now())
				.transactionType(DEPOSIT)
				.balance(newBalance).build();
			bankAccountRepository.save(account);
			statementRepository.save(statement);
		}
		else {
			throw new TechnicalException("Deposit amount is less than minimum allowed");
		}
		
	}
	
}
