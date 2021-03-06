# noinspection SqlNoDataSourceInspectionForFile

CREATE DATABASE IF NOT EXISTS testy_dev;

USE testy_dev;

-- DROP TABLE IF EXISTS testy_solution;
-- DROP TABLE IF EXISTS testy_exam;
-- DROP TABLE IF EXISTS testy_task;
-- DROP TABLE IF EXISTS testy_tag;
-- DROP TABLE IF EXISTS testy_question;
-- DROP TABLE IF EXISTS testy_category;
-- DROP TABLE IF EXISTS testy_user;


CREATE TABLE IF NOT EXISTS testy_user
(
    id            varchar(500) NOT NULL UNIQUE,
    firstName     varchar(255),
    lastName      varchar(255),
    authenticated BIT(1) DEFAULT 1 DEFAULT FALSE,
    roles         varchar(255) NOT NULL,
    username      varchar(255) NOT NULL UNIQUE,
    email         varchar(100) NULL,
    phone         varchar(14)  NULL,
    password      varchar(255) NOT NULL,
    course        varchar(255) NOT NULL DEFAULT 'ONGEDEFINEERD',
    activation    varchar(255) NOT NULL,
    active        BIT(1) DEFAULT 1,
    PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS testy_question
(
    id        VARCHAR(500) NOT NULL UNIQUE,
    active    BIT(1) DEFAULT 1,
    orderNo   INT,
    header    TEXT(1000)   NOT NULL,
    content   TEXT(1000)   NOT NULL,
    weight    DECIMAL(8, 2),
    teacherId VARCHAR(500) NOT NULL,
    PRIMARY KEY (id),

    CONSTRAINT testy_teacher_to_question
        FOREIGN KEY (teacherId) REFERENCES testy_user (id)
);

CREATE TABLE IF NOT EXISTS testy_task
(
    id         varchar(500) NOT NULL UNIQUE,
    active     BIT(1) DEFAULT 1,
    orderNo    INT,
    value      TEXT(1000)   NOT NULL,
    weight     decimal(8, 2),
    type       varchar(10)  NOT NULL,
    questionId varchar(500) NOT NULL,
    PRIMARY KEY (id),

    CONSTRAINT testy_question_to_task
        FOREIGN KEY (questionId) REFERENCES testy_question (id)
);

CREATE TABLE IF NOT EXISTS testy_exam
(
    id          varchar(500) NOT NULL UNIQUE,
    code        varchar(50)  NOT NULL,
    active      BIT(1) DEFAULT 1,
    session     varchar(255) NULL,
    organizerId varchar(500) NULL,
    studentId   varchar(500) NULL,
    taskId      varchar(500) NOT NULL,
    startTime   timestamp,
    endTime     timestamp,
    score       decimal(8, 2),
    selected    BIT(1) DEFAULT 1 DEFAULT FALSE,
    submitted   BIT(1) DEFAULT 1 DEFAULT FALSE,
    PRIMARY KEY (id),

    CONSTRAINT fk_student_to_exam
        FOREIGN KEY (studentId) REFERENCES testy_user (id),

    CONSTRAINT fk_organizer_to_exam
        FOREIGN KEY (organizerId) REFERENCES testy_user (id),

    CONSTRAINT fk_task_to_exam
        FOREIGN KEY (taskId) REFERENCES testy_task (id)
    
);


CREATE TABLE IF NOT EXISTS testy_solution
(
    id        varchar(500) NOT NULL UNIQUE,
    active    BIT(1) DEFAULT 1,
    examId    varchar(500) NOT NULL,
    teacherId varchar(500) NOT NULL,
    content   varchar(500) NOT NULL,
    likes     INT    DEFAULT 0,
    dislikes  INT    DEFAULT 0,
    PRIMARY KEY (id),

    CONSTRAINT fk_teacher_to_solution
        FOREIGN KEY (teacherId) REFERENCES testy_user (id),

    CONSTRAINT fk_exam_to_solution
        FOREIGN KEY (examId) REFERENCES testy_exam (id)
);


CREATE TABLE IF NOT EXISTS testy_category
(
    id       varchar(500) NOT NULL UNIQUE,
    active   BIT(1) DEFAULT 1,
    value    TEXT         NOT NULL,
    parentId varchar(500),
    PRIMARY KEY (id),

    CONSTRAINT testy_parent_to_category
        FOREIGN KEY (parentId) REFERENCES testy_category (id)
);


CREATE TABLE IF NOT EXISTS testy_tag
(
    id         varchar(500) NOT NULL UNIQUE,
    active     BIT(1) DEFAULT 1,
    categoryId varchar(500),
    questionId varchar(500),
    weight     decimal(8, 2),
    PRIMARY KEY (id),

    CONSTRAINT testy_category_to_tag
        FOREIGN KEY (categoryId) REFERENCES testy_category (id),

    CONSTRAINT testy_question_to_tag
        FOREIGN KEY (questionId) REFERENCES testy_question (id) 
);

