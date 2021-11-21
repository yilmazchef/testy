CREATE TABLE IF NOT EXISTS testy_exam (
    id varchar(500) NOT NULL UNIQUE,
    code varchar(50) NOT NULL ,
    active BIT(1) DEFAULT 1,
    session varchar(255),
    studentId varchar(500),
    taskId varchar(500) NOT NULL,
    startTime timestamp,
    endTime timestamp,
    score decimal(8,2),
    selected BIT(1) DEFAULT 1 DEFAULT FALSE,
    submitted BIT(1) DEFAULT 1 DEFAULT FALSE,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS testy_solution (
    id varchar(500) NOT NULL UNIQUE,
    active BIT(1) DEFAULT 1,
    examId varchar(500) NOT NULL ,
    teacherId varchar(500) NOT NULL ,
    content varchar(500) NOT NULL,
    likes INT DEFAULT 0 ,
    dislikes INT DEFAULT 0,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS testy_question (
    id VARCHAR(500) NOT NULL UNIQUE,
    active BIT(1) DEFAULT 1,
    orderNo INT,
    header TEXT(1000) NOT NULL ,
    content TEXT(1000) NOT NULL ,
    weight DECIMAL(8,2) ,
    teacherId VARCHAR(500) NOT NULL ,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS testy_task (
    id varchar(500) NOT NULL UNIQUE,
    active BIT(1) DEFAULT 1,
    orderNo INT,
    value TEXT(1000) NOT NULL ,
    weight decimal(8,2),
    type varchar(10) NOT NULL ,
    questionId varchar(500) NOT NULL ,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS testy_category (
    id varchar(500) NOT NULL UNIQUE,
    active BIT(1) DEFAULT 1,
    value TEXT NOT NULL,
    parentId varchar(500) ,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS testy_tag (
    id varchar(500) NOT NULL UNIQUE,
    active BIT(1) DEFAULT 1,
    categoryId varchar(500) ,
    questionId varchar(500) ,
    weight decimal(8,2),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS testy_user (
    id varchar(500) NOT NULL UNIQUE,
    active BIT(1) DEFAULT 1,
    authenticated BIT(1) DEFAULT 1 DEFAULT FALSE,
    roles varchar(255) NOT NULL ,
    username varchar(255) NOT NULL UNIQUE,
    password varchar(255) NOT NULL ,
    activation varchar(255) NOT NULL ,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS testy_student (
    id varchar(500) NOT NULL UNIQUE,
    active BIT(1) DEFAULT 1,
    firstName varchar(255) NOT NULL ,
    lastName varchar(255) NOT NULL ,
    className varchar(50) NOT NULL ,
    userId varchar(255) NOT NULL ,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS testy_teacher (
    id varchar(500) NOT NULL UNIQUE,
    active BIT(1) DEFAULT 1,
    firstName varchar(255) NOT NULL ,
    lastName varchar(255) NOT NULL ,
    department varchar(255) ,
    userId varchar(255) NOT NULL ,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS testy_manager (
    id varchar(500) NOT NULL UNIQUE,
    active BIT(1) DEFAULT 1,
    firstName varchar(255) NOT NULL ,
    lastName varchar(255) NOT NULL ,
    userId varchar(255) NOT NULL ,
    PRIMARY KEY (id)
);

ALTER TABLE testy_exam
ADD CONSTRAINT fk_student
FOREIGN KEY (studentId) REFERENCES testy_student(id);

ALTER TABLE testy_exam
ADD CONSTRAINT fk_task
FOREIGN KEY (taskId) REFERENCES testy_task(id);

