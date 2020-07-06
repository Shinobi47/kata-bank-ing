package kata.ing.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ing.kata.bank.entity.AccountEntity;

@Repository
public interface BankAccountRepository extends JpaRepository<AccountEntity, Long> {

}
