package it.vkod.testy.views.student;

import it.vkod.testy.data.dto.ExamDto;
import it.vkod.testy.data.dto.TaskDto;
import it.vkod.testy.views.component.GradientRadialBarChartLayout;
import it.vkod.testy.views.component.PieChartLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import javax.annotation.security.PermitAll;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@PermitAll
public class StudentExamCharts extends VerticalLayout {

    public StudentExamCharts(List<ExamDto> exams) {

        final var questionTotal = exams
                .stream()
                .filter(ExamDto::getSelected)
                .map(ExamDto::getTask)
                .mapToDouble(TaskDto::getWeight)
                .sum();

        final var allTotal = exams
                .stream()
                .map(ExamDto::getTask)
                .mapToDouble(TaskDto::getWeight)
                .sum();

        final var pieLayoutsGroup = new VerticalLayout();

        final var pieChartLayout = new PieChartLayout(
                new String[]{"Correct", "Wrong"},
                new Double[]{questionTotal, allTotal - questionTotal},
                allTotal
        );

        final var successRateValue = BigDecimal.valueOf(questionTotal)
                .divide(BigDecimal.valueOf(allTotal), RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();
        final var gradientChartLayout = new GradientRadialBarChartLayout(
                new String[]{"Success Rate"},
                new Double[]{successRateValue}
        );

        pieLayoutsGroup.setWidthFull();
        pieLayoutsGroup.setJustifyContentMode(JustifyContentMode.CENTER);
        pieLayoutsGroup.add(pieChartLayout, gradientChartLayout);

        add(pieLayoutsGroup);

    }
}
