package org.airo.asmp.controller;

import lombok.RequiredArgsConstructor;
import org.airo.asmp.dto.entity.OrganizationMemberCreateDto;
import org.airo.asmp.dto.entity.OrganizationMemberFilterDto;
import org.airo.asmp.dto.entity.OrganizationMemberUpdateDto;
import org.airo.asmp.model.entity.OrganizationMember;
import org.airo.asmp.service.OrganizationMemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/organization/{orgId}/member")
@RequiredArgsConstructor
public class OrganizationMemberController {
    
    private final OrganizationMemberService organizationMemberService;
    
    /**
     * 添加组织成员
     * POST /api/organization/{orgId}/member
     */
    @PostMapping
    public ResponseEntity<OrganizationMember> addMember(
            @PathVariable UUID orgId,
            @Valid @RequestBody OrganizationMemberCreateDto createDto
    ) {
        if (organizationMemberService.isMember(orgId, createDto.alumniId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

            OrganizationMember member = organizationMemberService.addMember(orgId, createDto);
            return new ResponseEntity<>(member, HttpStatus.CREATED);
    }
    
    /**
     * 获取组织的所有成员
     * GET /api/organization/{orgId}/member
     */
    @GetMapping
    public ResponseEntity<List<OrganizationMember>> getOrganizationMembers(@PathVariable UUID orgId) {
        List<OrganizationMember> members = organizationMemberService.getMembersByOrganizationId(orgId);
        return ResponseEntity.ok(members);
    }
    
    /**
     * 获取特定成员信息
     * GET /api/organization/{orgId}/member/{memId}
     */
    @GetMapping("/{memId}")
    public ResponseEntity<OrganizationMember> getMember(@PathVariable UUID orgId,
                                                       @PathVariable UUID memId) {
        return organizationMemberService.getMember(orgId, memId)
                .map(member -> ResponseEntity.ok(member))
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * 更新成员信息
     * PUT /api/organization/{orgId}/member/{memId}
     */
    @PutMapping("/{memId}")
    public ResponseEntity<OrganizationMember> updateMember(@PathVariable UUID orgId,
                                                          @PathVariable UUID memId,
                                                          @Valid @RequestBody OrganizationMemberUpdateDto updateDto) {

            OrganizationMember member = organizationMemberService.updateMember(orgId, memId, updateDto);
            return ResponseEntity.ok(member);
    }
    
    /**
     * 移除组织成员
     * DELETE /api/organization/{orgId}/member/{memId}
     */
    @DeleteMapping("/{memId}")
    public ResponseEntity<Void> removeMember(@PathVariable UUID orgId,
                                           @PathVariable UUID memId) {

            organizationMemberService.removeMember(orgId, memId);
            return ResponseEntity.noContent().build();

    }
    
    /**
     * 根据过滤条件查询组织成员
     * POST /api/organization/{orgId}/member/search
     */
    @PostMapping("/filter")
    public ResponseEntity<List<OrganizationMember>> searchMembers(@PathVariable UUID orgId,
                                                                 @RequestBody OrganizationMemberFilterDto filter) {
        List<OrganizationMember> members = organizationMemberService.findByFilter(orgId, filter);
        return ResponseEntity.ok(members);
    }
    
    /**
     * 根据角色查询组织成员
     * GET /api/organization/{orgId}/member/role/{role}
     */
    @GetMapping("/role/{role}")
    public ResponseEntity<List<OrganizationMember>> getMembersByRole(@PathVariable UUID orgId,
                                                                    @PathVariable OrganizationMember.Role role) {
        OrganizationMemberFilterDto filter = new OrganizationMemberFilterDto(null, null, role, null, null);
        List<OrganizationMember> members = organizationMemberService.findByFilter(orgId, filter);
        return ResponseEntity.ok(members);
    }
}
