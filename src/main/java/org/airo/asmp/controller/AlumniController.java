package org.airo.asmp.controller;

import lombok.RequiredArgsConstructor;
import org.airo.asmp.dto.entity.AlumniCreateDto;
import org.airo.asmp.dto.entity.AlumniFilterDto;
import org.airo.asmp.dto.entity.AlumniUpdateDto;
import org.airo.asmp.mapper.entity.AlumniMapper;
import org.airo.asmp.model.entity.Alumni;
import org.airo.asmp.repository.entity.AlumniRepository;
import org.airo.asmp.service.AlumniService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/alumni")
@RequiredArgsConstructor
public class AlumniController {
    private final AlumniRepository alumniRepository;
    private final AlumniMapper alumniMapper;
    private final AlumniService alumniService;    //校友注册
    @PostMapping
    public ResponseEntity<Alumni> add(@Valid @RequestBody AlumniCreateDto alumniCreateDto) {
        Alumni alumni = alumniMapper.toEntity(alumniCreateDto);
        Alumni savedAlumni = alumniRepository.save(alumni);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAlumni);
    }

    // 校友信息修改
    @PutMapping("/{id}")
    public ResponseEntity<Alumni> update(@PathVariable UUID id, @Valid @RequestBody AlumniUpdateDto alumniUpdateDto) {
        var alumni = alumniRepository.findById(id);
        if (alumni.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Alumni existingAlumni = alumni.get();
        alumniMapper.partialUpdate(alumniUpdateDto, existingAlumni);
        Alumni updatedAlumni = alumniRepository.save(existingAlumni);
        return ResponseEntity.ok(updatedAlumni);
    }    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!alumniRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

            alumniRepository.deleteById(id);
            return ResponseEntity.noContent().build();

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
        return alumniService.findByFilter(alumniFilterDto);
    }
}

