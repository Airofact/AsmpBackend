package org.airo.asmp.controller;

import lombok.RequiredArgsConstructor;
import org.airo.asmp.dto.entity.OrganizationMemberCreateDto;
import org.airo.asmp.model.OrganizationMember.OrganizationAlumniId;
import org.airo.asmp.model.OrganizationMember.OrganizationMember;
import org.airo.asmp.model.OrganizationMember.Role;
import org.airo.asmp.model.entity.Alumni;
import org.airo.asmp.model.entity.Organization;
import org.airo.asmp.repository.OrganizationMemberRepository;
import org.airo.asmp.repository.entity.AlumniRepository;
import org.airo.asmp.repository.entity.OrganizationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/organizationMember")
public class OrganizationMemberController {
    private final OrganizationMemberRepository organizationMemberRepository;
    private final OrganizationRepository organizationRepository;
    private final AlumniRepository alumniRepository;

    @PostMapping("/add")
    public ResponseEntity<String> addOrganizationMember(@RequestBody OrganizationMemberCreateDto dto) {
        Alumni alumni = alumniRepository.findById(dto.id().getAlumniId()).orElseThrow(() -> new RuntimeException("不存在该学生"));
        Organization organization = organizationRepository.findById(dto.id().getOrganizationId()).orElseThrow(() -> new RuntimeException("不存在该组织"));

        OrganizationAlumniId id = new OrganizationAlumniId();
        id.setOrganizationId(organization.getId());
        id.setAlumniId(alumni.getId());

        OrganizationMember member = new OrganizationMember();
        member.setId(id);
        member.setOrganization(organization);
        member.setAlumni(alumni);
        member.setRole(dto.role());
        member.setJoinTime(LocalDateTime.now());
        organizationMemberRepository.save(member);
        return ResponseEntity.ok("添加成功");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteByOrganizationAndAlumni(
            @RequestParam UUID organizationId,
            @RequestParam UUID alumniId) {

        OrganizationAlumniId id = new OrganizationAlumniId();
        id.setOrganizationId(organizationId);
        id.setAlumniId(alumniId);

        if (organizationMemberRepository.existsById(id)) {
            organizationMemberRepository.deleteById(id);
            return ResponseEntity.ok("删除成功");
        } else {
            return ResponseEntity.ok("记录不存在");
        }
    }
    // 按 AlumniId 查询
    @GetMapping("/byAlumni")
    public List<OrganizationMember> findByAlumniId(@RequestParam("alumniId") UUID alumniId) {
        return organizationMemberRepository.findByAlumniId(alumniId);
    }

    // 按 organizationId 查询
    @GetMapping("/byOrganization")
    public List<OrganizationMember> findByOrganizationId(@RequestParam("organizationId") UUID organizationId) {
        return organizationMemberRepository.findByOrganizationId(organizationId);
    }

    // 根据 alumni.realName 查询关联的 organization.name
    @GetMapping("/organizationsByAlumniName")
    public List<String> getOrganizationsByAlumniName(@RequestParam("realName") String realName) {
        return organizationMemberRepository.findOrganizationNamesByAlumniRealName(realName);
    }

    // 根据 organization.name 查询关联的 alumni.realName
    @GetMapping("/alumniByOrganizationName")
    public List<String> getAlumniByOrganizationName(@RequestParam("organizationName") String organizationName) {
        return organizationMemberRepository.findAlumniRealNamesByOrganizationName(organizationName);
    }
    //查询所有
    @GetMapping("/getAll")
    public ResponseEntity<List<OrganizationMember>> getAllOrganizationMembers() {
        List<OrganizationMember> list = organizationMemberRepository.findAll();
        return ResponseEntity.ok(list);
    }
}

