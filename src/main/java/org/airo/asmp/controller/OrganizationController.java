package org.airo.asmp.controller;

import lombok.RequiredArgsConstructor;
import org.airo.asmp.dto.entity.OrganizationCreateDto;
import org.airo.asmp.dto.entity.OrganizationFilterDto;
import org.airo.asmp.dto.entity.OrganizationMemberFilterDto;
import org.airo.asmp.dto.entity.OrganizationUpdateDto;
import org.airo.asmp.model.entity.Organization;
import org.airo.asmp.model.entity.OrganizationMember;
import org.airo.asmp.service.OrganizationMemberService;
import org.airo.asmp.service.OrganizationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/organization")
@RequiredArgsConstructor
public class OrganizationController {	
	private final OrganizationService organizationService;
	private final OrganizationMemberService organizationMemberService;

	// 组织注册
	@PostMapping
	public ResponseEntity<Organization> add(@Valid @RequestBody OrganizationCreateDto organizationCreateDto) {

			Organization savedOrganization = organizationService.create(organizationCreateDto);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedOrganization);
	}

	// 组织信息修改
	@PutMapping("/{id}")
	public ResponseEntity<Organization> update(@PathVariable UUID id, @Valid @RequestBody OrganizationUpdateDto organizationUpdateDto) {

			Organization updatedOrganization = organizationService.update(id, organizationUpdateDto);
			return ResponseEntity.ok(updatedOrganization);

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable UUID id) {
			organizationService.delete(id);
			return ResponseEntity.noContent().build();
	}

	// 根据ID查询组织
	@GetMapping("/{id}")
	public ResponseEntity<Organization> getById(@PathVariable UUID id) {
		Optional<Organization> organization = organizationService.findById(id);
		return organization.map(ResponseEntity::ok)
				          .orElse(ResponseEntity.notFound().build());
	}

	@GetMapping
	public ResponseEntity<List<Organization>> getAll() {
		return ResponseEntity.ok(organizationService.findAll());
	}

	// 通过name查询
	@GetMapping("/search")
	public List<Organization> searchByName(@RequestParam("name") String name){
		return organizationService.searchByName(name);
	}

	// 组织分组查询
	@PostMapping("/filter")
	public List<Organization> filter(@RequestBody OrganizationFilterDto organizationFilterDto) {
		return organizationService.findByFilter(organizationFilterDto);
	}
	
	// ===== 全局组织成员相关端点 =====
	
	/**
	 * 获取所有组织成员（不限组织）
	 */
	@GetMapping("/member")
	public ResponseEntity<List<OrganizationMember>> getAllMembers() {
		List<OrganizationMember> members = organizationMemberService.getAllMembers();
		return ResponseEntity.ok(members);
	}
	
	/**
	 * 全局组织成员过滤查询（不限组织）
	 */
	@PostMapping("/member/filter")
	public ResponseEntity<List<OrganizationMember>> filterMembers(
			@RequestBody OrganizationMemberFilterDto filterDto) {
		List<OrganizationMember> members = organizationMemberService.findAllByFilter(filterDto);
		return ResponseEntity.ok(members);
	}
}
