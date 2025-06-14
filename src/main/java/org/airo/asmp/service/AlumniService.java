package org.airo.asmp.service;

import lombok.RequiredArgsConstructor;
import org.airo.asmp.dto.entity.AlumniFilterDto;
import org.airo.asmp.model.entity.Alumni;
import org.airo.asmp.repository.entity.AlumniRepository;
import org.airo.asmp.util.OptionalSpecificationBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlumniService {
    
    private final AlumniRepository alumniRepository;
    
    /**
     * 根据ID查询校友
     */
    public Optional<Alumni> findById(UUID id) {
        return alumniRepository.findById(id);
    }
    
    /**
     * 查询所有校友
     */
    public List<Alumni> findAll() {
        return alumniRepository.findAll();
    }
    
    /**
     * 校友过滤查询
     */
    public List<Alumni> findByFilter(AlumniFilterDto filterDto) {
        if (filterDto == null) {
            return findAll();
        }
        
        return alumniRepository.findAll((root, query, builder) ->
            OptionalSpecificationBuilder.of(root, builder)
                .dateTimeAfterOrEqual("addedAt", filterDto.getAddedAtBegin())
                .dateTimeBefore("addedAt", filterDto.getAddedAtEnd())
                .equal("studentId", filterDto.getStudentId())
                .equal("realName", filterDto.getRealName())
                .equal("gender", filterDto.getGender())
                .dateAfterOrEqual("dateOfBirth", filterDto.getDateOfBirthBegin())
                .dateBefore("dateOfBirth", filterDto.getDateOfBirthEnd())
                .equal("address", filterDto.getAddress())
                .equal("companyName", filterDto.getCompanyName())
                .equal("currentJob", filterDto.getCurrentJob())
                .build()
        );
    }
}
