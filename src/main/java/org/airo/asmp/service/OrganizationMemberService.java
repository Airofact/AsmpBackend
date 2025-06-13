package org.airo.asmp.service;

import lombok.RequiredArgsConstructor;
import org.airo.asmp.model.entity.OrganizationMember;
import org.airo.asmp.repository.OrganizationMemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrganizationMemberService {
    
    private final OrganizationMemberRepository organizationMemberRepository;
    
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
}
