package org.airo.asmp.controller;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.airo.asmp.dto.entity.AlumniCreateDto;
import org.airo.asmp.dto.entity.AlumniFilterDto;
import org.airo.asmp.dto.entity.AlumniUpdateDto;
import org.airo.asmp.mapper.entity.AlumniMapper;
import org.airo.asmp.model.entity.Alumni;

import org.airo.asmp.repository.entity.AlumniRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/alumni")
@RequiredArgsConstructor
public class AlumniController {
    private final AlumniRepository alumniRepository;
    private final AlumniMapper alumniMapper;

    //校友注册
    @PostMapping("/add")
    public void add(@Valid @RequestBody AlumniCreateDto alumniCreateDto) {
        alumniRepository.save(alumniMapper.toEntity(alumniCreateDto));
    }

    // 校友信息修改
    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @Valid @RequestBody AlumniUpdateDto alumniUpdateDto) {
        var alumni = alumniRepository.findById(id);
        if (alumni.isEmpty()) {
            return ResponseEntity.badRequest().body("校友信息不存在！");
        }

        Alumni existingAlumni = alumni.get();
        alumniMapper.partialUpdate(alumniUpdateDto, existingAlumni);
        alumniRepository.save(existingAlumni);
        return ResponseEntity.ok("校友信息修改成功！");
    }

    //根据用户名查询校友
    @GetMapping("/search")
    public List<Alumni> search(@RequestParam String realName) {
        return alumniRepository.findByRealName(realName);
    }

    // 校友分组查询
    @GetMapping("/filter")
    public List<Alumni> filter(
            @RequestBody AlumniFilterDto alumniFilterDto
    ) {
        return alumniRepository.findAll((root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(alumniFilterDto.id() != null){
                predicates.add(builder.equal(root.get("id"), alumniFilterDto.id()));
            }
            if(alumniFilterDto.addedAtBegin() != null){
                predicates.add(builder.greaterThanOrEqualTo(root.get("addedAt"), alumniFilterDto.addedAtBegin()));
            }
            if(alumniFilterDto.addedAtEnd() != null){
                predicates.add(builder.lessThan(root.get("addedAt"), alumniFilterDto.addedAtEnd()));
            }
            if(alumniFilterDto.studentId() != null){
                predicates.add(builder.equal(root.get("studentId"), alumniFilterDto.studentId()));
            }
            if(alumniFilterDto.realName() != null){
                predicates.add(builder.equal(root.get("realName"), alumniFilterDto.realName()));
            }
            if(alumniFilterDto.gender() != null){
                predicates.add(builder.equal(root.get("gender"), alumniFilterDto.gender()));
            }
            if(alumniFilterDto.dateOfBirthBegin() != null){
                predicates.add(builder.greaterThanOrEqualTo(root.get("dateOfBirth"), alumniFilterDto.dateOfBirthBegin()));
            }
            if(alumniFilterDto.dateOfBirthEnd() != null){
                predicates.add(builder.lessThan(root.get("dateOfBirth"), alumniFilterDto.dateOfBirthEnd()));
            }
            if(alumniFilterDto.address() != null){
                predicates.add(builder.equal(root.get("address"), alumniFilterDto.address()));
            }
            if(alumniFilterDto.companyName() != null){
                predicates.add(builder.equal(root.get("companyName"), alumniFilterDto.companyName()));
            }
            if(alumniFilterDto.currentJob() != null){
                predicates.add(builder.equal(root.get("currentJob"), alumniFilterDto.currentJob()));
            }
            return builder.and(predicates.toArray(new Predicate[0]));
		});
    }
}

