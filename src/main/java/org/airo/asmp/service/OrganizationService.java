package org.airo.asmp.service;

import lombok.RequiredArgsConstructor;
import org.airo.asmp.dto.entity.OrganizationFilterDto;
import org.airo.asmp.model.entity.Organization;
import org.airo.asmp.repository.entity.OrganizationRepository;
import org.airo.asmp.util.OptionalSpecificationBuilder;
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
    private final AlumniService alumniService;
    
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
}
