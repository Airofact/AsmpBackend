package org.airo.asmp.repository;

import org.airo.asmp.model.donation.Donation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface DonationRepository extends JpaRepository<Donation, UUID>, JpaSpecificationExecutor<Donation> {
    
    // 根据捐赠者查询
    List<Donation> findByDonorId(UUID donorId);
    Page<Donation> findByDonorId(UUID donorId, Pageable pageable);
    
    // 根据项目查询
    List<Donation> findByProjectId(UUID projectId);
    Page<Donation> findByProjectId(UUID projectId, Pageable pageable);
    
    // 根据状态查询
    List<Donation> findByStatus(Donation.DonationStatus status);
    Page<Donation> findByStatus(Donation.DonationStatus status, Pageable pageable);
    
    // 根据支付方式查询
    List<Donation> findByPaymentMethod(Donation.PaymentMethod paymentMethod);
    
    // 根据金额范围查询
    List<Donation> findByAmountBetween(BigDecimal minAmount, BigDecimal maxAmount);
    Page<Donation> findByAmountBetween(BigDecimal minAmount, BigDecimal maxAmount, Pageable pageable);
    
    // 根据捐赠时间范围查询
    List<Donation> findByDonateTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
    Page<Donation> findByDonateTimeBetween(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);
    
    // 根据是否匿名查询
    List<Donation> findByAnonymous(Boolean anonymous);
    Page<Donation> findByAnonymous(Boolean anonymous, Pageable pageable);
      // 根据项目和状态查询
    List<Donation> findByProjectIdAndStatus(UUID projectId, Donation.DonationStatus status);
    
    // 根据捐赠者和状态查询
    List<Donation> findByDonorIdAndStatus(UUID donorId, Donation.DonationStatus status);
    
    // 根据交易号查询
    Donation findByTransactionId(String transactionId);
    
    // 查询某个时间段内的所有捐赠
    List<Donation> findByCreatedAtBetween(LocalDateTime startTime, LocalDateTime endTime);
}
