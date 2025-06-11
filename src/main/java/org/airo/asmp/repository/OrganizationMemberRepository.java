package org.airo.asmp.repository;
import org.airo.asmp.model.OrganizationMember.OrganizationAlumniId;
import org.airo.asmp.model.OrganizationMember.OrganizationMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OrganizationMemberRepository extends JpaRepository<OrganizationMember, OrganizationAlumniId> {
    // 查询某个组织下的所有成员
    List<OrganizationMember> findByOrganizationId(UUID organizationId);

    // 查询某个校友参与的所有组织
    List<OrganizationMember> findByAlumniId(UUID alumniId);

    @Query("SELECT om.organization.name FROM OrganizationMember om WHERE om.alumni.realName = :realName")
    List<String> findOrganizationNamesByAlumniRealName(@Param("realName") String realName);

    @Query("SELECT om.alumni.realName FROM OrganizationMember om WHERE om.organization.name = :organizationName")
    List<String> findAlumniRealNamesByOrganizationName(@Param("organizationName") String organizationName);
}
