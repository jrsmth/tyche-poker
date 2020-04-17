package com.tyche.poker;

import com.tyche.poker.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

    User findByUuid(String uuid);


}
