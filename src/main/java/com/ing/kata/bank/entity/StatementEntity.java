package com.ing.kata.bank.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ing.kata.bank.enums.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "STATEMENTS")
public class StatementEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "statment_generator")
	@SequenceGenerator(name="statment_generator", sequenceName = "STATEMENTS_PK_SEQ", allocationSize = 1)
	@Column(name = "STM_ID")
	private Long id;
	
	@Column(name = "STM_TRAN_DATE")
	private LocalDateTime date;
	
	@Column(name = "STM_TRAN_AMT")
	private BigDecimal amount;
	
	@Column(name = "STM_BLN")
	private BigDecimal balance;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "STM_TRAN_TYPE")
	private TransactionType transactionType;
	
	@ManyToOne
    @JoinColumn(name="STM_ACT_ID")
	private AccountEntity account;
}
