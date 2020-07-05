package kata.ing.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ing.kata.bank.entity.AccountEntity;

public interface BankAccountRepository extends JpaRepository<AccountEntity, Long> {

}
