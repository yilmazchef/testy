package be.intecbrussel.student.data.mock;


import be.intecbrussel.student.data.entity.ManagerEntity;
import be.intecbrussel.student.data.entity.TeacherEntity;
import com.github.javafaker.Faker;

import java.util.Locale;

public class TeacherMock extends TeacherEntity {

    private static final Faker FAKE = new Faker(Locale.getDefault());

    public TeacherMock(int index, String newUserId) {

        setId(newUserId);

        setAuthenticated(true);
        setDepartment(FAKE.company().industry());
        setFirstName(FAKE.name().firstName());
        setLastName(FAKE.name().lastName());
        setUser(new UserMock(index, FAKE.name().username(), FAKE.internet().password()));
        setRoles("TEACHER_ROLE");

    }

    public TeacherMock(int index, String firstName, String lastName, String newUserId) {

        setId(newUserId);

        setAuthenticated(true);
        setFirstName(firstName);
        setLastName(lastName);
        setDepartment(FAKE.company().industry());
        setUser(new UserMock(index, FAKE.name().username(), FAKE.internet().password()));
        setRoles("TEACHER_ROLE");

    }
}
