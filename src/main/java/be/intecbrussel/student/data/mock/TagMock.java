package be.intecbrussel.student.data.mock;

import be.intecbrussel.student.data.entity.TagEntity;
import com.github.javafaker.Faker;

import java.util.Locale;

public class TagMock extends TagEntity {

    private static final Faker FAKE = new Faker(Locale.getDefault());

    public TagMock() {

    }
}
