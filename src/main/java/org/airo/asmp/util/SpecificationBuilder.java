package org.airo.asmp.util;

import jakarta.persistence.criteria.*;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

/**
 * 通用的JPA Specification构建器
 * 支持各种常见的查询条件，避免重复代码
 */
public class SpecificationBuilder<T> {
    private final List<Predicate> predicates = new ArrayList<>();
    private final Root<T> root;
    private final CriteriaBuilder builder;

    public SpecificationBuilder(Root<T> root, CriteriaBuilder builder) {
        this.root = root;
        this.builder = builder;
    }

    /**
     * 创建一个新的SpecificationBuilder实例
     */
    public static <T> SpecificationBuilder<T> of(Root<T> root, CriteriaBuilder builder) {
        return new SpecificationBuilder<>(root, builder);
    }

    public SpecificationBuilder<T> in(String fieldName, List<?> values) {
        if (values != null && !values.isEmpty()) {
            predicates.add(root.get(fieldName).in(values));
        }
        return this;
    }

    /**
     * 添加相等条件（精确匹配）
     */
    public SpecificationBuilder<T> equal(String fieldName, Object value) {
        if (value != null) {
            predicates.add(builder.equal(getPath(fieldName), value));
        }
        return this;
    }

    /**
     * 添加模糊查询条件（忽略大小写）
     */
    public SpecificationBuilder<T> like(String fieldName, String value) {
        if (StringUtils.hasText(value)) {
            predicates.add(builder.like(
                    builder.lower(root.get(fieldName)),
                    "%" + value.toLowerCase() + "%"
            ));
        }
        return this;
    }

    /**
     * 添加大于等于条件
     */
    public <X extends Comparable<X>> SpecificationBuilder<T> greaterThanOrEqualTo(String fieldName, X value) {
        if (value != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get(fieldName), value));
        }
        return this;
    }

    /**
     * 添加小于条件
     */
    public <X extends Comparable<X>> SpecificationBuilder<T> lessThan(String fieldName, X value) {
        if (value != null) {
            predicates.add(builder.lessThan(root.get(fieldName), value));
        }
        return this;
    }

    /**
     * 添加小于等于条件
     */
    public <X extends Comparable<X>> SpecificationBuilder<T> lessThanOrEqualTo(String fieldName, X value) {
        if (value != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get(fieldName), value));
        }
        return this;
    }

    /**
     * 添加日期范围查询（开始时间）
     */
    public SpecificationBuilder<T> dateAfterOrEqual(String fieldName, LocalDate startDate) {
        return greaterThanOrEqualTo(fieldName, startDate);
    }

    /**
     * 添加日期范围查询（结束时间）
     */
    public SpecificationBuilder<T> dateBefore(String fieldName, LocalDate endDate) {
        return lessThan(fieldName, endDate);
    }

    /**
     * 添加日期时间范围查询（开始时间）
     */
    public SpecificationBuilder<T> dateTimeAfterOrEqual(String fieldName, LocalDateTime startDateTime) {
        return greaterThanOrEqualTo(fieldName, startDateTime);
    }

    /**
     * 添加日期时间范围查询（结束时间）
     */
    public SpecificationBuilder<T> dateTimeBefore(String fieldName, LocalDateTime endDateTime) {
        return lessThan(fieldName, endDateTime);
    }

    /**
     * 添加关联实体的ID查询
     */
    public SpecificationBuilder<T> equalRelatedId(String relationFieldName, String idFieldName, UUID id) {
        if (id != null) {
            predicates.add(builder.equal(
                    root.get(relationFieldName).get(idFieldName),
                    id
            ));
        }
        return this;
    }

    /**
     * 添加关联实体字段的模糊查询
     */
    public SpecificationBuilder<T> likeRelatedField(String relationFieldName, String fieldName, String value) {
        if (StringUtils.hasText(value)) {
            predicates.add(builder.like(
                    builder.lower(root.get(relationFieldName).get(fieldName)),
                    "%" + value.toLowerCase() + "%"
            ));
        }
        return this;
    }

    /**
     * 添加自定义条件
     */
    public SpecificationBuilder<T> custom(Function<CriteriaBuilder, Predicate> customPredicate) {
        Predicate predicate = customPredicate.apply(builder);
        if (predicate != null) {
            predicates.add(predicate);
        }
        return this;
    }

    /**
     * 构建最终的Predicate
     */
    public Predicate build() {
        return builder.and(predicates.toArray(new Predicate[0]));
    }

    /**
     * 获取字段路径，支持嵌套字段（如 "addedBy.id"）
     */
    private Path<?> getPath(String fieldName) {
        if (fieldName.contains(".")) {
            String[] parts = fieldName.split("\\.");
            Path<?> path = root.get(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                path = path.get(parts[i]);
            }
            return path;
        }
        return root.get(fieldName);
    }
}
