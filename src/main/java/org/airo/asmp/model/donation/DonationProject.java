package org.airo.asmp.model.donation;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.airo.asmp.model.entity.BusinessEntity;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class DonationProject {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(nullable = false, columnDefinition = "varchar(200)")
    @NotNull
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column(nullable = false, precision = 15, scale = 2)
    @NotNull
    private BigDecimal targetAmount;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal currentAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    @NotNull
    private LocalDateTime startDate;

    @Column
    private LocalDateTime endDate;

    @Column(nullable = false)
    private ProjectStatus status = ProjectStatus.ACTIVE;

    @Column(columnDefinition = "varchar(100)")
    private String category;

    @Column(columnDefinition = "varchar(500)")
    private String imageUrl;

    // 项目发起者 - 可以是组织或其他业务实体
    @ManyToOne
    @JoinColumn(name = "organizer_id")
    private BusinessEntity organizer;

    // 创建时间
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // 更新时间
    @Column
    private LocalDateTime updatedAt;

    public enum ProjectStatus {
        ACTIVE,    // 活跃中
        COMPLETED, // 已完成
        SUSPENDED, // 已暂停
        CLOSED     // 已关闭
    }

    // 计算捐赠进度百分比
    @Transient
    public Double getProgress() {
        if (targetAmount.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        return currentAmount.divide(targetAmount, 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"))
                .doubleValue();
    }

    // 判断项目是否已达到目标金额
    @Transient
    public Boolean isTargetReached() {
        return currentAmount.compareTo(targetAmount) >= 0;
    }

    // 在保存或更新前设置updatedAt
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
