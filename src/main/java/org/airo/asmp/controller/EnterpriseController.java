package org.airo.asmp.controller;

import lombok.RequiredArgsConstructor;
import org.airo.asmp.dto.entity.EnterpriseCreateDto;
import org.airo.asmp.dto.entity.EnterpriseFilterDto;
import org.airo.asmp.dto.entity.EnterpriseUpdateDto;
import org.airo.asmp.mapper.entity.EnterpriseMapper;
import org.airo.asmp.model.Admin;
import org.airo.asmp.model.entity.Enterprise;
import org.airo.asmp.repository.AdminRepository;
import org.airo.asmp.repository.entity.EnterpriseRepository;
import org.airo.asmp.service.FilterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/enterprise")
@RequiredArgsConstructor
public class EnterpriseController {
	private final EnterpriseRepository enterpriseRepository;
	private final EnterpriseMapper enterpriseMapper;
	private final AdminRepository adminRepository;
	private final FilterService filterService;

	@PostMapping
	public void add(@RequestBody EnterpriseCreateDto enterpriseDto) {
		var enterprise = enterpriseMapper.toEntity(enterpriseDto);
		enterpriseRepository.save(enterprise);
	}

	@PutMapping("/{id}")
	public void update(@PathVariable UUID id, @RequestBody EnterpriseUpdateDto enterpriseDto) {
		Optional<Enterprise> enterprise = enterpriseRepository.findById(id);
		if (enterprise.isEmpty()) {
			throw new RuntimeException("id为 %s 的企业不存在！".formatted(id));
		}
		var existingEnterprise = enterprise.get();
		enterpriseMapper.partialUpdate(enterpriseDto, existingEnterprise);
		enterpriseRepository.save(existingEnterprise);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable UUID id) {
		if (!enterpriseRepository.existsById(id)) {
			return;
		}
		enterpriseRepository.deleteById(id);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Enterprise> get(@PathVariable UUID id) {
		Optional<Enterprise> enterprise = enterpriseRepository.findById(id);
		if (enterprise.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(enterprise.get());
	}

	@GetMapping
	public List<Enterprise> getAllEnterprises() {
		return enterpriseRepository.findAll();
	}

	@GetMapping("/filter")
	public List<Enterprise> filter(
			@RequestBody EnterpriseFilterDto filterDto
	) {
		return filterService.filterEnterprise(filterDto);
	}

}
