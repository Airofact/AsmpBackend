package org.airo.asmp.controller;

import lombok.RequiredArgsConstructor;
import org.airo.asmp.dto.AdminDto;
import org.airo.asmp.mapper.AdminMapper;
import org.airo.asmp.model.Admin;
import org.airo.asmp.repository.AdminRepository;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/admin")
@RequiredArgsConstructor
public class AdminController {
	private final AdminRepository adminRepository;
	private final AdminMapper adminMapper;

	@PostMapping("/register")
	private ResponseEntity<UUID> register(@RequestBody AdminDto adminDto) {
		if (adminRepository.existsByUsername(adminDto.username())) {
			return ResponseEntity.badRequest().body(null);
		}
		return ResponseEntity.ok(adminRepository.save(adminMapper.toEntity(adminDto)).getId());
	}

	@PostMapping("/login")
	private ResponseEntity<UUID> login(@RequestBody AdminDto adminDto) {
		Optional<Admin> admin = adminRepository.findByUsernameAndPassword(
				adminDto.username(),
				adminDto.password()
		);
		if (admin.isPresent()) {
			return ResponseEntity.ok(admin.get().getId());
		} else {
			return ResponseEntity.badRequest().body(null);
		}
	}
}
