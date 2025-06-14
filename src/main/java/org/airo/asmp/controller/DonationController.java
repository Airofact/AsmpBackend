package org.airo.asmp.controller;

import lombok.RequiredArgsConstructor;
import org.airo.asmp.dto.donation.DonationCreateDto;
import org.airo.asmp.dto.donation.DonationFilterDto;
import org.airo.asmp.dto.donation.DonationUpdateDto;
import org.airo.asmp.model.donation.Donation;
import org.airo.asmp.service.DonationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/donation-project/{donProjId}/donation")
@RequiredArgsConstructor
public class DonationController {
    
    private final DonationService donationService;    /**
     * 创建捐赠
     */
    @PostMapping
    public ResponseEntity<Donation> createDonation(@PathVariable UUID donProjId,
                                                  @Valid @RequestBody DonationCreateDto createDto) {        // 验证路径参数与DTO中的项目ID一致
        if (!donProjId.equals(createDto.projectId())) {
            return ResponseEntity.badRequest().build();
        }
        
        try {
            Donation donation = donationService.createDonation(createDto);
            return new ResponseEntity<>(donation, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
      /**
     * 更新捐赠
     */
    @PutMapping("/{donId}")
    public ResponseEntity<Donation> updateDonation(@PathVariable UUID donProjId,
                                                  @PathVariable UUID donId, 
                                                  @Valid @RequestBody DonationUpdateDto updateDto) {        try {
            // 验证捐赠属于指定的项目
            Donation existingDonation = donationService.findById(donId)
                    .orElse(null);
            if (existingDonation == null || !existingDonation.getProject().getId().equals(donProjId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            
            Donation donation = donationService.updateDonation(donId, updateDto);
            return ResponseEntity.ok(donation);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
      /**
     * 删除捐赠
     */
    @DeleteMapping("/{donId}")
    public ResponseEntity<Void> deleteDonation(@PathVariable UUID donProjId, @PathVariable UUID donId) {        try {
            // 验证捐赠属于指定的项目
            Donation existingDonation = donationService.findById(donId)
                    .orElse(null);
            if (existingDonation == null || !existingDonation.getProject().getId().equals(donProjId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            
            donationService.deleteDonation(donId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    /**
     * 根据ID查询捐赠
     */
    @GetMapping("/{donId}")
    public ResponseEntity<Donation> getDonationById(@PathVariable UUID donProjId, @PathVariable UUID donId) {        return donationService.findById(donId)
                .filter(donation -> donation.getProject().getId().equals(donProjId))
                .map(donation -> ResponseEntity.ok(donation))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
      /**
     * 查询项目的所有捐赠
     */
    @GetMapping
    public ResponseEntity<List<Donation>> getProjectDonations(@PathVariable UUID donProjId) {
        List<Donation> donations = donationService.findByProject(donProjId);
        return ResponseEntity.ok(donations);
    }      /**
     * 根据条件查询捐赠
     */    @PostMapping("/filter")
    public ResponseEntity<List<Donation>> searchDonations(@PathVariable UUID donProjId, 
                                                         @RequestBody DonationFilterDto filter) {
        List<Donation> donations = donationService.findByFilter(filter)
                .stream()
                .filter(donation -> donation.getProject().getId().equals(donProjId))
                .toList();
        return ResponseEntity.ok(donations);
    }/**
     * 根据捐赠者查询
     */
    @GetMapping("/donor/{donorId}")
    public ResponseEntity<List<Donation>> getDonationsByDonor(@PathVariable UUID donProjId, @PathVariable UUID donorId) {        List<Donation> donations = donationService.findByDonor(donorId)
                .stream()
                .filter(donation -> donation.getProject().getId().equals(donProjId))
                .toList();
        return ResponseEntity.ok(donations);
    }
      
    /**
     * 计算项目总捐赠金额
     */
    @GetMapping("/total")
    public ResponseEntity<BigDecimal> getProjectTotalAmount(@PathVariable UUID donProjId) {
        BigDecimal totalAmount = donationService.calculateProjectTotalAmount(donProjId);
        return ResponseEntity.ok(totalAmount);
    }
      /**
     * 确认捐赠
     */    @PostMapping("/{donId}/confirm")
    public ResponseEntity<Donation> confirmDonation(@PathVariable UUID donProjId, @PathVariable UUID donId) {        try {
            // 验证捐赠属于指定的项目
            Donation existingDonation = donationService.findById(donId)
                    .orElse(null);
            if (existingDonation == null || !existingDonation.getProject().getId().equals(donProjId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            
            Donation donation = donationService.confirmDonation(donId);
            return ResponseEntity.ok(donation);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    /**
     * 完成捐赠
     */    @PostMapping("/{donId}/complete")
    public ResponseEntity<Donation> completeDonation(@PathVariable UUID donProjId, @PathVariable UUID donId) {        try {
            // 验证捐赠属于指定的项目
            Donation existingDonation = donationService.findById(donId)
                    .orElse(null);
            if (existingDonation == null || !existingDonation.getProject().getId().equals(donProjId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            
            Donation donation = donationService.completeDonation(donId);
            return ResponseEntity.ok(donation);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }      /**
     * 根据状态查询捐赠
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Donation>> getDonationsByStatus(@PathVariable UUID donProjId, @PathVariable Donation.DonationStatus status) {        DonationFilterDto filter = new DonationFilterDto(
                null, null, null, null, null, 
                null, null, status, null, null, null, null);
        List<Donation> donations = donationService.findByFilter(filter)
                .stream()
                .filter(donation -> donation.getProject().getId().equals(donProjId))
                .toList();
        return ResponseEntity.ok(donations);
    }
      /**
     * 根据支付方式查询捐赠
     */
    @GetMapping("/payment/{paymentMethod}")
    public ResponseEntity<List<Donation>> getDonationsByPaymentMethod(@PathVariable UUID donProjId, @PathVariable Donation.PaymentMethod paymentMethod) {        DonationFilterDto filter = new DonationFilterDto(
                null, null, null, null, paymentMethod, 
                null, null, null, null, null, null, null);
        List<Donation> donations = donationService.findByFilter(filter)
                .stream()
                .filter(donation -> donation.getProject().getId().equals(donProjId))
                .toList();
        return ResponseEntity.ok(donations);
    }
}
