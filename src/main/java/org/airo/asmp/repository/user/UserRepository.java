package org.airo.asmp.repository.user;

import jakarta.annotation.Nullable;
import org.airo.asmp.model.Gender;
import org.airo.asmp.model.user.Alumni;
import org.airo.asmp.model.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public interface UserRepository extends CrudRepository<User, String> {
	Optional<User> findByUserName(String name);

	List<User> findUserByUserName(String userName);

	default List<Alumni> findByFilters(
			@Nullable Integer minAge,
			@Nullable Integer maxAge,
			@Nullable Gender gender,
			@Nullable String companyName,
			@Nullable Alumni.Status status) {
		return StreamSupport.stream(findAll().spliterator(), false)
				.filter(alumni -> alumni instanceof Alumni)
				.map(alumni -> (Alumni) alumni)
				.filter(alumni -> minAge == null || alumni.getAge() >= minAge)
				.filter(alumni -> maxAge == null || alumni.getAge() <= maxAge)
				.filter(alumni -> gender == null || alumni.getGender() == gender)
				.filter(alumni -> companyName == null || companyName.isEmpty() || alumni.getCompanyName().equals(companyName))
				.filter(alumni -> status == null || alumni.getStatus() == status)
				.toList();
	}
}
