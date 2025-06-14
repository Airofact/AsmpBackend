package org.airo.asmp.service;

import lombok.RequiredArgsConstructor;
import org.airo.asmp.dto.entity.OrganizationCreateDto;
import org.airo.asmp.dto.entity.OrganizationFilterDto;
import org.airo.asmp.dto.entity.OrganizationMemberCreateDto;
import org.airo.asmp.dto.entity.OrganizationUpdateDto;
import org.airo.asmp.mapper.entity.OrganizationMapper;
import org.airo.asmp.model.entity.Alumni;
import org.airo.asmp.model.entity.Organization;
import org.airo.asmp.model.entity.OrganizationMember;
import org.airo.asmp.repository.entity.AlumniRepository;
import org.airo.asmp.repository.entity.OrganizationRepository;
import org.airo.asmp.util.OptionalSpecificationBuilder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrganizationService {
    
    private final OrganizationRepository organizationRepository;
    private final AlumniRepository alumniRepository;
    private final AlumniService alumniService;
    private final OrganizationMapper organizationMapper;
    private final OrganizationMemberService organizationMemberService;

    /**
     * 根据ID查询组织
     */
    public Optional<Organization> findById(UUID id) {
        return organizationRepository.findById(id);
    }
    
    /**
     * 查询所有组织
     */
    public List<Organization> findAll() {
        return organizationRepository.findAll();
    }
    
    /**
     * 组织过滤查询
     */
    public List<Organization> findByFilter(OrganizationFilterDto filterDto) {
        if (filterDto == null) {
            return findAll();
        }
        
        var creators = filterDto.getCreatorFilter() != null ? 
            alumniService.findByFilter(filterDto.getCreatorFilter()) : null;
              return organizationRepository.findAll((root, query, builder) ->
            OptionalSpecificationBuilder.of(root, builder)
                    .dateTimeAfterOrEqual("addedAt", filterDto.getAddedAtBegin())
                    .dateTimeBefore("addedAt", filterDto.getAddedAtEnd())
                    .like("name", filterDto.getName())
                    .equal("type", filterDto.getType())
                    .like("description", filterDto.getDescription())
                    .in("creator", creators)
                    .build()
        );
    }
    
    /**
     * 创建组织
     */
    @Transactional
    public Organization create(OrganizationCreateDto organizationCreateDto) {
        Organization organization = organizationMapper.toEntity(organizationCreateDto);
        //validate creatorId
        Optional<Alumni> alumniOptional = alumniRepository.findById(organizationCreateDto.creatorId());
        if (alumniOptional.isEmpty()) {
            throw new RuntimeException("Creator not found");
        }
        organization.setCreator(alumniOptional.get());
        var savedOrganization = organizationRepository.save(organization);
        OrganizationMemberCreateDto memberCreateDto = new OrganizationMemberCreateDto(
                organizationCreateDto.creatorId(),
                OrganizationMember.Role.CREATOR
        );
        organizationMemberService.addMember(savedOrganization.getId(), memberCreateDto);
        return savedOrganization;
    }
    
    /**
     * 更新组织
     */
    @Transactional
    public Organization update(UUID id, OrganizationUpdateDto organizationUpdateDto) {
        var organization = organizationRepository.findById(id);
        if (organization.isEmpty()) {
            throw new RuntimeException("Organization not found");
        }

        Organization existingOrganization = organization.get();
        organizationMapper.partialUpdate(organizationUpdateDto, existingOrganization);
        
        // 注意：创建者(creator)不能被修改，因此不处理creatorId
        
        return organizationRepository.save(existingOrganization);
    }
    
    /**
     * 删除组织
     */
    @Transactional
    public void delete(UUID id) {
        if (!organizationRepository.existsById(id)) {
            throw new RuntimeException("Organization not found");
        }

            organizationRepository.deleteById(id);

    }
    
    /**
     * 检查组织是否存在
     */
    public boolean existsById(UUID id) {
        return organizationRepository.existsById(id);
    }
    
    /**
     * 根据名称搜索组织
     */
    public List<Organization> searchByName(String name) {
        return organizationRepository.findByName(name);
    }
}
