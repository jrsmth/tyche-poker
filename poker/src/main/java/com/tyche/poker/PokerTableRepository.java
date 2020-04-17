package com.tyche.poker;

import com.tyche.poker.model.PokerTable;
import org.springframework.data.repository.CrudRepository;

public interface PokerTableRepository extends CrudRepository<PokerTable, Integer> {

    PokerTable findByUuid(String uuid);


}
