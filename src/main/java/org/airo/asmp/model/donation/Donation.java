package org.airo.asmp.model.donation;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.airo.asmp.model.entity.BusinessEntity;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Donation {
    
    @Id
    @UuidGenerator
    private UUID id;
    
    // 捐赠者 - 可以是校友、企业或其他BusinessEntity
    @ManyToOne
    @JoinColumn(name = "donor_id", nullable = false)
    @NotNull
    private BusinessEntity donor;
    
    // 捐赠项目
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    @NotNull
    private DonationProject project;
    
    // 捐赠金额
    @Column(nullable = false, precision = 15, scale = 2)
    @NotNull
    @Positive
    private BigDecimal amount;
    
    // 支付方式
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(50)")
    private PaymentMethod paymentMethod;
    
    // 捐赠时间
    @Column(nullable = false)
    @NotNull
    private LocalDateTime donateTime = LocalDateTime.now();
    
    // 备注
    @Column(columnDefinition = "text")
    private String remark;
    
    // 捐赠状态
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DonationStatus status = DonationStatus.PENDING;
    
    // 交易号
    @Column(columnDefinition = "varchar(100)")
    private String transactionId;
    
    // 是否匿名捐赠
    @Column(nullable = false)
    private Boolean anonymous = false;
    
    // 创建时间
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    // 更新时间
    @Column
    private LocalDateTime updatedAt;
    
    public enum PaymentMethod {
        ALIPAY("支付宝"),
        WECHAT("微信支付"),
        BANK_TRANSFER("银行转账"),
        CREDIT_CARD("信用卡"),
        CASH("现金");
        
        private final String description;
        
        PaymentMethod(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    public enum DonationStatus {
        PENDING("待确认"),
        CONFIRMED("已确认"),
        COMPLETED("已完成"),
        CANCELLED("已取消"),
        REFUNDED("已退款");
        
        private final String description;
        
        DonationStatus(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    // 在保存或更新前设置updatedAt
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
