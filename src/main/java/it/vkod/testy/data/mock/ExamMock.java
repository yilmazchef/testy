package it.vkod.testy.data.mock;


import it.vkod.testy.data.entity.ExamEntity;
import com.github.javafaker.Faker;

import java.util.Locale;
import java.util.UUID;

public class ExamMock extends ExamEntity {

    private static final Faker FAKE = new Faker(Locale.getDefault());

    public ExamMock() {
        setId(UUID.randomUUID().toString());
        setActive(Boolean.TRUE);

    }
}
