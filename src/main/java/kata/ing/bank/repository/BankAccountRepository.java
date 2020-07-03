package kata.ing.bank.repository;

import java.math.BigDecimal;
import java.util.Optional;

import com.ing.kata.bank.dto.AccountDto;
import com.ing.kata.bank.dto.CustomerDto;

public class BankAccountRepository {

	//temporary
	public Optional<AccountDto> findById(Long id) {
		return Optional.ofNullable(AccountDto.builder().id(id).currentBalance(new BigDecimal(10D)).customer(new CustomerDto()).build());
	}
	
	public void save(AccountDto account) {
		
	}
}
