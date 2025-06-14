package org.airo.asmp.service;

import lombok.RequiredArgsConstructor;
import org.airo.asmp.dto.entity.BusinessEntityFilterDto;
import org.airo.asmp.model.entity.BusinessEntity;
import org.airo.asmp.repository.entity.RawBusinessEntityRepository;
import org.airo.asmp.util.SpecificationBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BusinessEntityService {
    
    private final RawBusinessEntityRepository businessEntityRepository;
    
    /**
     * 根据ID查询业务实体
     */
    public Optional<BusinessEntity> findById(UUID id) {
        return businessEntityRepository.findById(id);
    }
    
    /**
     * 查询所有业务实体
     */
    public List<BusinessEntity> findAll() {
        return businessEntityRepository.findAll();
    }
    
    /**
     * 业务实体过滤查询
     */
    public List<BusinessEntity> findByFilter(BusinessEntityFilterDto filterDto) {
        if (filterDto == null) {
            return findAll();
        }
        
        return businessEntityRepository.findAll((root, query, builder) ->
            SpecificationBuilder.of(root, builder)
                .dateTimeAfterOrEqual("addedAt", filterDto.getAddedAtBegin())
                .dateTimeBefore("addedAt", filterDto.getAddedAtEnd())
                .build()
        );
    }
}
