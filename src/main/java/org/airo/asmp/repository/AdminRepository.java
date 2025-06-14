package org.airo.asmp.repository;

import org.airo.asmp.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AdminRepository extends JpaRepository<Admin, UUID> {
	Optional<Admin> findByUsernameAndPassword(String username, String password);

	boolean existsByUsername(String username);
}
