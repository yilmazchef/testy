package be.intecbrussel.student.data.mock;


import be.intecbrussel.student.data.entity.StudentEntity;
import com.github.javafaker.Faker;

import java.util.Locale;

public class StudentMock extends StudentEntity {

    private static final Faker FAKE = new Faker(Locale.getDefault());

    public StudentMock(Integer index, String newUserId) {

        setId(newUserId);

        setActive();
        setAuthenticated(true);
        setFirstName(FAKE.name().firstName());
        setLastName(FAKE.name().lastName());
        setClassName(FAKE.company().name());
        setUser(new UserMock(index));
        setRoles("STUDENT_ROLE");
    }

    public StudentMock(Integer index, String firstName, String lastName, String newUserId) {

        setId(newUserId);

        setActive();
        setAuthenticated(true);
        setFirstName(firstName);
        setLastName(lastName);
        setClassName(FAKE.company().name());
        setUser(new UserMock(index));
        setRoles("STUDENT_ROLE");
    }
}
