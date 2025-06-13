package org.airo.asmp.controller;

import lombok.RequiredArgsConstructor;
import org.airo.asmp.dto.AdminDto;
import org.airo.asmp.mapper.AdminMapper;
import org.airo.asmp.model.Admin;
import org.airo.asmp.repository.AdminRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/admin")
@RequiredArgsConstructor
public class AdminController {
	private final AdminRepository adminRepository;
	private final AdminMapper adminMapper;
	@PostMapping("/register")
	private ResponseEntity<String> register(@Valid @RequestBody AdminDto adminDto) {
		if (adminRepository.existsByUsername(adminDto.username())) {
			return ResponseEntity.badRequest().body("用户名重复");
		}
		return ResponseEntity.ok(adminRepository.save(adminMapper.toEntity(adminDto)).getId().toString());
	}

	@PostMapping("/login")
	private ResponseEntity<String> login(@RequestBody AdminDto adminDto) {
		Optional<Admin> admin = adminRepository.findByUsernameAndPassword(
				adminDto.username(),
				adminDto.password()
		);
		if (admin.isPresent()) {
			return ResponseEntity.ok(admin.get().getId().toString());
		}
		if (adminRepository.existsByUsername(adminDto.username())){
			return ResponseEntity.badRequest().body("密码错误");
		}
		return ResponseEntity.badRequest().body("用户名不存在");
	}

	@GetMapping
	private ResponseEntity<List<Admin>> getAll() {
		return ResponseEntity.ok(adminRepository.findAll());
	}
}
