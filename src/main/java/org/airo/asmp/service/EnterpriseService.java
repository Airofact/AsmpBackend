package org.airo.asmp.service;

import lombok.RequiredArgsConstructor;
import org.airo.asmp.dto.entity.EnterpriseFilterDto;
import org.airo.asmp.model.entity.Enterprise;
import org.airo.asmp.repository.entity.EnterpriseRepository;
import org.airo.asmp.util.SpecificationBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EnterpriseService {
    
    private final EnterpriseRepository enterpriseRepository;
    
    /**
     * 根据ID查询企业
     */
    public Optional<Enterprise> findById(UUID id) {
        return enterpriseRepository.findById(id);
    }
    
    /**
     * 查询所有企业
     */
    public List<Enterprise> findAll() {
        return enterpriseRepository.findAll();
    }
    
    /**
     * 企业过滤查询
     */
    public List<Enterprise> findByFilter(EnterpriseFilterDto filterDto) {
        if (filterDto == null) {
            return findAll();
        }
        
        return enterpriseRepository.findAll((root, query, builder) ->
            SpecificationBuilder.of(root, builder)
                .like("name", filterDto.getName())
                .equal("field", filterDto.getField())
                .like("address", filterDto.getAddress())
                .like("contactPerson", filterDto.getContactPerson())
                .equal("contactEmail", filterDto.getContactEmail())
                .equal("contactPhone", filterDto.getContactPhone())
                .dateTimeAfterOrEqual("addedAt", filterDto.getAddedAtBegin())
                .dateTimeBefore("addedAt", filterDto.getAddedAtEnd())
                .build()
        );
    }
}
