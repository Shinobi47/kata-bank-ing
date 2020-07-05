package com.ing.kata.bank.service;

import static com.ing.kata.bank.constant.Constants.MINIMAL_DEPOSIT;
import static com.ing.kata.bank.enums.TransactionType.DEPOSIT;
import static com.ing.kata.bank.enums.TransactionType.WITHDRAW;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ing.kata.bank.dto.AccountDto;
import com.ing.kata.bank.dto.StatementDto;
import com.ing.kata.bank.entity.AccountEntity;
import com.ing.kata.bank.entity.StatementEntity;
import com.ing.kata.bank.enums.TransactionType;
import com.ing.kata.bank.exception.TechnicalException;
import com.ing.kata.bank.utils.DataMapper;

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
	private DataMapper dataMapper;
	
	public AccountDto fetchAccount(Long accountId) {
		Assert.notNull(accountId, "Cannot find account with null accountId");
		
		return bankAccountRepository
				.findById(accountId)
				.map(dataMapper::toDto)
				.orElseThrow(() -> new TechnicalException("No account with the specified id"));
		
	}
	
	public List<StatementDto> fetchTransactionsHistory(Long accountId){
		Assert.notNull(accountId, "Cannot find transactions history with null accountId");
		
		List<StatementEntity> statements = statementRepository.findByAccount_id(accountId);
		return statements.stream()
				.map(dataMapper::toDto)
				.collect(Collectors.toList());
	}
	
	public void executeTransaction(TransactionType transactionType, Long accountId, BigDecimal amount) {
		
		Assert.notNull(accountId, "Cannot perform transaction with null accountId");
		Assert.notNull(amount, "Cannot perform transaction with null amount");
		Assert.isTrue(isPositive(amount), "Cannot perform transaction with negative amount");
		
		AccountEntity account = bankAccountRepository
				.findById(accountId)
				.orElseThrow(() -> new TechnicalException("No account available with the specified accountId"));
		
		if(DEPOSIT.equals(transactionType)) {
			depositAmount(amount, account);
		}
		else if(WITHDRAW.equals(transactionType)) {
			withdrawAmount(amount, account);
		}
	}

	
	private void withdrawAmount(BigDecimal amount, AccountEntity account) {
		BigDecimal currentBalance = account.getCurrentBalance();
		
		if(amountToWithdrawDoesntCauseOverdraft(amount, currentBalance)) {
			
			log.info("Withdrawing the amount : {} from the account : {}", amount, account.toString());
			BigDecimal newBalance = currentBalance.subtract(amount);
			persistTransactionDetails(amount, account, newBalance, WITHDRAW);
		}
		
		else {
			throw new TechnicalException("Withdraw not allowed because you are poor");
		}
	}


	
	private void depositAmount(BigDecimal amount, AccountEntity account) {
		
		if(amount.compareTo(MINIMAL_DEPOSIT) == 1) {
			
			log.info("Depositing the amount : {} on the account : {}", amount, account.toString());
			BigDecimal newBalance = account.getCurrentBalance().add(amount);
			persistTransactionDetails(amount, account, newBalance, DEPOSIT);
			
		}
		else {
			throw new TechnicalException("Deposit amount is less than minimum allowed");
		}
		
	}

	private boolean isPositive(BigDecimal amount) {
		return amount.compareTo(BigDecimal.ZERO) > 0;
	}

	private boolean amountToWithdrawDoesntCauseOverdraft(BigDecimal amount, BigDecimal currentBalance) {
		return currentBalance.compareTo(amount) == 1 || currentBalance.compareTo(amount) == 0;
	}
	
	private void persistTransactionDetails(BigDecimal amount, AccountEntity account, BigDecimal newBalance, TransactionType transactionType) {
		account.setCurrentBalance(newBalance);
		StatementEntity statement = StatementEntity.builder()
			.amount(amount)
			.date(LocalDateTime.now())
			.transactionType(transactionType)
			.balance(newBalance)
			.account(account).build();
		bankAccountRepository.save(account);
		statementRepository.save(statement);
	}

	
}
