package be.intecbrussel.student.views.component;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.*;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.xaxis.XAxisType;
import com.github.appreciated.apexcharts.helper.Coordinate;
import com.github.appreciated.apexcharts.helper.Series;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.Map;

public class HeatmapChartLayout extends VerticalLayout {

    public HeatmapChartLayout(final String title, final Map<String, Map<String, Double>> seriesMap) {

        final var series = seriesMap
                .entrySet()
                .stream()
                .map(entry -> {
                    final var coordinates = entry
                            .getValue()
                            .entrySet()
                            .stream()
                            .map(valueEntry -> new Coordinate<>(valueEntry.getKey(), valueEntry.getValue()))
                            .toArray(Coordinate[]::new);

                    return new Series<Coordinate<String, Double>>(entry.getKey(), coordinates);
                })
                .toArray(Series[]::new);

        ApexCharts heatmapChart = ApexChartsBuilder.get()
                .withChart(
                        ChartBuilder.get()
                                .withType(Type.heatmap)
                                .build())
                .withDataLabels(DataLabelsBuilder.get()
                        .withEnabled(false)
                        .build())
                .withColors("#008FFB")
                .withTitle(TitleSubtitleBuilder.get().withText(title).build())
                .withSeries(series)
                .withXaxis(XAxisBuilder.get().withType(XAxisType.numeric).build())
                .withYaxis(YAxisBuilder.get().withMax(70.0).build())
                .build();

        setWidthFull();
        setAlignItems(Alignment.CENTER);
        setPadding(false);

        add(heatmapChart);
    }
}
