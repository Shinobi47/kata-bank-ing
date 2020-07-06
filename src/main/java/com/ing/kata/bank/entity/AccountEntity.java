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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "ACCOUNT")
public class AccountEntity {

	@Id
	@Column(name = "ACT_ID")
	private Long id;
	
	@Column(name = "ACT_CUR_BLN")
	private BigDecimal currentBalance;
		
	@OneToOne
	@JoinColumn(name = "ACT_CUS_ID")
	private CustomerEntity customer;
	
	@OneToMany(mappedBy="account")
	private List<StatementEntity> statements;

	@Override
	public String toString() {
		return "AccountEntity [id=" + id + ", currentBalance=" + currentBalance + ", customer=" + customer + "]";
	}
	
	
}
