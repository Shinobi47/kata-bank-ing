package com.ing.kata.bank.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "CUSTOMER")
public class CustomerEntity {

	@Id
    @Column(name = "CUS_ID")
	private Long id;
	
    @Column(name = "CUS_NAME")
	private String name;
    
    @OneToOne(mappedBy = "customer")
	private AccountEntity account;

	@Override
	public String toString() {
		return "CustomerEntity [id=" + id + ", name=" + name + "]";
	}

    
}
