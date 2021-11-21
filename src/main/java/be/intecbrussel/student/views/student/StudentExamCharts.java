package be.intecbrussel.student.views.student;

import be.intecbrussel.student.data.dto.ExamDto;
import be.intecbrussel.student.data.dto.TaskDto;
import be.intecbrussel.student.views.component.GradientRadialBarChartLayout;
import be.intecbrussel.student.views.component.PieChartLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

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
