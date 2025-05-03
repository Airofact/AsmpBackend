package org.airo.asmp.controller;

import org.airo.asmp.model.user.Alumni;
import org.airo.asmp.model.user.Enterprise;
import org.airo.asmp.model.user.User;
import org.airo.asmp.repository.user.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TestController {
	UserRepository userRepository;

	public TestController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@PostMapping("/register/alumni")
	public void registerAlumni(@RequestBody Alumni alumni) {
		userRepository.save(alumni);
	}

	@PostMapping("/register/enterprise")
	public void registerEnterprise(@RequestBody Enterprise enterprise) {
		userRepository.save(enterprise);
	}

	@GetMapping("/user")
	public List<User> getUser(@RequestParam(name = "name") String name) {
		return userRepository.findUserByUserName(name);
	}
}
