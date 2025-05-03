package org.airo.asmp.controller;

import org.airo.asmp.model.Gender;
import org.airo.asmp.model.user.Alumni;

import org.airo.asmp.model.user.User;
import org.airo.asmp.repository.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/alumni")
public class AlumniController {

    private final UserRepository userRepository;

    public AlumniController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //校友注册
    @PostMapping("/register")
    public ResponseEntity<String> registerAlumni(@Valid @RequestBody Alumni alumni) {
        userRepository.save(alumni);
        return ResponseEntity.ok("校友注册成功！");
    }
    
    // 校友信息修改
    @PutMapping("/{id}")
    public ResponseEntity<String> updateAlumni(@PathVariable String id, @Valid @RequestBody Alumni updatedAlumni) {
        Optional<User> existingUser = userRepository.findByUserName(updatedAlumni.getUserName());
        if (existingUser.isPresent()) {
            Alumni alumni = (Alumni) existingUser.get();
            alumni.setRealName(updatedAlumni.getRealName());
            alumni.setGender(updatedAlumni.getGender());
            alumni.setDateOfBirth(updatedAlumni.getDateOfBirth());
            alumni.setAddress(updatedAlumni.getAddress());
            alumni.setCompanyName(updatedAlumni.getCompanyName());
            alumni.setCurrentJob(updatedAlumni.getCurrentJob());
    
            userRepository.save(alumni);
            return ResponseEntity.ok("校友信息更新成功！");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("未找到指定校友！");
        }
    }

    //根据用户名查询校友
    @GetMapping("/search")
    public ResponseEntity<?> searchAlumni(@RequestParam String userName) {
        List<User> users = userRepository.findUserByUserName(userName);
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("未找到指定校友！");
        }
        return ResponseEntity.ok(users);
    }
        
    // 校友分组查询
    @GetMapping("/group")
    public ResponseEntity<?> groupAlumni(
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) Gender gender,
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) Alumni.Status status) {
        List<Alumni> alumniList = userRepository.findByFilters(minAge, maxAge, gender, companyName, status);
        if (alumniList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("未找到符合条件的校友！");
        }
        return ResponseEntity.ok(alumniList);
    }

        // 校友审核
        @PutMapping("/{id}/approve")
        public ResponseEntity<String> approveAlumni(@PathVariable String id) {
            Optional<User> existingUser = userRepository.findById(id);
            if (existingUser.isPresent() && existingUser.get() instanceof Alumni) {
                Alumni alumni = (Alumni) existingUser.get();
                alumni.setStatus(Alumni.Status.APPROVED);
                userRepository.save(alumni);
                return ResponseEntity.ok("校友审核通过！");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("未找到指定校友或该用户不是校友！");
            }
        }
}

