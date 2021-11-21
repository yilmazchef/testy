package be.intecbrussel.student.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
public class DBConfig implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DBConfig.class);

    private final JdbcTemplate jdbc;

    @Autowired
    public DBConfig(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private Integer createTables() throws SQLException {

        final var sql = "CREATE TABLE IF NOT EXISTS testy_exam ( " + "\n" + "id varchar(500) NOT NULL UNIQUE,  " + "\n"
                + "code varchar(50) NOT NULL , " + "\n" + "active BIT(1) DEFAULT 1, " + "\n" + "session varchar(255), "
                + "\n" + "studentId varchar(500), " + "\n" + "taskId varchar(500) NOT NULL, " + "\n"
                + "startTime timestamp, " + "\n" + "endTime timestamp, " + "\n" + "score decimal(8,2), " + "\n"
                + "selected BIT(1) DEFAULT 1 DEFAULT FALSE, " + "\n" + "submitted BIT(1) DEFAULT 1 DEFAULT FALSE, "
                + "\n" + "PRIMARY KEY (id) " + "\n" + "); " + "\n" + " " + "\n"
                + "CREATE TABLE IF NOT EXISTS testy_solution (" + "\n" + "id varchar(500) NOT NULL UNIQUE," + "\n"
                + "active BIT(1) DEFAULT 1," + "\n" + "examId varchar(500) NOT NULL ," + "\n"
                + "teacherId varchar(500) NOT NULL ," + "\n" + "content varchar(500) NOT NULL," + "\n"
                + "likes INT DEFAULT 0 ," + "\n" + "dislikes INT DEFAULT 0," + "\n" + "PRIMARY KEY (id)" + "\n" + ");"
                + "\n" + " " + "\n" + "CREATE TABLE IF NOT EXISTS testy_question (" + "\n"
                + "    id VARCHAR(500) NOT NULL UNIQUE," + "\n" + "    active BIT(1) DEFAULT 1," + "\n"
                + "    orderNo INT," + "\n" + "    header TEXT(1000) NOT NULL ," + "\n"
                + "    content TEXT(1000) NOT NULL ," + "\n" + "    weight DECIMAL(8,2) ," + "\n"
                + "    teacherId VARCHAR(500) NOT NULL ," + "\n" + "    PRIMARY KEY (id)" + "\n" + ");" + "\n" + " "
                + "\n" + "CREATE TABLE IF NOT EXISTS testy_task (" + "\n" + "    id varchar(500) NOT NULL UNIQUE,"
                + "\n" + "    active BIT(1) DEFAULT 1," + "\n" + "    orderNo INT," + "\n"
                + "    value TEXT(1000) NOT NULL ," + "\n" + "    weight decimal(8,2)," + "\n"
                + "    type varchar(10) NOT NULL ," + "\n" + "    questionId varchar(500) NOT NULL ," + "\n"
                + "    PRIMARY KEY (id)" + "\n" + ");" + "\n" + " " + "\n"
                + "CREATE TABLE IF NOT EXISTS testy_category (" + "\n" + "    id varchar(500) NOT NULL UNIQUE," + "\n"
                + "    active BIT(1) DEFAULT 1," + "\n" + "    value TEXT NOT NULL," + "\n"
                + "    parentId varchar(500) ," + "\n" + "    PRIMARY KEY (id)" + "\n" + ");" + "\n" + " " + "\n"
                + "CREATE TABLE IF NOT EXISTS testy_tag (" + "\n" + "    id varchar(500) NOT NULL UNIQUE," + "\n"
                + "    active BIT(1) DEFAULT 1," + "\n" + "    categoryId varchar(500) ," + "\n"
                + "    questionId varchar(500) ," + "\n" + "    weight decimal(8,2)," + "\n" + "    PRIMARY KEY (id)"
                + "\n" + "); " + "\n" + "\n" + "\n" + "CREATE TABLE IF NOT EXISTS testy_user ( " + "\n"
                + "    id varchar(500) NOT NULL UNIQUE, " + "\n" + "    active BIT(1) DEFAULT 1, " + "\n"
                + "    authenticated BIT(1) DEFAULT 1 DEFAULT FALSE, " + "\n" + "    roles varchar(255) NOT NULL , "
                + "\n" + "    username varchar(255) NOT NULL UNIQUE, " + "\n" + "    password varchar(255) NOT NULL , "
                + "\n" + "    activation varchar(255) NOT NULL , " + "\n" + "    PRIMARY KEY (id) " + "\n" + "); "
                + "\n" + " " + "\n" + "CREATE TABLE IF NOT EXISTS testy_student ( " + "\n"
                + "    id varchar(500) NOT NULL UNIQUE, " + "\n" + "    active BIT(1) DEFAULT 1, " + "\n"
                + "    firstName varchar(255) NOT NULL , " + "\n" + "    lastName varchar(255) NOT NULL , " + "\n"
                + "    className varchar(50) NOT NULL , " + "\n" + "    userId varchar(255) NOT NULL , " + "\n"
                + "    PRIMARY KEY (id) " + "\n" + "); " + "\n" + " " + "\n"
                + "CREATE TABLE IF NOT EXISTS testy_teacher ( " + "\n" + "    id varchar(500) NOT NULL UNIQUE, " + "\n"
                + "    active BIT(1) DEFAULT 1, " + "\n" + "    firstName varchar(255) NOT NULL , " + "\n"
                + "    lastName varchar(255) NOT NULL , " + "\n" + "    department varchar(255) , " + "\n"
                + "    userId varchar(255) NOT NULL , " + "\n" + "    PRIMARY KEY (id) " + "\n" + "); " + "\n" + " "
                + "\n" + "CREATE TABLE IF NOT EXISTS testy_manager ( " + "\n" + "    id varchar(500) NOT NULL UNIQUE, "
                + "\n" + "    active BIT(1) DEFAULT 1, " + "\n" + "    firstName varchar(255) NOT NULL , " + "\n"
                + "    lastName varchar(255) NOT NULL , " + "\n" + "    userId varchar(255) NOT NULL , " + "\n"
                + "    PRIMARY KEY (id) " + "\n" + "); " + "\n" + " " + "\n" + "ALTER TABLE testy_exam " + "\n"
                + "ADD CONSTRAINT fk_student " + "\n" + "FOREIGN KEY (studentId) REFERENCES testy_student(id); " + "\n"
                + "  " + "\n" + "ALTER TABLE testy_exam " + "\n" + "ADD CONSTRAINT fk_task " + "\n"
                + "FOREIGN KEY (taskId) REFERENCES testy_task(id); " + "\n";

        log.info(sql);

        int createdTablesCount = jdbc.update(sql);

        if (createdTablesCount <= 0)
            throw new SQLException("Tables cannot be created.");

        return createdTablesCount;
    }

    @Override
    public void run(String... args) throws Exception {
        createTables();
    }

}
