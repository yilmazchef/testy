package it.vkod.testy.views.component;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.*;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.chart.builder.ZoomBuilder;
import com.github.appreciated.apexcharts.config.xaxis.XAxisType;
import com.github.appreciated.apexcharts.helper.Series;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.Map;

public class BubbleChartLayout extends VerticalLayout {

    public BubbleChartLayout(final String title, final Map<String, Double[]> seriesMap) {


        final var series = seriesMap
                .entrySet()
                .stream()
                .map(entry -> new Series<>(entry.getKey(), entry.getValue()))
                .toArray(Series[]::new);

        ApexCharts bubbleChart = ApexChartsBuilder.get()
                .withChart(ChartBuilder.get()
                        .withType(Type.bubble)
                        .withZoom(ZoomBuilder.get()
                                .withEnabled(false)
                                .build())
                        .build())
                .withDataLabels(DataLabelsBuilder.get()
                        .withEnabled(false)
                        .build())
                .withFill(FillBuilder.get().withOpacity(0.8).build())
                .withTitle(TitleSubtitleBuilder.get().withText(title).build())
                .withSeries(series)
                .withXaxis(XAxisBuilder.get().withType(XAxisType.numeric).build())
                .withYaxis(YAxisBuilder.get().withMax(70.0).build())
                .build();

        setWidthFull();
        setAlignItems(Alignment.CENTER);
        setPadding(false);

        add(bubbleChart);
    }
}

