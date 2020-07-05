package kata.ing.bank.repository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import com.ing.kata.bank.dto.StatementDto;

public class StatementRepository {
	
	public void save(StatementDto statement) {
		//temp
	}
	
	public List<StatementDto> findByAccount_id(Long id){
		return Arrays.asList(StatementDto.builder().amount(new BigDecimal(10)).build());
	}

}
