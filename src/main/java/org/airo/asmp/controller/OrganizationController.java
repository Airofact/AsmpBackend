package org.airo.asmp.controller;


import lombok.RequiredArgsConstructor;
import org.airo.asmp.dto.entity.OrganizationCreateDto;
import org.airo.asmp.dto.entity.OrganizationUpdateDto;
import org.airo.asmp.model.Admin;
import org.airo.asmp.model.entity.Alumni;
import org.airo.asmp.model.entity.Organization;
import org.airo.asmp.model.entity.Type;
import org.airo.asmp.repository.AdminRepository;
import org.airo.asmp.repository.entity.AlumniRepository;
import org.airo.asmp.repository.entity.OrganizationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/organization")
@RequiredArgsConstructor
public class OrganizationController {
    private final OrganizationRepository organizationRepository;
    private final AlumniRepository alumniRepository;
    private final AdminRepository adminRepository;

    //增加组织
     @PostMapping("/add")
     public ResponseEntity<?> createOrganization(@RequestBody OrganizationCreateDto dto) {
         Admin admin = adminRepository.findById(dto.addedById())
                 .orElseThrow(() -> new RuntimeException("Admin not found"));
         Alumni alumni = alumniRepository.findById(dto.alumni()).orElseThrow(() -> new RuntimeException("Alumni not found"));
         Organization organization = new Organization();
         organization.setName(dto.name());
         organization.setDescription(dto.description());
         organization.setType(Type.valueOf(dto.type()));
         organization.setAddedBy(admin);
         organization.setAlumni(alumni);
         if (alumni != null) {
             alumni.setAddedBy(admin);
             organization.setAlumni(alumni);
         }
         Organization saved = organizationRepository.save(organization);
         return ResponseEntity.ok("添加成功");
     }
     //查询
     @GetMapping("/search/{id}")
    public ResponseEntity<Organization> search(@PathVariable("id") UUID id) {
        if(organizationRepository.existsById(id)) {
            Optional<Organization> organization = organizationRepository.findById(id);
            return ResponseEntity.ok(organization.get());
        }
        else {return ResponseEntity.ok(null);}
     }
     //通过name查询
     @GetMapping("/search/byName")
     public List<Organization> searchByName(@RequestParam("name") String name){
         return organizationRepository.findByName(name);
     }
//查询所有组织
    @GetMapping("/getAll")
    public ResponseEntity<List<Organization>> getAllOrganizations() {
        List<Organization> list = organizationRepository.findAll();
        return ResponseEntity.ok(list);
    }
     //删除
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteAlumni(@PathVariable("id") UUID id) {
        if(organizationRepository.existsById(id)) {
            organizationRepository.deleteById(id);
            return ResponseEntity.ok("删除成功");
        }
        else {return ResponseEntity.ok("删除失败，不存在该数据");}
    }
    //修改
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateAlumni(@RequestBody OrganizationUpdateDto newData, @PathVariable("id") UUID id) {
        Admin admin = adminRepository.findById(newData.addedById())
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        if(organizationRepository.existsById(id)) {
            Optional<Organization> oldDate = organizationRepository.findById(id);
            Organization organization = oldDate.get();
            organization.setName(newData.name());
            organization.setDescription(newData.description());
            organization.setType(Type.valueOf(newData.type()));
            organization.setAddedBy(admin);
            organization.setAlumni(organization.getAlumni());
            organizationRepository.save(organization);
            return ResponseEntity.ok("修改成功");
        }
        else{return ResponseEntity.ok("修改失败，未找到该组织");}
    }
}
