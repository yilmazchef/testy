package it.vkod.testy.data.mock;


import it.vkod.testy.data.dto.TaskDto;
import it.vkod.testy.data.entity.TaskEntity;
import com.github.javafaker.Faker;

import java.util.Locale;

public class TaskMock extends TaskEntity {

    private static final Faker FAKE = new Faker(Locale.getDefault());

    public TaskMock(Integer taskIndex, TaskDto.Type type, String savedQuestionId) {

        if (type == TaskDto.Type.TODO) {

            String value = String.join(" ", FAKE.lorem().sentences(2));
            double weight = taskIndex % 2 == 0 ? 0.00 : FAKE.number().randomDouble(2, 0, 1);
            setOrderNo(taskIndex);
            setType("TODO");
            setValue(value);
            setWeight(weight);
            setQuestionId(savedQuestionId);
            setActive();

        } else if (type == TaskDto.Type.CHOICE) {

            String value = String.join(" ", FAKE.lorem().sentences(2));
            double weight = taskIndex % 2 == 0 ? 1.00 : 0.00;

            setOrderNo(taskIndex);
            setType("CHOICE");
            setValue(value);
            setWeight(weight);
            setQuestionId(savedQuestionId);
            setActive();
        }

    }
}
