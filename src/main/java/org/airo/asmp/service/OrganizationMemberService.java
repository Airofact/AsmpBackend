package org.airo.asmp.service;

import lombok.RequiredArgsConstructor;
import org.airo.asmp.dto.entity.AlumniFilterDto;
import org.airo.asmp.dto.entity.OrganizationMemberCreateDto;
import org.airo.asmp.dto.entity.OrganizationMemberFilterDto;
import org.airo.asmp.dto.entity.OrganizationMemberUpdateDto;
import org.airo.asmp.mapper.entity.OrganizationMemberMapper;
import org.airo.asmp.model.entity.Alumni;
import org.airo.asmp.model.entity.Organization;
import org.airo.asmp.model.entity.OrganizationAlumniId;
import org.airo.asmp.model.entity.OrganizationMember;
import org.airo.asmp.repository.entity.BusinessEntityRepository;
import org.airo.asmp.repository.OrganizationMemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrganizationMemberService {
    
    private final OrganizationMemberRepository organizationMemberRepository;
    private final BusinessEntityRepository<Organization> organizationRepository;
    private final BusinessEntityRepository<Alumni> alumniRepository;
    private final OrganizationMemberMapper organizationMemberMapper;

    /**
     * 添加组织成员
     */
    @Transactional
    public OrganizationMember addMember(UUID organizationId, OrganizationMemberCreateDto dto) {
        // 验证组织是否存在
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new RuntimeException("组织不存在"));
        
        // 验证校友是否存在
        Alumni alumni = alumniRepository.findById(dto.alumniId())
                .orElseThrow(() -> new RuntimeException("校友不存在"));
        
        // 检查成员关系是否已存在
        OrganizationAlumniId id = new OrganizationAlumniId();
        id.setOrganizationId(organizationId);
        id.setAlumniId(dto.alumniId());
        
        if (organizationMemberRepository.existsById(id)) {
            throw new RuntimeException("该校友已经是组织成员");
        }
        
        // 创建成员关系
        OrganizationMember member = new OrganizationMember();
        member.setId(id);
        member.setOrganization(organization);
        member.setAlumni(alumni);
        member.setRole(dto.role());
        member.setJoinTime(LocalDateTime.now());
        
        return organizationMemberRepository.save(member);
    }
    
    /**
     * 更新组织成员信息
     */
    @Transactional
    public OrganizationMember updateMember(UUID organizationId, UUID alumniId, OrganizationMemberUpdateDto dto) {
        OrganizationAlumniId id = new OrganizationAlumniId();
        id.setOrganizationId(organizationId);
        id.setAlumniId(alumniId);
        
        OrganizationMember member = organizationMemberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("组织成员关系不存在"));
        
        organizationMemberMapper.partialUpdate(dto, member);
        return organizationMemberRepository.save(member);
    }
    
    /**
     * 移除组织成员
     */
    @Transactional
    public void removeMember(UUID organizationId, UUID alumniId) {
        OrganizationAlumniId id = new OrganizationAlumniId();
        id.setOrganizationId(organizationId);
        id.setAlumniId(alumniId);
        
        if (!organizationMemberRepository.existsById(id)) {
            throw new RuntimeException("组织成员关系不存在");
        }
        
        organizationMemberRepository.deleteById(id);
    }
    
    /**
     * 获取组织成员信息
     */
    public Optional<OrganizationMember> getMember(UUID organizationId, UUID alumniId) {
        OrganizationAlumniId id = new OrganizationAlumniId();
        id.setOrganizationId(organizationId);
        id.setAlumniId(alumniId);
        
        return organizationMemberRepository.findById(id);
    }
    
    /**
     * 根据校友真实姓名查询所有相关的组织名称
     */
    public List<String> getOrganizationNamesByAlumniRealName(String realName) {
        List<OrganizationMember> members = organizationMemberRepository.findByAlumniRealName(realName);
        return members.stream()
                .map(member -> member.getOrganization().getName())
                .collect(Collectors.toList());
    }
    
    /**
     * 根据组织名称查询所有相关的校友真实姓名
     */
    public List<String> getAlumniRealNamesByOrganizationName(String organizationName) {
        List<OrganizationMember> members = organizationMemberRepository.findByOrganizationName(organizationName);
        return members.stream()
                .map(member -> member.getAlumni().getRealName())
                .collect(Collectors.toList());
    }
    
    /**
     * 获取某个组织下的所有成员
     */
    public List<OrganizationMember> getMembersByOrganizationId(UUID organizationId) {
        return organizationMemberRepository.findByOrganizationId(organizationId);
    }
    
    /**
     * 获取某个校友参与的所有组织
     */
    public List<OrganizationMember> getMembersByAlumniId(UUID alumniId) {
        return organizationMemberRepository.findByAlumniId(alumniId);
    }
    
    /**
     * 获取所有组织成员（全局查询）
     */
    public List<OrganizationMember> getAllMembers() {
        return organizationMemberRepository.findAll();
    }
    
    /**
     * 全局组织成员过滤查询（不限组织）
     */
    public List<OrganizationMember> findAllByFilter(OrganizationMemberFilterDto filter) {
        List<OrganizationMember> allMembers = organizationMemberRepository.findAll();
        
        return allMembers.stream()
                .filter(member -> matchesFilter(member, filter))
                .collect(Collectors.toList());
    }
    
    /**
     * 根据过滤条件查询组织成员
     */
    public List<OrganizationMember> findByFilter(UUID organizationId, OrganizationMemberFilterDto filter) {
        List<OrganizationMember> members = organizationMemberRepository.findByOrganizationId(organizationId);
        
        return members.stream()
                .filter(member -> matchesFilter(member, filter))
                .collect(Collectors.toList());
    }
    
    /**
     * 检查成员是否符合过滤条件
     */
    private boolean matchesFilter(OrganizationMember member, OrganizationMemberFilterDto filter) {
        if (filter == null) {
            return true;
        }
        
        // 角色过滤
        if (filter.role() != null && !filter.role().equals(member.getRole())) {
            return false;
        }
        
        // 加入时间范围过滤
        if (filter.joinTimeBegin() != null && member.getJoinTime() != null && 
            member.getJoinTime().isBefore(filter.joinTimeBegin())) {
            return false;
        }
        
        if (filter.joinTimeEnd() != null && member.getJoinTime() != null && 
            member.getJoinTime().isAfter(filter.joinTimeEnd())) {
            return false;
        }
        
        // 校友过滤（如果提供了校友过滤条件）
        if (filter.alumniFilter() != null) {
            Alumni alumni = member.getAlumni();
            if (!matchesAlumniFilter(alumni, filter.alumniFilter())) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 检查校友是否符合过滤条件
     */
    private boolean matchesAlumniFilter(Alumni alumni, AlumniFilterDto filter) {
        if (filter.getStudentId() != null && !filter.getStudentId().equals(alumni.getStudentId())) {
            return false;
        }
        
        if (filter.getRealName() != null && !alumni.getRealName().contains(filter.getRealName())) {
            return false;
        }
        
        if (filter.getGender() != null && !filter.getGender().equals(alumni.getGender())) {
            return false;
        }
        
        if (filter.getCompanyName() != null && !alumni.getCompanyName().contains(filter.getCompanyName())) {
            return false;
        }
        
        if (filter.getCurrentJob() != null && !alumni.getCurrentJob().contains(filter.getCurrentJob())) {
            return false;
        }
        
        return true;
    }
}
