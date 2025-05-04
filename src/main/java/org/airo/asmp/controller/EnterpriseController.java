package org.airo.asmp.controller;

import lombok.RequiredArgsConstructor;
import org.airo.asmp.dto.entity.EnterpriseCreateDto;
import org.airo.asmp.dto.entity.EnterpriseFilterDto;
import org.airo.asmp.dto.entity.EnterpriseUpdateDto;
import org.airo.asmp.mapper.entity.EnterpriseMapper;
import org.airo.asmp.model.entity.Enterprise;
import org.airo.asmp.repository.entity.EnterpriseRepository;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

@RestController
@RequestMapping("/api/enterprise")
@RequiredArgsConstructor
public class EnterpriseController {
	private final EnterpriseRepository enterpriseRepository;
	private final EnterpriseMapper enterpriseMapper;

	@PostMapping("/add")
	public void add(@RequestBody EnterpriseCreateDto enterpriseDto) {
		enterpriseRepository.save(enterpriseMapper.toEntity(enterpriseDto));
	}

	@PostMapping("/update")
	public void update(@RequestBody EnterpriseUpdateDto enterpriseDto) {
		enterpriseRepository.save(enterpriseMapper.toEntity(enterpriseDto));
	}

	@GetMapping("/filter")
	public List<Enterprise> filter(
			@RequestBody EnterpriseFilterDto filterDto
	) {
		return enterpriseRepository.findAll((root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();

			// ID 精确匹配
			if (filterDto.id() != null) {
				predicates.add((Predicate) builder.equal(root.get("id"), filterDto.id()));
			}

			// 添加人ID 关联查询
			if (filterDto.addedById() != null) {
				predicates.add(builder.equal(
						root.get("addedBy").get("id"),
						filterDto.addedById()
				));
			}

			// 名称模糊查询（忽略大小写）
			if (StringUtils.hasText(filterDto.name())) {
				predicates.add(builder.like(
						builder.lower(root.get("name")),
						"%" + filterDto.name().toLowerCase() + "%"
				));
			}

			// 领域精确匹配
			if (StringUtils.hasText(filterDto.field())) {
				predicates.add(builder.equal(root.get("field"), filterDto.field()));
			}

			// 地址模糊查询
			if (StringUtils.hasText(filterDto.address())) {
				predicates.add(builder.like(
						builder.lower(root.get("address")),
						"%" + filterDto.address().toLowerCase() + "%"
				));
			}

			// 联系人模糊查询
			if (StringUtils.hasText(filterDto.contactPerson())) {
				predicates.add(builder.like(
						builder.lower(root.get("contactPerson")),
						"%" + filterDto.contactPerson().toLowerCase() + "%"
				));
			}

			// 联系邮箱精确匹配
			if (StringUtils.hasText(filterDto.contactEmail())) {
				predicates.add(builder.equal(root.get("contactEmail"), filterDto.contactEmail()));
			}

			// 联系电话精确匹配
			if (StringUtils.hasText(filterDto.contactPhone())) {
				predicates.add(builder.equal(root.get("contactPhone"), filterDto.contactPhone()));
			}

			// 添加时间范围
			if (filterDto.addedAtBegin() != null) {
				predicates.add(builder.greaterThanOrEqualTo(
						root.get("createdAt"),
						filterDto.addedAtBegin()
				));
			}
			if (filterDto.addedAtEnd() != null) {
				predicates.add(builder.lessThanOrEqualTo(
						root.get("createdAt"),
						filterDto.addedAtEnd()
				));
			}

			return builder.and(predicates.toArray(new Predicate[0]));
		});
	}

}
