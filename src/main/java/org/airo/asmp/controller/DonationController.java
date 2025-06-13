package org.airo.asmp.controller;

import lombok.RequiredArgsConstructor;
import org.airo.asmp.dto.donation.DonationCreateDto;
import org.airo.asmp.dto.donation.DonationFilterDto;
import org.airo.asmp.dto.donation.DonationUpdateDto;
import org.airo.asmp.model.donation.Donation;
import org.airo.asmp.service.DonationService;
import org.airo.asmp.service.FilterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/donations")
@RequiredArgsConstructor
public class DonationController {
    
    private final DonationService donationService;
    private final FilterService filterService;

    /**
     * 创建捐赠
     */
    @PostMapping
    public ResponseEntity<Donation> createDonation(@Valid @RequestBody DonationCreateDto createDto) {
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
    @PutMapping("/{id}")
    public ResponseEntity<Donation> updateDonation(@PathVariable UUID id, 
                                                  @Valid @RequestBody DonationUpdateDto updateDto) {
        try {
            Donation donation = donationService.updateDonation(id, updateDto);
            return ResponseEntity.ok(donation);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    /**
     * 删除捐赠
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDonation(@PathVariable UUID id) {
        try {
            donationService.deleteDonation(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    /**
     * 根据ID查询捐赠
     */
    @GetMapping("/{id}")
    public ResponseEntity<Donation> getDonationById(@PathVariable UUID id) {
        return donationService.findById(id)
                .map(donation -> ResponseEntity.ok(donation))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
      /**
     * 查询所有捐赠
     */
    @GetMapping
    public ResponseEntity<List<Donation>> getAllDonations() {
        List<Donation> donations = donationService.findAll();
        return ResponseEntity.ok(donations);
    }
      /**
     * 根据条件查询捐赠
     */
    @PostMapping("/filter")
    public ResponseEntity<List<Donation>> searchDonations(@RequestBody DonationFilterDto filter) {
        List<Donation> donations = filterService.filterDonation(filter);
        return ResponseEntity.ok(donations);
    }
      /**
     * 根据捐赠者查询
     */
    @GetMapping("/donor/{donorId}")
    public ResponseEntity<List<Donation>> getDonationsByDonor(@PathVariable UUID donorId) {
        List<Donation> donations = donationService.findByDonor(donorId);
        return ResponseEntity.ok(donations);
    }
      /**
     * 根据项目查询
     */
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Donation>> getDonationsByProject(@PathVariable UUID projectId) {
        List<Donation> donations = donationService.findByProject(projectId);
        return ResponseEntity.ok(donations);
    }
    
    /**
     * 计算项目总捐赠金额
     */
    @GetMapping("/project/{projectId}/total")
    public ResponseEntity<BigDecimal> getProjectTotalAmount(@PathVariable UUID projectId) {
        BigDecimal totalAmount = donationService.calculateProjectTotalAmount(projectId);
        return ResponseEntity.ok(totalAmount);
    }
    
    /**
     * 确认捐赠
     */    @PostMapping("/{id}/confirm")
    public ResponseEntity<Donation> confirmDonation(@PathVariable UUID id) {
        try {
            Donation donation = donationService.confirmDonation(id);
            return ResponseEntity.ok(donation);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    /**
     * 完成捐赠
     */    @PostMapping("/{id}/complete")
    public ResponseEntity<Donation> completeDonation(@PathVariable UUID id) {
        try {
            Donation donation = donationService.completeDonation(id);
            return ResponseEntity.ok(donation);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
      /**
     * 根据状态查询捐赠
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Donation>> getDonationsByStatus(@PathVariable Donation.DonationStatus status) {
        DonationFilterDto filter = new DonationFilterDto(
                null, null, null, null, null, 
                null, null, status, null, null, null, null);
        List<Donation> donations = filterService.filterDonation(filter);
        return ResponseEntity.ok(donations);
    }
      /**
     * 根据支付方式查询捐赠
     */
    @GetMapping("/payment/{paymentMethod}")
    public ResponseEntity<List<Donation>> getDonationsByPaymentMethod(@PathVariable Donation.PaymentMethod paymentMethod) {
        DonationFilterDto filter = new DonationFilterDto(
                null, null, null, null, paymentMethod, 
                null, null, null, null, null, null, null);
        List<Donation> donations = filterService.filterDonation(filter);
        return ResponseEntity.ok(donations);
    }
}
