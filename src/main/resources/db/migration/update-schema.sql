CREATE TABLE activity
(
    id               BINARY(16)   NOT NULL,
    title            VARCHAR(100) NULL,
    `description`    TEXT NULL,
    start_time       datetime NULL,
    end_time         datetime NULL,
    location         VARCHAR(200) NULL,
    max_participants INT NULL,
    organizer_id     BINARY(16)   NULL,
    CONSTRAINT pk_activity PRIMARY KEY (id)
);

CREATE TABLE activity_application
(
    apply_time  datetime NULL,
    signed_in   BIT(1) NULL,
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
    added_at datetime NOT NULL,
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
    remark         TEXT NULL,
    status         VARCHAR(255)   NOT NULL,
    transaction_id VARCHAR(100) NULL,
    anonymous      BIT(1)         NOT NULL,
    created_at     datetime       NOT NULL,
    updated_at     datetime NULL,
    CONSTRAINT pk_donation PRIMARY KEY (id)
);

CREATE TABLE donation_project
(
    id             BINARY(16)     NOT NULL,
    name           VARCHAR(200)   NOT NULL,
    `description`  TEXT NULL,
    target_amount  DECIMAL(15, 2) NOT NULL,
    current_amount DECIMAL(15, 2) NOT NULL,
    start_date     datetime       NOT NULL,
    end_date       datetime NULL,
    status         VARCHAR(255)   NOT NULL,
    category       VARCHAR(100) NULL,
    image_url      VARCHAR(500) NULL,
    organizer_id   BINARY(16)     NULL,
    created_at     datetime       NOT NULL,
    updated_at     datetime NULL,
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
    id          BINARY(16)   NOT NULL,
    job_post_id BINARY(16)   NULL,
    alumni_id   BINARY(16)   NULL,
    apply_time  datetime NULL,
    status      VARCHAR(255) NOT NULL,
    CONSTRAINT pk_jobapplication PRIMARY KEY (id)
);

CREATE TABLE job_post
(
    id            BINARY(16)   NOT NULL,
    title         VARCHAR(100) NOT NULL,
    job_type      VARCHAR(100) NOT NULL,
    `description` TEXT NULL,
    salary_min    INT NULL,
    salary_max    INT NULL,
    publish_time  datetime NULL,
    enterprise_id BINARY(16)   NULL,
    CONSTRAINT pk_jobpost PRIMARY KEY (id)
);

CREATE TABLE notice
(
    id      BINARY(16)   NOT NULL,
    title   VARCHAR(100) NULL,
    content TEXT NULL,
    type    VARCHAR(255) NOT NULL,
    CONSTRAINT pk_notice PRIMARY KEY (id)
);

CREATE TABLE `organization`
(
    id            BINARY(16)   NOT NULL,
    name          VARCHAR(100) NULL,
    type          SMALLINT NOT NULL,
    `description` TEXT NULL,
    creator_id    BINARY(16)   NOT NULL,
    CONSTRAINT pk_organization PRIMARY KEY (id)
);

CREATE TABLE organization_member
(
    join_time       datetime NULL,
    `role`          SMALLINT NULL,
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
    ADD CONSTRAINT FK_JOBAPPLICATION_ON_ALUMNIID FOREIGN KEY (alumni_id) REFERENCES alumni (id);

ALTER TABLE job_application
    ADD CONSTRAINT FK_JOBAPPLICATION_ON_JOBPOSTID FOREIGN KEY (job_post_id) REFERENCES job_post (id);

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

DROP TABLE user;
ALTER TABLE activity_copy1
    DROP FOREIGN KEY activity_copy1_ibfk_1;

DROP TABLE activity_copy1;

DROP TABLE user;

ALTER TABLE notice
    DROP COLUMN type;

ALTER TABLE notice
    ADD type VARCHAR(255) NOT NULL;
ALTER TABLE business_entity
    DROP FOREIGN KEY FKm3c2mb1x2lniccoybdphcsvf5;

ALTER TABLE activity_copy1
    DROP FOREIGN KEY activity_copy1_ibfk_1;

CREATE UNIQUE INDEX IX_pk_organizationmember ON organization_member (organization_id, alumni_id);

DROP TABLE activity_copy1;

DROP TABLE user;

ALTER TABLE activity_application
    DROP COLUMN id;

ALTER TABLE business_entity
    DROP COLUMN added_by_id;

ALTER TABLE activity
    DROP COLUMN status;

ALTER TABLE business_entity
    MODIFY added_at datetime NOT NULL;

ALTER TABLE alumni
    MODIFY date_of_birth date NOT NULL;

ALTER TABLE alumni
    MODIFY gender SMALLINT NOT NULL;

ALTER TABLE alumni
    MODIFY real_name VARCHAR(255) NOT NULL;

ALTER TABLE donation
    DROP COLUMN status;

ALTER TABLE donation
    ADD status VARCHAR(255) NOT NULL;

ALTER TABLE donation_project
    DROP COLUMN status;

ALTER TABLE donation_project
    ADD status VARCHAR(255) NOT NULL;

ALTER TABLE job_application
    DROP COLUMN status;

ALTER TABLE job_application
    ADD status VARCHAR(255) NOT NULL;

ALTER TABLE notice
    DROP COLUMN type;

ALTER TABLE notice
    ADD type VARCHAR(255) NOT NULL;

ALTER TABLE activity_application
    ADD PRIMARY KEY (activity_id, alumni_id);
ALTER TABLE business_entity
    DROP FOREIGN KEY FKm3c2mb1x2lniccoybdphcsvf5;

ALTER TABLE activity_copy1
    DROP FOREIGN KEY activity_copy1_ibfk_1;

CREATE UNIQUE INDEX IX_pk_organizationmember ON organization_member (organization_id, alumni_id);

DROP TABLE activity_copy1;

DROP TABLE user;

ALTER TABLE activity_application
    DROP COLUMN id;

ALTER TABLE business_entity
    DROP COLUMN added_by_id;

ALTER TABLE activity
    DROP COLUMN status;

ALTER TABLE business_entity
    MODIFY added_at datetime NOT NULL;

ALTER TABLE alumni
    MODIFY date_of_birth date NOT NULL;

ALTER TABLE alumni
    MODIFY gender SMALLINT NOT NULL;

ALTER TABLE alumni
    MODIFY real_name VARCHAR(255) NOT NULL;

ALTER TABLE donation
    DROP COLUMN status;

ALTER TABLE donation
    ADD status VARCHAR(255) NOT NULL;

ALTER TABLE donation_project
    DROP COLUMN status;

ALTER TABLE donation_project
    ADD status VARCHAR(255) NOT NULL;

ALTER TABLE job_application
    DROP COLUMN status;

ALTER TABLE job_application
    ADD status VARCHAR(255) NOT NULL;

ALTER TABLE notice
    DROP COLUMN type;

ALTER TABLE notice
    ADD type VARCHAR(255) NOT NULL;

ALTER TABLE activity_application
    ADD PRIMARY KEY (activity_id, alumni_id);
ALTER TABLE business_entity
    DROP FOREIGN KEY FKm3c2mb1x2lniccoybdphcsvf5;

ALTER TABLE activity_copy1
    DROP FOREIGN KEY activity_copy1_ibfk_1;

CREATE UNIQUE INDEX IX_pk_organizationmember ON organization_member (organization_id, alumni_id);

DROP TABLE activity_copy1;

DROP TABLE user;

ALTER TABLE activity_application
    DROP COLUMN id;

ALTER TABLE business_entity
    DROP COLUMN added_by_id;

ALTER TABLE activity
    DROP COLUMN status;

ALTER TABLE business_entity
    MODIFY added_at datetime NOT NULL;

ALTER TABLE alumni
    MODIFY date_of_birth date NOT NULL;

ALTER TABLE alumni
    MODIFY gender SMALLINT NOT NULL;

ALTER TABLE alumni
    MODIFY real_name VARCHAR(255) NOT NULL;

ALTER TABLE donation
    DROP COLUMN status;

ALTER TABLE donation
    ADD status VARCHAR(255) NOT NULL;

ALTER TABLE donation_project
    DROP COLUMN status;

ALTER TABLE donation_project
    ADD status VARCHAR(255) NOT NULL;

ALTER TABLE job_application
    DROP COLUMN status;

ALTER TABLE job_application
    ADD status VARCHAR(255) NOT NULL;

ALTER TABLE activity_application
    ADD PRIMARY KEY (activity_id, alumni_id);