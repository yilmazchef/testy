package be.intecbrussel.student.views.component;

import be.intecbrussel.student.data.dto.ExamDto;
import be.intecbrussel.student.data.dto.QuestionDto;
import be.intecbrussel.student.data.dto.TaskDto;
import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.LegendBuilder;
import com.github.appreciated.apexcharts.config.builder.ResponsiveBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.legend.Position;
import com.github.appreciated.apexcharts.config.responsive.builder.OptionsBuilder;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class PieChartLayout extends VerticalLayout {

    public PieChartLayout(final String[] labels, final Double[] series, final Double breakpoint) {

        final var chart = ApexChartsBuilder.get()
                .withChart(ChartBuilder.get().withType(Type.pie).build())
                .withLabels(labels)
                .withLegend(LegendBuilder.get()
                        .withPosition(Position.right)
                        .build())
                .withSeries(series)
                .withResponsive(ResponsiveBuilder.get()
                        .withBreakpoint(breakpoint)
                        .withOptions(OptionsBuilder.get()
                                .withLegend(LegendBuilder.get()
                                        .withPosition(Position.bottom)
                                        .build())
                                .build())
                        .build())
                .build();

        setAlignItems(Alignment.CENTER);
        setWidthFull();
        setPadding(false);

        add(chart);
    }

}

