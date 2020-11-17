package com.sat.data.tacos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.sat.tacos.User;

@CrossOrigin(origins = "*")
public interface UserRepository extends CrudRepository<User,Long> {
	
	User findByUsername(String username);
}
