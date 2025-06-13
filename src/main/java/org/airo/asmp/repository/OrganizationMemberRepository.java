package org.airo.asmp.repository;
import org.airo.asmp.model.entity.OrganizationAlumniId;
import org.airo.asmp.model.entity.OrganizationMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface OrganizationMemberRepository extends JpaRepository<OrganizationMember, OrganizationAlumniId>, JpaSpecificationExecutor<OrganizationMember> {
    // 查询某个组织下的所有成员
    List<OrganizationMember> findByOrganizationId(UUID organizationId);

    // 查询某个校友参与的所有组织
    List<OrganizationMember> findByAlumniId(UUID alumniId);
    
    // 根据校友真实姓名查询组织成员记录
    List<OrganizationMember> findByAlumniRealName(String realName);
    
    // 根据组织名称查询组织成员记录
    List<OrganizationMember> findByOrganizationName(String organizationName);
}
