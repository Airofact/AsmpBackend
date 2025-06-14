package org.airo.asmp.controller;

import lombok.RequiredArgsConstructor;
import org.airo.asmp.dto.entity.OrganizationCreateDto;
import org.airo.asmp.dto.entity.OrganizationFilterDto;
import org.airo.asmp.dto.entity.OrganizationMemberFilterDto;
import org.airo.asmp.dto.entity.OrganizationUpdateDto;
import org.airo.asmp.mapper.entity.OrganizationMapper;
import org.airo.asmp.model.entity.Alumni;
import org.airo.asmp.model.entity.Organization;
import org.airo.asmp.model.entity.OrganizationMember;
import org.airo.asmp.repository.entity.AlumniRepository;
import org.airo.asmp.repository.entity.OrganizationRepository;
import org.airo.asmp.service.OrganizationMemberService;
import org.airo.asmp.service.OrganizationService;
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
	private final OrganizationRepository organizationRepository;
	private final AlumniRepository alumniRepository;
	private final OrganizationService organizationService;
	private final OrganizationMemberService organizationMemberService;
	private final OrganizationMapper organizationMapper;

	// 组织注册
	@PostMapping
	public ResponseEntity<String> add(@Valid @RequestBody OrganizationCreateDto organizationCreateDto) {
		Organization organization = organizationMapper.toEntity(organizationCreateDto);
		//validate creatorId
		Optional<Alumni> alumniOptional = alumniRepository.findById(organizationCreateDto.creatorId());
		if (alumniOptional.isEmpty()) {
			return ResponseEntity.badRequest().body("校友不存在");
		}
		organization.setCreator(alumniOptional.get());
		organizationRepository.save(organization);
		return ResponseEntity.ok("组织添加成功！");
	}

	// 组织信息修改
	@PutMapping("/{id}")
	public ResponseEntity<String> update(@PathVariable UUID id, @Valid @RequestBody OrganizationUpdateDto organizationUpdateDto) {
		var organization = organizationRepository.findById(id);
		if (organization.isEmpty()) {
			return ResponseEntity.badRequest().body("id为 %s 的组织不存在！".formatted(id));
		}

		Organization existingOrganization = organization.get();
		organizationMapper.partialUpdate(organizationUpdateDto, existingOrganization);
		
		// 注意：创建者(creator)不能被修改，因此不处理creatorId
		
		organizationRepository.save(existingOrganization);
		return ResponseEntity.ok("组织信息修改成功！");
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> delete(@PathVariable UUID id) {
		if (!organizationRepository.existsById(id)) {
			return ResponseEntity.badRequest().body("id为 %s 的组织不存在！".formatted(id));
		}
		organizationRepository.deleteById(id);
		return ResponseEntity.ok("组织删除成功！");
	}

	// 根据ID查询组织
	@GetMapping("/{id}")
	public ResponseEntity<Organization> getById(@PathVariable UUID id) {
		Optional<Organization> organization = organizationRepository.findById(id);
		return organization.map(ResponseEntity::ok)
				          .orElse(ResponseEntity.notFound().build());
	}

	@GetMapping
	public ResponseEntity<List<Organization>> getAll() {
		return ResponseEntity.ok(organizationRepository.findAll());
	}

	// 通过name查询
	@GetMapping("/search")
	public List<Organization> searchByName(@RequestParam("name") String name){
		return organizationRepository.findByName(name);
	}	// 组织分组查询
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
