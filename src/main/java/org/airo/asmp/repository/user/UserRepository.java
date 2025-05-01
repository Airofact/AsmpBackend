package org.airo.asmp.repository.user;

import org.airo.asmp.model.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, String> {
	Optional<User> findByUserName(String name);

	List<User> findUserByUserName(String userName);
}
