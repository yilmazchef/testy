package be.intecbrussel.student.data.mock;


import be.intecbrussel.student.data.entity.QuestionEntity;
import com.github.javafaker.Faker;

import java.util.Locale;

public class QuestionMock extends QuestionEntity {

    private static final Faker FAKE = new Faker(Locale.getDefault());

    public QuestionMock(Integer questionIndex, String teacherId) {
        String header = FAKE.lorem().sentence(10);
        String content = questionIndex + " " + String.join("\n", FAKE.lorem().paragraphs(2)).concat("<code>").concat(String.join("\n", FAKE.lorem().sentences(2))).concat("</code>");
        setOrderNo(questionIndex);
        setHeader(header);
        setContent(content);
        setWeight(0.00);
        setTeacherId(teacherId);
        setActive();
    }
}
