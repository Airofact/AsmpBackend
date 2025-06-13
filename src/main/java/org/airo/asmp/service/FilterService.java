package org.airo.asmp.service;

import lombok.RequiredArgsConstructor;
import org.airo.asmp.dto.NoticeFilterDto;
import org.airo.asmp.dto.activity.ActivityApplicationFilterDto;
import org.airo.asmp.dto.activity.ActivityFilterDto;
import org.airo.asmp.dto.donation.DonationFilterDto;
import org.airo.asmp.dto.donation.DonationProjectFilterDto;
import org.airo.asmp.dto.entity.*;
import org.airo.asmp.dto.job.JobApplicationFilterDto;
import org.airo.asmp.dto.job.JobPostFilterDto;
import org.airo.asmp.model.activity.ActivityApplication;
import org.airo.asmp.model.donation.Donation;
import org.airo.asmp.model.donation.DonationProject;
import org.airo.asmp.model.job.JobApplication;
import org.airo.asmp.model.job.JobPost;
import org.airo.asmp.model.notice.Notice;
import org.airo.asmp.model.activity.Activity;
import org.airo.asmp.model.entity.*;
import org.airo.asmp.repository.*;
import org.airo.asmp.repository.entity.*;
import org.airo.asmp.util.SpecificationBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 过滤服务类
 * 统一处理各种实体的过滤查询逻辑
 */
@Service
@RequiredArgsConstructor
public class FilterService {
    
    // 注入当前已使用的Repository
    private final RawBusinessEntityRepository businessEntityRepository;
    private final AlumniRepository alumniRepository;
    private final EnterpriseRepository enterpriseRepository;
    private final OrganizationRepository organizationRepository;
    private final ActivityRepository activityRepository;
    private final ActivityApplicationRepository activityApplicationRepository;
    private final NoticeRepository noticeRepository;
    private final DonationRepository donationRepository;
    private final DonationProjectRepository donationProjectRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final JobPostRepository jobPostRepository;

    public List<BusinessEntity> filterBusinessEntity(BusinessEntityFilterDto filterDto) {
        return businessEntityRepository.findAll((root, query, builder) ->
            SpecificationBuilder.of(root, builder)
                .equal("id", filterDto.getId())
                .equal("addedBy.id", filterDto.getAddedById())
                .dateTimeAfterOrEqual("addedAt", filterDto.getAddedAtBegin())
                .dateTimeBefore("addedAt", filterDto.getAddedAtEnd())// 这里假设有一个 entityType 字段
                .build()
        );
    }

    /**
     * 校友过滤查询
     */
    public List<Alumni> filterAlumni(AlumniFilterDto filterDto) {

        return alumniRepository.findAll((root, query, builder) ->
            SpecificationBuilder.of(root, builder)
                .dateTimeAfterOrEqual("addedAt", filterDto.getAddedAtBegin())
                .dateTimeBefore("addedAt", filterDto.getAddedAtEnd())
                .equal("studentId", filterDto.getStudentId())
                .equal("realName", filterDto.getRealName())
                .equal("gender", filterDto.getGender())
                .dateAfterOrEqual("dateOfBirth", filterDto.getDateOfBirthBegin())
                .dateBefore("dateOfBirth", filterDto.getDateOfBirthEnd())
                .equal("address", filterDto.getAddress())
                .equal("companyName", filterDto.getCompanyName())
                .equal("currentJob", filterDto.getCurrentJob())
                .build()
        );
    }    /**
     * 企业过滤查询
     */
    public List<Enterprise> filterEnterprise(EnterpriseFilterDto filterDto) {
        
        return enterpriseRepository.findAll((root, query, builder) ->
            SpecificationBuilder.of(root, builder)
                .like("name", filterDto.getName())
                .equal("field", filterDto.getField())
                .like("address", filterDto.getAddress())
                .like("contactPerson", filterDto.getContactPerson())
                .equal("contactEmail", filterDto.getContactEmail())
                .equal("contactPhone", filterDto.getContactPhone())
                .dateTimeAfterOrEqual("addedAt", filterDto.getAddedAtBegin())
                .dateTimeBefore("addedAt", filterDto.getAddedAtEnd())
                .build()
        );
    }    /**
     * 组织过滤查询
     */
    public List<Organization> filterOrganization(OrganizationFilterDto filterDto) {
        var creators = filterAlumni(filterDto.getCreatorFilter());
        return organizationRepository.findAll((root, query, builder) ->
            SpecificationBuilder.of(root, builder)
                    .dateTimeAfterOrEqual("addedAt", filterDto.getAddedAtBegin())
                    .dateTimeBefore("addedAt", filterDto.getAddedAtEnd())
                    .like("name", filterDto.getName())
                    .equal("type", filterDto.getType())
                    .like("description", filterDto.getDescription())
                    .in("creator", creators)
                    .build()
        );
    }

    /**
     * 活动过滤查询
     */
    public List<Activity> filterActivity(ActivityFilterDto filterDto) {
        var organizers = filterAlumni(filterDto.organizerFilter());
        List<Activity> filteredActivities = activityRepository.findAll((root, query, builder) ->
            SpecificationBuilder.of(root, builder)
                    .like("title", filterDto.title())
                    .like("description", filterDto.description())
                    .dateTimeAfterOrEqual("startTime", filterDto.startTimeBegin())
                    .dateTimeBefore("startTime", filterDto.startTimeEnd())
                    .dateTimeAfterOrEqual("endTime", filterDto.endTimeBegin())
                    .dateTimeBefore("endTime", filterDto.endTimeEnd())
                    .like("location", filterDto.location())
                    .equal("maxParticipants", filterDto.maxParticipants())
                    .in("organizer", organizers)
                    .likeRelatedField("organizer", "name", filterDto.organizerName())
                    .build()
        );
        
        // 如果指定了状态过滤，在服务层进行过滤
        if (filterDto.status() != null) {
            return filteredActivities.stream()
                    .filter(activity -> activity.getStatus() == filterDto.status())
                    .toList();
        }
        
        return filteredActivities;
    }

    /**
     * 通知过滤查询
     */
    public List<Notice> filterNotice(NoticeFilterDto filterDto) {
        return noticeRepository.findAll((root, query, builder) ->
            SpecificationBuilder.of(root, builder)
                .like("title", filterDto.title())
                .like("content", filterDto.content())
                .equal("type", filterDto.type())
                .build()
        );
    }

    public List<ActivityApplication> filterActivityApplication(ActivityApplicationFilterDto activityApplicationFilterDto) {
        var activities = filterActivity(activityApplicationFilterDto.activityFilter());
        var alumni = filterAlumni(activityApplicationFilterDto.alumniFilter());
        return activityApplicationRepository.findAll((root, query, builder) ->
            SpecificationBuilder.of(root, builder)
                    .in("activity", activities)
                    .in("creatorId", alumni)
                    .dateTimeAfterOrEqual("applyTime", activityApplicationFilterDto.applyTimeBegin())
                    .dateTimeBefore("applyTime", activityApplicationFilterDto.applyTimeEnd())
                    .equal("signedIn", activityApplicationFilterDto.signedIn())
                    .build()
        );
    }

    public List<Donation> filterDonation(DonationFilterDto filter) {
        var donors = filterAlumni(filter.donorFilter());
        var donationProjects = filterDonationProject(filter.donationProjectFilter());
        return donationRepository.findAll((root, query, builder) ->
            SpecificationBuilder.of(root, builder)
                    .in("donor", donors)
                    .in("project", donationProjects)
                    .greaterThanOrEqualTo("amount", filter.minAmount())
                    .lessThanOrEqualTo("amount", filter.maxAmount())
                    .equal("paymentMethod", filter.paymentMethod())
                    .dateTimeAfterOrEqual("donateTime", filter.donateTimeFrom())
                    .dateTimeBefore("donateTime", filter.donateTimeTo())
                    .equal("status", filter.status())
                    .equal("anonymous", filter.anonymous())
                    .like("transactionId", filter.transactionId())
                    .dateTimeAfterOrEqual("createdAt", filter.createdAtFrom())
                    .dateTimeBefore("createdAt", filter.createdAtTo())
                    .build()
        );
    }

    public List<DonationProject> filterDonationProject(DonationProjectFilterDto filter) {
        var organizers = filterBusinessEntity(filter.organizerFilter());
        return donationProjectRepository.findAll((root, query, builder) ->
                SpecificationBuilder.of(root, builder)
                        .in("organizer", organizers)
                        .like("name", filter.name())
                        .like("description", filter.description())
                        .greaterThanOrEqualTo("targetAmount", filter.minTargetAmount())
                        .lessThanOrEqualTo("targetAmount", filter.maxTargetAmount())
                        .greaterThanOrEqualTo("currentAmount", filter.minCurrentAmount())
                        .lessThanOrEqualTo("currentAmount", filter.maxCurrentAmount())
                        .dateTimeAfterOrEqual("startDate", filter.startDateFrom())
                        .dateTimeBefore("startDate", filter.startDateTo())
                        .dateTimeAfterOrEqual("endDate", filter.endDateFrom())
                        .dateTimeBefore("endDate", filter.endDateTo())
                        .equal("status", filter.status())
                        .like("category", filter.category())
                        .equal("targetReached", filter.targetReached())
                        .build()
        );
    }

    public List<JobApplication> filterJobApplication(JobApplicationFilterDto filterDto) {
        return jobApplicationRepository.findAll((root, query, builder) ->
            SpecificationBuilder.of(root, builder)
                .equal("id", filterDto.id())
                .equal("jobPost.id", filterDto.jobPostId())
                .equal("creatorId.id", filterDto.alumniId())
                .equal("status", filterDto.status())
                .dateTimeAfterOrEqual("applyTime", filterDto.applyTime())
                .build()
        );
    }

    public List<JobPost> filterJobPost(JobPostFilterDto filter) {
        var publishers = filterEnterprise(filter.enterpriseFilter());
        return jobPostRepository.findAll((root, query, builder) ->
            SpecificationBuilder.of(root, builder)
                    .like("title", filter.title())
                    .like("description", filter.description())
                    .equal("jobType", filter.jobType())
                    .greaterThanOrEqualTo("salaryMax", filter.salaryMaxLowerBound())
                    .lessThanOrEqualTo("salaryMax", filter.salaryMaxUpperBound())
                    .greaterThanOrEqualTo("salaryMin", filter.salaryMinLowerBound())
                    .lessThanOrEqualTo("salaryMin", filter.salaryMinUpperBound())
                    .dateTimeAfterOrEqual("publishTime", filter.publishTimeStart())
                    .dateTimeBefore("publishTime", filter.publishTimeEnd())
                    .in("publisher", publishers)
                    .build()
        );
    }
}
