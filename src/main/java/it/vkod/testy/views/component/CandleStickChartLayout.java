package it.vkod.testy.views.component;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.*;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.subtitle.Align;
import com.github.appreciated.apexcharts.config.xaxis.XAxisType;
import com.github.appreciated.apexcharts.helper.Coordinate;
import com.github.appreciated.apexcharts.helper.Series;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.Map;

public class CandleStickChartLayout extends VerticalLayout {

    public CandleStickChartLayout(final String title, final Map<String, Double[]> seriesWithCoordinates) {

        ApexCharts barChart = ApexChartsBuilder.get()
                .withChart(ChartBuilder.get()
                        .withType(Type.candlestick)
                        .build())
                .withTitle(TitleSubtitleBuilder.get()
                        .withText(title)
                        .withAlign(Align.left)
                        .build())
                .withSeries(new Series<>(
                        seriesWithCoordinates
                                .entrySet()
                                .stream()
                                .map(entry -> new Coordinate<>(entry.getKey(), entry.getValue()))
                                .toArray(Coordinate[]::new)
                ))
                .withXaxis(XAxisBuilder.get()
                        .withType(XAxisType.categories)
                        .build())
                .withYaxis(YAxisBuilder.get()
                        .withTooltip(TooltipBuilder.get()
                                .withEnabled(true)
                                .build())
                        .build())
                .build();

        setWidthFull();
        setPadding(false);
        setAlignItems(Alignment.CENTER);

        add(barChart);

    }
}

