package it.vkod.testy.views.component;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.TitleSubtitleBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.helper.Series;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.Map;

public class RadarChartLayout extends VerticalLayout {

    public RadarChartLayout(final String title, final String[] labels, final Map<String, Double[]> seriesMap) {

        final var series = seriesMap.entrySet().stream().map(entry -> new Series<>(entry.getKey(), entry.getValue())).toArray(Series[]::new);

        ApexCharts radarChart = ApexChartsBuilder.get()
                .withChart(ChartBuilder.get()
                        .withType(Type.radar)
                        .build())
                .withSeries(series)
                .withTitle(TitleSubtitleBuilder.get()
                        .withText(title)
                        .build())
                .withLabels(labels)
                .build();

        setWidthFull();
        setAlignItems(Alignment.CENTER);
        setPadding(false);

        add(radarChart);
    }
}

