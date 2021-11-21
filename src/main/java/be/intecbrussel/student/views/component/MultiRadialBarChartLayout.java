package be.intecbrussel.student.views.component;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class MultiRadialBarChartLayout extends VerticalLayout {

    public MultiRadialBarChartLayout(final String[] labels, final Double[] series) {

        ApexCharts multiRadialBarChart = ApexChartsBuilder.get()
                .withChart(ChartBuilder.get()
                        .withType(Type.radialBar)
                        .build())
                .withSeries(series)
                .withLabels(labels)
                .build();

        setWidthFull();
        setAlignItems(Alignment.CENTER);
        setPadding(false);

        add(multiRadialBarChart);
    }
}

