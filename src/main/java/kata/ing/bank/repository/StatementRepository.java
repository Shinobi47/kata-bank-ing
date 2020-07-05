package kata.ing.bank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ing.kata.bank.entity.StatementEntity;

public interface StatementRepository extends JpaRepository<StatementEntity, Long> {
	
	public List<StatementEntity> findByAccount_id(Long id);

}
