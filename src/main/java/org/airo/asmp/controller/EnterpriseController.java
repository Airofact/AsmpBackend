package org.airo.asmp.controller;

import lombok.RequiredArgsConstructor;
import org.airo.asmp.dto.entity.EnterpriseCreateDto;
import org.airo.asmp.dto.entity.EnterpriseFilterDto;
import org.airo.asmp.dto.entity.EnterpriseUpdateDto;
import org.airo.asmp.mapper.entity.EnterpriseMapper;
import org.airo.asmp.model.entity.Enterprise;
import org.airo.asmp.repository.entity.EnterpriseRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.airo.asmp.service.EnterpriseService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/enterprise")
@RequiredArgsConstructor
public class EnterpriseController {
	private final EnterpriseRepository enterpriseRepository;
	private final EnterpriseMapper enterpriseMapper;
	private final EnterpriseService enterpriseService;
	@PostMapping
	public ResponseEntity<Enterprise> add(@RequestBody EnterpriseCreateDto enterpriseDto) {
		var enterprise = enterpriseMapper.toEntity(enterpriseDto);
		Enterprise savedEnterprise = enterpriseRepository.save(enterprise);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedEnterprise);
	}
	@PutMapping("/{id}")
	public ResponseEntity<Enterprise> update(@PathVariable UUID id, @RequestBody EnterpriseUpdateDto enterpriseDto) {
		Optional<Enterprise> enterprise = enterpriseRepository.findById(id);
		if (enterprise.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		var existingEnterprise = enterprise.get();
		enterpriseMapper.partialUpdate(enterpriseDto, existingEnterprise);
		Enterprise updatedEnterprise = enterpriseRepository.save(existingEnterprise);
		return ResponseEntity.ok(updatedEnterprise);
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable UUID id) {
		if (!enterpriseRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}

			enterpriseRepository.deleteById(id);
			return ResponseEntity.noContent().build();

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

	@PostMapping("/filter")
	public List<Enterprise> filter(
			@RequestBody EnterpriseFilterDto filterDto
	) {
		return enterpriseService.findByFilter(filterDto);
	}

}
