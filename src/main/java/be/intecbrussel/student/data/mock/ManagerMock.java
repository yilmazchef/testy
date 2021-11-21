package be.intecbrussel.student.data.mock;


import be.intecbrussel.student.data.entity.ManagerEntity;
import be.intecbrussel.student.data.entity.TeacherEntity;
import com.github.javafaker.Faker;

import java.util.Locale;

public class ManagerMock extends ManagerEntity {

    private static final Faker FAKE = new Faker(Locale.getDefault());

    public ManagerMock(int index, String newUserId) {
        setId(newUserId);
        setFirstName(FAKE.name().firstName());
        setLastName(FAKE.name().lastName());
        setUser(new UserMock(index, FAKE.name().username(), FAKE.internet().password()));
        setRoles("MANAGER_ROLE");
    }

    public ManagerMock(int index, String firstName, String lastName, String newUserId) {
        setId(newUserId);
        setFirstName(firstName);
        setLastName(lastName);
        setUser(new UserMock(index, FAKE.name().username(), FAKE.internet().password()));
        setRoles("MANAGER_ROLE");
    }
}
