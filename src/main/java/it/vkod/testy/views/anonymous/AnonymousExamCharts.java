package it.vkod.testy.views.anonymous;

import it.vkod.testy.data.dto.ExamDto;
import it.vkod.testy.data.dto.TaskDto;
import it.vkod.testy.views.component.GradientRadialBarChartLayout;
import it.vkod.testy.views.component.PieChartLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

@AnonymousAllowed
public class AnonymousExamCharts extends VerticalLayout {

    public AnonymousExamCharts(List<ExamDto> exams) {

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

        final var pieLayoutsGroup = new HorizontalLayout();

        final var pieChartLayout = new PieChartLayout(
                new String[]{"Correct", "Wrong"},
                new Double[]{questionTotal, allTotal - questionTotal},
                allTotal
        );

        final var successRateValue = BigDecimal.valueOf(questionTotal)
                .divide(BigDecimal.valueOf(allTotal), new MathContext(2))
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
