package com.ing.kata.bank.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ing.kata.bank.dto.AccountDto;
import com.ing.kata.bank.dto.StatementDto;
import com.ing.kata.bank.dto.TransactionDto;
import com.ing.kata.bank.exception.TechnicalException;
import com.ing.kata.bank.service.BankAccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/v1")
public class BankAccountController {

	private BankAccountService bankAccountService; 
	
	@Operation(summary = "Executes a DEPOSIT or WITHDRAW transaction with the given parameters")
	@ApiResponses(value = { 
			  @ApiResponse(responseCode = "201", description = "Transaction Successful, Statement created", 
			    content = { @Content(mediaType = "application/json") }),
			  @ApiResponse(responseCode = "400", description = "Bad Request, check your input parameters", 
			    content = @Content) })
	@PostMapping(path = "/statements")
	public ResponseEntity<?> postTransaction(@RequestBody TransactionDto transaction) {
		try {
			bankAccountService.executeTransaction(transaction.getTransactionType(), transaction.getAccountId(), transaction.getAmount());
			return new ResponseEntity<>(HttpStatus.CREATED);
		}catch(TechnicalException | IllegalArgumentException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@Operation(summary = "Gets Statements (Or transaction History) of a given accoun")
	@ApiResponses(value = { 
			  @ApiResponse(responseCode = "200", description = "Found the transaction history for this account", 
			    content = { @Content(mediaType = "application/json", 
			      schema = @Schema(implementation = StatementDto.class)) }),
			  @ApiResponse(responseCode = "404", description = "No transaction history found", 
			    content = @Content),
			  @ApiResponse(responseCode = "400", description = "Bad request, check parameters", 
			    content = @Content)})
	@GetMapping(path = "/accounts/{id}/statements")
	public ResponseEntity<?> getStatements(@Parameter(description="Id of the account.",
            required=true)@PathVariable Long id) {
		try {
		List<StatementDto> statements = bankAccountService.fetchTransactionsHistory(id);
		return new ResponseEntity<>(statements, statements.isEmpty() ? HttpStatus.NOT_FOUND : HttpStatus.OK);
		
		}catch(TechnicalException | IllegalArgumentException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@Operation(summary = "Gets an account through which we can consult Balance")
	@ApiResponses(value = { 
			  @ApiResponse(responseCode = "200", description = "Found the Account", 
			    content = { @Content(mediaType = "application/json", 
			      schema = @Schema(implementation = AccountDto.class)) }),
			  @ApiResponse(responseCode = "400", description = "Bad request, check the parameters", 
			    content = @Content) })
	@GetMapping(path = "/accounts/{id}")
	public ResponseEntity<?> getAccount(@PathVariable Long id) {
		try {
		AccountDto account = bankAccountService.fetchAccount(id);
		return new ResponseEntity<>(account, HttpStatus.OK);
		
		}catch(TechnicalException | IllegalArgumentException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
}
