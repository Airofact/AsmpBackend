package org.airo.asmp.controller;

import lombok.RequiredArgsConstructor;
import org.airo.asmp.dto.entity.AlumniCreateDto;
import org.airo.asmp.dto.entity.AlumniFilterDto;
import org.airo.asmp.dto.entity.AlumniUpdateDto;
import org.airo.asmp.mapper.entity.AlumniMapper;
import org.airo.asmp.model.Admin;
import org.airo.asmp.model.entity.Alumni;
import org.airo.asmp.repository.AdminRepository;
import org.airo.asmp.repository.entity.AlumniRepository;
import org.airo.asmp.service.FilterService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/alumni")
@RequiredArgsConstructor
public class AlumniController {
    private final AdminRepository adminRepository;
    private final AlumniRepository alumniRepository;
    private final AlumniMapper alumniMapper;
    private final FilterService filterService;

    //校友注册
    @PostMapping
    public void add(@Valid @RequestBody AlumniCreateDto alumniCreateDto) {
        Alumni alumni = alumniMapper.toEntity(alumniCreateDto);
        alumniRepository.save(alumni);
    }

    // 校友信息修改
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @Valid @RequestBody AlumniUpdateDto alumniUpdateDto) {
        var alumni = alumniRepository.findById(id);
        if (alumni.isEmpty()) {
            return ResponseEntity.badRequest().body("id为 %s 的校友不存在！".formatted(id));
        }

        Alumni existingAlumni = alumni.get();
        alumniMapper.partialUpdate(alumniUpdateDto, existingAlumni);
        alumniRepository.save(existingAlumni);
        return ResponseEntity.ok("校友信息修改成功！");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        if (!alumniRepository.existsById(id)) {
            return ResponseEntity.badRequest().body("id为 %s 的校友不存在！".formatted(id));
        }
        alumniRepository.deleteById(id);
        return ResponseEntity.ok("校友删除成功！");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Alumni> findById(@PathVariable UUID id) {
        var alumni = alumniRepository.findById(id);
        if (alumni.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Alumni existingAlumni = alumni.get();
        return ResponseEntity.ok(existingAlumni);
    }

    @GetMapping
    public ResponseEntity<List<Alumni>> findAll() {
        return ResponseEntity.ok(alumniRepository.findAll());
    }

    //根据用户名查询校友
    @GetMapping("/search")
    public List<Alumni> search(@RequestParam String realName) {
        return alumniRepository.findByRealName(realName);
    }

    // 校友分组查询
    @PostMapping("/filter")
    public List<Alumni> filter(@RequestBody AlumniFilterDto alumniFilterDto) {
        return filterService.filterAlumni(alumniFilterDto);
    }
}

