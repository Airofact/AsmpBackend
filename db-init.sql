CREATE DATABASE IF NOT EXISTS alumni_db;
USE alumni_db;

SET NAMES utf8mb4;
SET CHARACTER_SET_CLIENT = utf8mb4;
SET CHARACTER_SET_CONNECTION = utf8mb4;
SET CHARACTER_SET_RESULTS = utf8mb4;

CREATE TABLE activity
(
    id               BINARY(16)   NOT NULL,
    title            VARCHAR(100) NULL,
    `description`    TEXT         NULL,
    start_time       datetime     NULL,
    end_time         datetime     NULL,
    location         VARCHAR(200) NULL,
    max_participants INT          NULL,
    organizer_id     BINARY(16)   NULL,
    CONSTRAINT pk_activity PRIMARY KEY (id)
);

CREATE TABLE activity_application
(
    apply_time  datetime   NULL,
    signed_in   BIT(1)     NULL,
    activity_id BINARY(16) NOT NULL,
    alumni_id   BINARY(16) NOT NULL,
    CONSTRAINT pk_activityapplication PRIMARY KEY (activity_id, alumni_id)
);

CREATE TABLE `admin`
(
    id       BINARY(16)   NOT NULL,
    username VARCHAR(255) NULL,
    password VARCHAR(255) NULL,
    CONSTRAINT pk_admin PRIMARY KEY (id)
);

CREATE TABLE alumni
(
    id            BINARY(16)   NOT NULL,
    student_id    VARCHAR(255) NOT NULL,
    real_name     VARCHAR(255) NOT NULL,
    gender        SMALLINT     NOT NULL,
    date_of_birth date         NOT NULL,
    address       VARCHAR(255) NULL,
    company_name  VARCHAR(255) NULL,
    current_job   VARCHAR(255) NULL,
    CONSTRAINT pk_alumni PRIMARY KEY (id)
);

CREATE TABLE business_entity
(
    id       BINARY(16) NOT NULL,
    added_at datetime   NOT NULL,
    CONSTRAINT pk_businessentity PRIMARY KEY (id)
);

CREATE TABLE donation
(
    id             BINARY(16)     NOT NULL,
    donor_id       BINARY(16)     NOT NULL,
    project_id     BINARY(16)     NOT NULL,
    amount         DECIMAL(15, 2) NOT NULL,
    payment_method VARCHAR(50)    NOT NULL,
    donate_time    datetime       NOT NULL,
    remark         TEXT           NULL,
    status         VARCHAR(255)   NOT NULL,
    transaction_id VARCHAR(100)   NULL,
    anonymous      BIT(1)         NOT NULL,
    created_at     datetime       NOT NULL,
    updated_at     datetime       NULL,
    CONSTRAINT pk_donation PRIMARY KEY (id)
);

CREATE TABLE donation_project
(
    id             BINARY(16)     NOT NULL,
    name           VARCHAR(200)   NOT NULL,
    `description`  TEXT           NULL,
    target_amount  DECIMAL(15, 2) NOT NULL,
    current_amount DECIMAL(15, 2) NOT NULL,
    start_date     datetime       NOT NULL,
    end_date       datetime       NULL,
    status         SMALLINT       NOT NULL,
    category       VARCHAR(100)   NULL,
    image_url      VARCHAR(500)   NULL,
    organizer_id   BINARY(16)     NULL,
    created_at     datetime       NOT NULL,
    updated_at     datetime       NULL,
    CONSTRAINT pk_donationproject PRIMARY KEY (id)
);

CREATE TABLE enterprise
(
    id             BINARY(16)   NOT NULL,
    name           VARCHAR(255) NULL,
    field          VARCHAR(255) NULL,
    address        VARCHAR(255) NULL,
    contact_person VARCHAR(255) NULL,
    contact_email  VARCHAR(255) NULL,
    contact_phone  VARCHAR(255) NULL,
    CONSTRAINT pk_enterprise PRIMARY KEY (id)
);

CREATE TABLE job_application
(
    id          BINARY(16) NOT NULL,
    job_post_id BINARY(16) NULL,
    alumni_id   BINARY(16) NULL,
    apply_time  datetime   NULL,
    status      SMALLINT   NOT NULL,
    CONSTRAINT pk_jobapplication PRIMARY KEY (id)
);

CREATE TABLE job_post
(
    id            BINARY(16)   NOT NULL,
    title         VARCHAR(100) NOT NULL,
    job_type      VARCHAR(100) NOT NULL,
    `description` TEXT         NULL,
    salary_min    INT          NULL,
    salary_max    INT          NULL,
    publish_time  datetime     NULL,
    enterprise_id BINARY(16)   NULL,
    CONSTRAINT pk_jobpost PRIMARY KEY (id)
);

CREATE TABLE notice
(
    id      BINARY(16)   NOT NULL,
    title   VARCHAR(100) NULL,
    content TEXT         NULL,
    type    SMALLINT     NOT NULL,
    CONSTRAINT pk_notice PRIMARY KEY (id)
);

CREATE TABLE `organization`
(
    id            BINARY(16)   NOT NULL,
    name          VARCHAR(100) NULL,
    type          SMALLINT     NOT NULL,
    `description` TEXT         NULL,
    creator_id    BINARY(16)   NOT NULL,
    state         SMALLINT     NOT NULL,
    CONSTRAINT pk_organization PRIMARY KEY (id)
);

CREATE TABLE organization_member
(
    join_time       datetime   NULL,
    `role`          SMALLINT   NULL,
    organization_id BINARY(16) NOT NULL,
    alumni_id       BINARY(16) NOT NULL,
    CONSTRAINT pk_organizationmember PRIMARY KEY (organization_id, alumni_id)
);

ALTER TABLE alumni
    ADD CONSTRAINT uc_alumni_studentid UNIQUE (student_id);

ALTER TABLE activity_application
    ADD CONSTRAINT FK_ACTIVITYAPPLICATION_ON_ACTIVITY FOREIGN KEY (activity_id) REFERENCES activity (id);

ALTER TABLE activity_application
    ADD CONSTRAINT FK_ACTIVITYAPPLICATION_ON_ALUMNI FOREIGN KEY (alumni_id) REFERENCES alumni (id);

ALTER TABLE activity
    ADD CONSTRAINT FK_ACTIVITY_ON_ORGANIZER FOREIGN KEY (organizer_id) REFERENCES `organization` (id);

ALTER TABLE alumni
    ADD CONSTRAINT FK_ALUMNI_ON_ID FOREIGN KEY (id) REFERENCES business_entity (id);

ALTER TABLE donation_project
    ADD CONSTRAINT FK_DONATIONPROJECT_ON_ORGANIZER FOREIGN KEY (organizer_id) REFERENCES business_entity (id);

ALTER TABLE donation
    ADD CONSTRAINT FK_DONATION_ON_DONOR FOREIGN KEY (donor_id) REFERENCES business_entity (id);

ALTER TABLE donation
    ADD CONSTRAINT FK_DONATION_ON_PROJECT FOREIGN KEY (project_id) REFERENCES donation_project (id);

ALTER TABLE enterprise
    ADD CONSTRAINT FK_ENTERPRISE_ON_ID FOREIGN KEY (id) REFERENCES business_entity (id);

ALTER TABLE job_application
    ADD CONSTRAINT FK_JOBAPPLICATION_ON_ALUMNI FOREIGN KEY (alumni_id) REFERENCES alumni (id);

ALTER TABLE job_application
    ADD CONSTRAINT FK_JOBAPPLICATION_ON_JOB_POST FOREIGN KEY (job_post_id) REFERENCES job_post (id);

ALTER TABLE job_post
    ADD CONSTRAINT FK_JOBPOST_ON_ENTERPRISEID FOREIGN KEY (enterprise_id) REFERENCES enterprise (id);

ALTER TABLE organization_member
    ADD CONSTRAINT FK_ORGANIZATIONMEMBER_ON_ALUMNI FOREIGN KEY (alumni_id) REFERENCES alumni (id);

ALTER TABLE organization_member
    ADD CONSTRAINT FK_ORGANIZATIONMEMBER_ON_ORGANIZATION FOREIGN KEY (organization_id) REFERENCES `organization` (id);

ALTER TABLE `organization`
    ADD CONSTRAINT FK_ORGANIZATION_ON_CREATOR FOREIGN KEY (creator_id) REFERENCES alumni (id);

ALTER TABLE `organization`
    ADD CONSTRAINT FK_ORGANIZATION_ON_ID FOREIGN KEY (id) REFERENCES business_entity (id);

-- 校友服务管理平台测试数据初始化脚本
-- 生成日期: 2025-06-13

-- 清理现有数据（按依赖关系倒序删除）
SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM donation;
DELETE FROM job_application;
DELETE FROM activity_application;
DELETE FROM organization_member;
DELETE FROM donation_project;
DELETE FROM job_post;
DELETE FROM activity;
DELETE FROM notice;
DELETE FROM organization;
DELETE FROM alumni;
DELETE FROM enterprise;
DELETE FROM business_entity;
DELETE FROM admin;

SET FOREIGN_KEY_CHECKS = 1;

-- 设置变量来存储生成的ID，避免重复的UUID
SET @admin1_id = UUID_TO_BIN(UUID());
SET @admin2_id = UUID_TO_BIN(UUID());
SET @admin3_id = UUID_TO_BIN(UUID());

SET @alumni1_id = UUID_TO_BIN(UUID());
SET @alumni2_id = UUID_TO_BIN(UUID());
SET @alumni3_id = UUID_TO_BIN(UUID());
SET @alumni4_id = UUID_TO_BIN(UUID());
SET @alumni5_id = UUID_TO_BIN(UUID());

SET @enterprise1_id = UUID_TO_BIN(UUID());
SET @enterprise2_id = UUID_TO_BIN(UUID());
SET @enterprise3_id = UUID_TO_BIN(UUID());

SET @org1_id = UUID_TO_BIN(UUID());
SET @org2_id = UUID_TO_BIN(UUID());
SET @org3_id = UUID_TO_BIN(UUID());

-- ============================================================================
-- 1. 管理员数据 (admin)
-- ============================================================================
INSERT INTO admin (id, username, password) VALUES 
(@admin1_id, 'admin1', '$2a$10$N.zmdr9k7uOLQvQHbPWMUOmMraNZ4O5p6fECpxMGQFBr0J.NQQzCu'),
(@admin2_id, 'admin2', '$2a$10$N.zmdr9k7uOLQvQHbPWMUOmMraNZ4O5p6fECpxMGQFBr0J.NQQzCu'),
(@admin3_id, 'superadmin', '$2a$10$N.zmdr9k7uOLQvQHbPWMUOmMraNZ4O5p6fECpxMGQFBr0J.NQQzCu');

-- ============================================================================
-- 2. 业务实体数据 (business_entity)
-- ============================================================================
INSERT INTO business_entity (id, added_at) VALUES 
(@alumni1_id, '2023-01-15 09:00:00'),
(@alumni2_id, '2023-01-16 10:30:00'),
(@alumni3_id, '2023-01-17 14:20:00'),
(@alumni4_id, '2023-01-18 16:45:00'),
(@alumni5_id, '2023-01-19 11:15:00'),
(@enterprise1_id, '2023-02-01 09:00:00'),
(@enterprise2_id, '2023-02-02 10:00:00'),
(@enterprise3_id, '2023-02-03 11:00:00'),
(@org1_id, '2023-03-01 09:00:00'),
(@org2_id, '2023-03-02 10:00:00'),
(@org3_id, '2023-03-03 11:00:00');

-- ============================================================================
-- 3. 校友数据 (alumni)
-- ============================================================================
INSERT INTO alumni (id, student_id, real_name, gender, date_of_birth, address, company_name, current_job) VALUES 
(@alumni1_id, '2018001001', '张三', 1, '1995-03-15', '北京市海淀区中关村大街1号', '腾讯科技', '高级软件工程师'),
(@alumni2_id, '2018001002', '李四', 1, '1994-07-22', '上海市浦东新区陆家嘴环路1000号', '阿里巴巴集团', '产品经理'),
(@alumni3_id, '2018001003', '王五', 0, '1995-11-08', '深圳市南山区科技园南区', '华为技术有限公司', '研发工程师'),
(@alumni4_id, '2017001001', '赵六', 0, '1994-02-14', '杭州市西湖区文三路269号', '网易科技', 'UI设计师'),
(@alumni5_id, '2017001002', '孙七', 1, '1993-09-30', '广州市天河区珠江新城', '百度在线', '算法工程师');

-- ============================================================================
-- 4. 企业数据 (enterprise)
-- ============================================================================
INSERT INTO enterprise (id, name, field, address, contact_person, contact_email, contact_phone) VALUES
(@enterprise1_id, '阿里巴巴集团', '互联网/电子商务', '杭州市余杭区文一西路969号', '张经理', 'hr@alibaba.com', '0571-85022088'),
(@enterprise2_id, '腾讯科技', '互联网/游戏', '深圳市南山区科技中一路腾讯大厦', '李经理', 'recruitment@tencent.com', '0755-86013388'),
(@enterprise3_id, '华为技术有限公司', '通信设备/软件', '深圳市龙岗区坂田华为基地', '王经理', 'career@huawei.com', '0755-28780808');

-- ============================================================================
-- 5. 组织数据 (organization)
INSERT INTO organization (id, name, type, description, creator_id, state) VALUES 
(@org1_id, '计算机学院校友会', 0, '计算机学院毕业生联谊组织，定期举办技术交流和聚会活动', @alumni1_id, 1),
(@org2_id, '创业校友俱乐部', 1, '面向有创业意向或已创业的校友，提供资源对接和经验分享', @alumni2_id, 1),
(@org3_id, '深圳地区校友分会', 2, '深圳地区校友的地方性组织，便于当地校友交流', @alumni3_id, 1);

-- ============================================================================
-- 6. 组织成员数据 (organization_member)
-- ============================================================================
INSERT INTO organization_member (organization_id, alumni_id, join_time, role) VALUES 
(@org1_id, @alumni1_id, '2023-03-01 09:00:00', 1),
(@org1_id, @alumni2_id, '2023-03-05 14:30:00', 0),
(@org1_id, @alumni5_id, '2023-03-10 16:20:00', 0),
(@org2_id, @alumni2_id, '2023-03-02 10:00:00', 1),
(@org2_id, @alumni3_id, '2023-03-08 11:45:00', 0),
(@org3_id, @alumni3_id, '2023-03-03 11:00:00', 1),
(@org3_id, @alumni1_id, '2023-03-12 15:30:00', 0),
(@org3_id, @alumni4_id, '2023-03-15 09:15:00', 0);

-- ============================================================================
-- 7. 活动数据 (activity)
-- ============================================================================
INSERT INTO activity (id, title, description, start_time, end_time, location, max_participants, organizer_id) VALUES 
(UUID_TO_BIN(UUID()), '2024年春季校友聚会', '一年一度的春季校友聚会，包含晚宴、抽奖和才艺表演等环节', '2024-04-15 18:00:00', '2024-04-15 22:00:00', '北京市朝阳区国际会议中心', 200, @org1_id),
(UUID_TO_BIN(UUID()), '创业经验分享会', '邀请成功创业的校友分享创业心得和经验', '2024-05-20 14:00:00', '2024-05-20 17:00:00', '深圳市南山区创新科技园', 50, @org2_id),
(UUID_TO_BIN(UUID()), '技术沙龙：AI前沿技术', '探讨人工智能最新技术发展趋势和应用场景', '2024-06-10 09:00:00', '2024-06-10 12:00:00', '上海市浦东新区张江高科技园区', 80, @org1_id),
(UUID_TO_BIN(UUID()), '校友企业招聘会', '校友企业专场招聘会，为在校学生和求职校友提供就业机会', '2024-07-05 09:00:00', '2024-07-05 17:00:00', '学校体育馆', 500, @org3_id);

-- ============================================================================
-- 8. 活动报名数据 (activity_application) - 简化为不需要引用activity_id
-- ============================================================================
-- 首先获取活动ID（因为活动是动态生成的）
SET @activity1_id = (SELECT id FROM activity WHERE title = '2024年春季校友聚会' LIMIT 1);
SET @activity2_id = (SELECT id FROM activity WHERE title = '创业经验分享会' LIMIT 1);
SET @activity3_id = (SELECT id FROM activity WHERE title = '技术沙龙：AI前沿技术' LIMIT 1);
SET @activity4_id = (SELECT id FROM activity WHERE title = '校友企业招聘会' LIMIT 1);

INSERT INTO activity_application (activity_id, alumni_id, apply_time, signed_in) VALUES 
(@activity1_id, @alumni1_id, '2024-04-01 10:30:00', 1),
(@activity1_id, @alumni2_id, '2024-04-02 14:15:00', 1),
(@activity1_id, @alumni3_id, '2024-04-03 09:45:00', 0),
(@activity2_id, @alumni2_id, '2024-05-05 11:20:00', 1),
(@activity2_id, @alumni3_id, '2024-05-06 16:30:00', 1),
(@activity3_id, @alumni1_id, '2024-05-25 13:10:00', 1),
(@activity3_id, @alumni5_id, '2024-05-26 15:45:00', 0),
(@activity4_id, @alumni4_id, '2024-06-20 10:00:00', 1),
(@activity4_id, @alumni5_id, '2024-06-21 12:30:00', 1);

-- ============================================================================
-- 9. 招聘信息数据 (job_post)
-- ============================================================================
INSERT INTO job_post (id, title, job_type, description, salary_min, salary_max, publish_time, enterprise_id) VALUES 
(UUID_TO_BIN(UUID()), '高级Java开发工程师', 'FULL_TIME', '负责核心业务系统开发，要求3年以上Java开发经验，熟悉Spring框架', 15000, 25000, '2024-05-01 09:00:00', @enterprise1_id),
(UUID_TO_BIN(UUID()), '产品经理', 'FULL_TIME', '负责移动端产品规划和设计，要求2年以上产品经验', 18000, 30000, '2024-05-05 10:30:00', @enterprise2_id),
(UUID_TO_BIN(UUID()), '算法工程师实习生', 'INTERNSHIP', '参与机器学习算法研发，在校研究生优先', 3000, 5000, '2024-05-10 14:20:00', @enterprise3_id),
(UUID_TO_BIN(UUID()), '前端开发工程师', 'PART_TIME', 'React/Vue项目开发，可远程办公', 8000, 15000, '2024-05-15 16:00:00', @enterprise1_id);


-- ============================================================================
-- 9.1. 求职申请数据 (job_application)
-- ============================================================================
-- 首先获取职位ID（因为职位是动态生成的）
SET @job1_id = (SELECT id FROM job_post WHERE title = '高级Java开发工程师' LIMIT 1);
SET @job2_id = (SELECT id FROM job_post WHERE title = '产品经理' LIMIT 1);
SET @job3_id = (SELECT id FROM job_post WHERE title = '算法工程师实习生' LIMIT 1);
SET @job4_id = (SELECT id FROM job_post WHERE title = '前端开发工程师' LIMIT 1);

INSERT INTO job_application (id, job_post_id, alumni_id, apply_time, status) VALUES 
(UUID_TO_BIN(UUID()), @job1_id, @alumni1_id, '2024-05-02 10:15:00', 0), -- 待处理
(UUID_TO_BIN(UUID()), @job1_id, @alumni5_id, '2024-05-03 14:30:00', 1), -- 已通过
(UUID_TO_BIN(UUID()), @job2_id, @alumni2_id, '2024-05-06 09:45:00', 1), -- 已通过
(UUID_TO_BIN(UUID()), @job2_id, @alumni4_id, '2024-05-07 16:20:00', 2), -- 已拒绝
(UUID_TO_BIN(UUID()), @job3_id, @alumni3_id, '2024-05-11 11:30:00', 0), -- 待处理
(UUID_TO_BIN(UUID()), @job3_id, @alumni4_id, '2024-05-12 13:45:00', 0), -- 待处理
(UUID_TO_BIN(UUID()), @job4_id, @alumni1_id, '2024-05-16 15:10:00', 1), -- 已通过
(UUID_TO_BIN(UUID()), @job4_id, @alumni3_id, '2024-05-17 12:25:00', 0); -- 待处理
-- ============================================================================
-- 10. 捐赠项目数据 (donation_project)
-- ============================================================================
INSERT INTO donation_project (id, name, description, target_amount, current_amount, start_date, end_date, status, category, image_url, organizer_id, created_at, updated_at) VALUES 
(UUID_TO_BIN(UUID()), '贫困学生助学金', '为家庭经济困难的在校学生提供助学金支持', 100000.00, 45000.00, '2024-03-01 00:00:00', '2024-12-31 23:59:59', 0, '教育助学', 'https://example.com/images/scholarship.jpg', @org1_id, '2024-02-25 09:00:00', '2024-06-01 15:30:00'),
(UUID_TO_BIN(UUID()), '图书馆建设基金', '支持学校图书馆数字化改造和图书采购', 200000.00, 80000.00, '2024-01-01 00:00:00', '2024-10-31 23:59:59', 2, '基础设施', 'https://example.com/images/library.jpg', @org2_id, '2023-12-20 10:00:00', '2024-05-15 12:20:00'),
(UUID_TO_BIN(UUID()), '科研创新奖励基金', '奖励在科研创新方面有突出表现的师生', 50000.00, 50000.00, '2024-02-15 00:00:00', '2024-08-15 23:59:59', 3, '科研支持', 'https://example.com/images/research.jpg', @org3_id, '2024-02-10 14:00:00', '2024-08-15 18:45:00');

-- ============================================================================
-- 10.1. 捐赠数据 (donation)
-- ============================================================================
-- 首先获取捐赠项目ID（因为项目是动态生成的）
SET @project1_id = (SELECT id FROM donation_project WHERE name = '贫困学生助学金' LIMIT 1);
SET @project2_id = (SELECT id FROM donation_project WHERE name = '图书馆建设基金' LIMIT 1);
SET @project3_id = (SELECT id FROM donation_project WHERE name = '科研创新奖励基金' LIMIT 1);

INSERT INTO donation (id, donor_id, project_id, amount, payment_method, donate_time, remark, status, transaction_id, anonymous, created_at, updated_at) VALUES 
(UUID_TO_BIN(UUID()), @alumni1_id, @project1_id, 5000.00, 'ALIPAY', '2024-03-15 10:30:00', '希望能帮助到有需要的同学', 'COMPLETED', 'TXN2024031510301001', 0, '2024-03-15 10:30:00', '2024-03-15 10:35:00'),
(UUID_TO_BIN(UUID()), @alumni2_id, @project1_id, 10000.00, 'WECHAT', '2024-04-02 14:20:00', '支持母校教育事业发展', 'COMPLETED', 'TXN2024040214201002', 0, '2024-04-02 14:20:00', '2024-04-02 14:25:00'),
(UUID_TO_BIN(UUID()), @alumni3_id, @project1_id, 2000.00, 'BANK_TRANSFER', '2024-04-20 09:45:00', '匿名捐赠', 'COMPLETED', 'TXN2024042009451003', 1, '2024-04-20 09:45:00', '2024-04-20 10:00:00'),
(UUID_TO_BIN(UUID()), @alumni4_id, @project2_id, 15000.00, 'ALIPAY', '2024-03-25 16:15:00', '支持图书馆数字化建设', 'COMPLETED', 'TXN2024032516151004', 0, '2024-03-25 16:15:00', '2024-03-25 16:20:00'),
(UUID_TO_BIN(UUID()), @alumni5_id, @project2_id, 8000.00, 'WECHAT', '2024-05-10 11:30:00', '希望同学们有更好的学习环境', 'COMPLETED', 'TXN2024051011301005', 0, '2024-05-10 11:30:00', '2024-05-10 11:35:00'),
(UUID_TO_BIN(UUID()), @enterprise1_id, @project2_id, 25000.00, 'BANK_TRANSFER', '2024-04-15 13:45:00', '阿里巴巴集团企业捐赠', 'COMPLETED', 'TXN2024041513451006', 0, '2024-04-15 13:45:00', '2024-04-15 14:00:00'),
(UUID_TO_BIN(UUID()), @alumni1_id, @project3_id, 3000.00, 'ALIPAY', '2024-05-20 15:20:00', '鼓励科研创新', 'COMPLETED', 'TXN2024052015201007', 0, '2024-05-20 15:20:00', '2024-05-20 15:25:00'),
(UUID_TO_BIN(UUID()), @alumni3_id, @project3_id, 8000.00, 'WECHAT', '2024-06-01 10:10:00', '支持学术研究发展', 'COMPLETED', 'TXN2024060110101008', 0, '2024-06-01 10:10:00', '2024-06-01 10:15:00'),
(UUID_TO_BIN(UUID()), @enterprise2_id, @project3_id, 20000.00, 'BANK_TRANSFER', '2024-05-25 14:30:00', '腾讯科技企业支持科研', 'COMPLETED', 'TXN2024052514301009', 0, '2024-05-25 14:30:00', '2024-05-25 14:45:00'),
(UUID_TO_BIN(UUID()), @alumni2_id, @project1_id, 5000.00, 'ALIPAY', '2024-06-05 16:40:00', '继续支持助学事业', 'PENDING', 'TXN2024060516401010', 0, '2024-06-05 16:40:00', NULL);
-- ============================================================================
-- 11. 通知公告数据 (notice)
-- ============================================================================
INSERT INTO notice (id, title, content, type) VALUES 
(UUID_TO_BIN(UUID()), '2024年校友会换届选举通知', '根据校友会章程规定，现启动2024年度校友会换届选举工作。请符合条件的校友积极参与。选举时间：2024年7月15日，地点：学校大礼堂。', 0),
(UUID_TO_BIN(UUID()), '春季校友聚会活动通知', '2024年春季校友聚会将于4月15日举行，地点在北京国际会议中心。活动包含晚宴、抽奖等环节，欢迎广大校友报名参加。', 1),
(UUID_TO_BIN(UUID()), '校友信息更新提醒', '为了更好地为校友提供服务，请各位校友及时更新个人信息，包括联系方式、工作单位等。可通过校友平台在线更新。', 2),
(UUID_TO_BIN(UUID()), '新版校友平台上线公告', '经过半年的开发和测试，新版校友服务管理平台正式上线。新平台增加了职位推荐、在线捐赠等功能，欢迎体验使用。', 2);

-- ============================================================================
-- 数据插入完成提示
-- ============================================================================
SELECT '测试数据插入完成！注意：部分需要引用动态生成ID的表（如activity_application、job_application、donation）需要手动处理。' as message;

-- 验证数据插入结果
SELECT
    'admin' as table_name, COUNT(*) as record_count FROM admin
UNION ALL
SELECT 'business_entity', COUNT(*) FROM business_entity
UNION ALL
SELECT 'alumni', COUNT(*) FROM alumni
UNION ALL
SELECT 'enterprise', COUNT(*) FROM enterprise
UNION ALL
SELECT 'organization', COUNT(*) FROM organization
UNION ALL
SELECT 'organization_member', COUNT(*) FROM organization_member
UNION ALL
SELECT 'activity', COUNT(*) FROM activity
UNION ALL
SELECT 'job_post', COUNT(*) FROM job_post
UNION ALL
SELECT 'donation_project', COUNT(*) FROM donation_project
UNION ALL
SELECT 'notice', COUNT(*) FROM notice;