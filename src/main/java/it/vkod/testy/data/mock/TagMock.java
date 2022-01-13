package it.vkod.testy.data.mock;

import it.vkod.testy.data.entity.TagEntity;
import com.github.javafaker.Faker;

import java.util.Locale;

public class TagMock extends TagEntity {

    private static final Faker FAKE = new Faker(Locale.getDefault());

    public TagMock() {

    }
}
