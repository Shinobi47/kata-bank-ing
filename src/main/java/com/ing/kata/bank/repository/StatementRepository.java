package com.ing.kata.bank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ing.kata.bank.entity.StatementEntity;

@Repository
public interface StatementRepository extends JpaRepository<StatementEntity, Long> {
	
	public List<StatementEntity> findByAccount_id(Long id);

}
