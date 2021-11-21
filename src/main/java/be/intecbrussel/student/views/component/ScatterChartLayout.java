package be.intecbrussel.student.views.component;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.XAxisBuilder;
import com.github.appreciated.apexcharts.config.builder.YAxisBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.chart.builder.ZoomBuilder;
import com.github.appreciated.apexcharts.config.chart.zoom.ZoomType;
import com.github.appreciated.apexcharts.helper.Series;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.math.BigDecimal;
import java.util.Map;

public class ScatterChartLayout extends VerticalLayout {

    public ScatterChartLayout(final Map<String, Double[][]> seriesMap) {

        final var series = seriesMap
                .entrySet()
                .stream()
                .map(entry -> new Series<>(entry.getKey(), entry.getValue()))
                .toArray(Series[]::new);

        ApexCharts scatterChart = ApexChartsBuilder.get()
                .withChart(ChartBuilder.get()
                        .withType(Type.scatter)
                        .withZoom(ZoomBuilder.get()
                                .withEnabled(true)
                                .withType(ZoomType.xy)
                                .build())
                        .build())
                .withSeries(series)
                .withXaxis(XAxisBuilder.get()
                        .withTickAmount(new BigDecimal("10"))
                        .build())
                .withYaxis(YAxisBuilder.get()
                        .withTickAmount(7.0)
                        .build())
                .build();

        setWidthFull();
        setAlignItems(Alignment.CENTER);
        setPadding(false);

        add(scatterChart);
    }
}

