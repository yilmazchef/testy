package be.intecbrussel.student.data.mock;

import be.intecbrussel.student.data.entity.CategoryEntity;
import com.github.javafaker.Faker;

import java.util.Locale;
import java.util.UUID;

public class CategoryMock extends CategoryEntity {

    private static final Faker FAKE = new Faker(Locale.getDefault());

    private final String[] data = new String[]{
            "Java Fundamentals",
            "Enums",
            "Arrays",
            "Wrapper Classes",
            "Inheritance",
            "Polymorphism",
            "Interfaces",
            "Exception Handling",
            "Advanced Java",
            "Functional Java",
            "Project Reactor",
            "Databases",
            "SQL",
            "JPA",
            "Hibernate",
            "Template Engines",
            "Vaadin",
            "Thymeleaf",
            "Spring",
            "Spring Boot",
            "Spring Cloud"
    };

    public CategoryMock(Integer index) {
        setId(UUID.randomUUID().toString());
        setActive();
        setValue(data[index]);
        setParentId(null);
    }
}
