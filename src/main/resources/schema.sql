DROP TABLE IF EXISTS Schedules;
DROP TABLE IF EXISTS Users;

CREATE TABLE `Users`
(
    `user_id`         bigint AUTO_INCREMENT COMMENT '고유 식별자',
    `email`           varchar(50) NOT NULL UNIQUE COMMENT '이메일',
    `password`        varchar(20) NOT NULL COMMENT '비밀번호',
    `name`            varchar(50) NOT NULL COMMENT '이름',
    `created_at`      timestamp   NOT NULL COMMENT '작성일',
    `last_updated_at` timestamp   NOT NULL COMMENT '수정일'
);

CREATE TABLE `Schedules`
(
    `schedule_id`      bigint AUTO_INCREMENT COMMENT '고유 식별자',
    `author_id`        bigint       NOT NULL COMMENT '작성자 고유 식별자',
    `title`            varchar(30)  NOT NULL COMMENT '할일 제목',
    `content`          varchar(500) NOT NULL COMMENT '할일 내용',
    `created_at`       timestamp    NOT NULL COMMENT '작성일',
    `last_updated_at` timestamp    NOT NULL COMMENT '수정일'
);

ALTER TABLE `Users`
    ADD CONSTRAINT `PK_USERS` PRIMARY KEY (`user_id`);

ALTER TABLE `Schedules`
    ADD CONSTRAINT `PK_SCHEDULES` PRIMARY KEY (`schedule_id`);