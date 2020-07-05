package com.ing.kata.bank.entity;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Data;

@Data @Builder
@Entity
@Table(name = "MAILING_SPONSOR")
public class AccountEntity {

	@Id
	@Column(name = "ACT_ID")
	private Long id;
	
	@Column(name = "ACT_CUR_BLN")
	private BigDecimal currentBalance;
	
	@OneToOne
	@JoinColumn(name = "ACT_CUS_ID")//, referencedColumnName = "id")
	private CustomerEntity customer;
	
	@OneToMany(mappedBy="account")
	private List<StatementEntity> statements;
}
