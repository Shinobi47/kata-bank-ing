package com.ing.kata.bank.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.OneToOne;

public class CustomerEntity {

	@Id
    @Column(name = "CUS_ID")
	private Long id;
	
    @Column(name = "CUS_NAME")
	private String name;
    
    @OneToOne(mappedBy = "customer")
	private AccountEntity account;

}
