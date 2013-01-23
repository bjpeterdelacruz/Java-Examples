/*******************************************************************************
 * Copyright (C) 2012 BJ Peter DeLaCruz
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.bpd.jfreechart;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StackedXYAreaRenderer2;
import org.jfree.data.time.FixedMillisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;
import org.jfree.data.time.TimeTableXYDataset;
import com.bpd.utils.math.Point;

/**
 * Test class.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@hawaii.edu>
 */
public class StackedAreaChartDemo {

  /**
   * Test program that will display a JFreeChart showing interpolated data points.
   * 
   * @param args <b>"1"</b> to display Series 1. <b>"2"</b> to display Series 2. <b>"0"</b> to
   * display both series.
   */
  public static void main(String... args) {
    // Check arguments.
    if (args.length != 1) {
      System.err.println("Usage: java Chart [0|1|2]\n\n  -- 0: Display Series 1.");
      System.err.println("  -- 1: Display Series 2.\n  -- 2: Display both series.");
      return;
    }

    String option = args[0];
    if (!"0".equals(option) && !"1".equals(option) && !"2".equals(option)) {
      System.err.println("Invalid argument: " + option);
      return;
    }

    // Create some sample data.
    List<Point<Number, Number>> list1 = new ArrayList<Point<Number, Number>>();
    list1.add(new Point<Number, Number>(50, 100.0));
    list1.add(new Point<Number, Number>(150, 100));
    list1.add(new Point<Number, Number>(250, 200));
    list1.add(new Point<Number, Number>(350, 400));
    list1.add(new Point<Number, Number>(450, 200));
    list1.add(new Point<Number, Number>(550, 100));

    List<Point<Number, Number>> list2 = new ArrayList<Point<Number, Number>>();
    list2.add(new Point<Number, Number>(50, 100.0));
    list2.add(new Point<Number, Number>(150, 200.0));
    list2.add(new Point<Number, Number>(250, 400.0));
    list2.add(new Point<Number, Number>(350, 600.0));
    list2.add(new Point<Number, Number>(450, 400.0));
    list2.add(new Point<Number, Number>(550, 200.0));

    // Add data to time series.
    TimeSeries series1 = new TimeSeries("Series 1", FixedMillisecond.class);
    for (Point<Number, Number> dataPoint : list1) {
      if ("1".equals(option) || "0".equals(option)) {
        series1.add(new FixedMillisecond(dataPoint.getX().longValue()), dataPoint.getY());
      }
      series1.setDescription("Series 1");
    }

    TimeSeries series2 = new TimeSeries("Series 2", FixedMillisecond.class);
    for (Point<Number, Number> dataPoint : list2) {
      if ("2".equals(option) || "0".equals(option)) {
        series2.add(new FixedMillisecond(dataPoint.getX().longValue()), dataPoint.getY());
      }
      series2.setDescription("Series 2");
    }

    TimeSeriesCollection collection = new TimeSeriesCollection();
    if ("1".equals(option)) {
      collection.addSeries(series1);
    }
    else if ("2".equals(option)) {
      collection.addSeries(series2);
    }
    else if ("0".equals(option)) {
      collection.addSeries(series1);
      collection.addSeries(series2);
    }

    TimeTableXYDataset dataset = new TimeTableXYDataset();
    @SuppressWarnings("unchecked")
    List<TimeSeries> timeSeriesList = collection.getSeries();
    for (TimeSeries t : timeSeriesList) {
      for (int index = 0; index < t.getItemCount(); index++) {
        TimeSeriesDataItem dataItem = (TimeSeriesDataItem) t.getItems().get(index);
        dataset.add(t.getTimePeriod(index), dataItem.getValue().doubleValue(),
            t.getDescription());
      }
    }

    // Create and display chart.
    JFreeChart chart =
        ChartFactory.createStackedXYAreaChart(null, null, null, dataset, PlotOrientation.VERTICAL,
            false, true, false);

    customizeChart(chart);

    ChartPanel chartPanel = new ChartPanel(chart);
    chartPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

    JFrame frame = new JFrame();
    frame.getContentPane().add(chartPanel);
    frame.pack();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }

  /**
   * Customizes the JFreeChart.
   * 
   * @param chart The chart to customize.
   */
  private static void customizeChart(JFreeChart chart) {
    StackedXYAreaRenderer2 renderer = (StackedXYAreaRenderer2) chart.getXYPlot().getRenderer();
    renderer.setSeriesPaint(0, Color.RED);
    renderer.setSeriesPaint(1, Color.BLUE);

    XYPlot plot = chart.getXYPlot();
    plot.getDomainAxis().setLowerMargin(0.0);
    plot.getDomainAxis().setUpperMargin(0.0);
    plot.setBackgroundPaint(Color.WHITE);
    plot.setDomainGridlinePaint(Color.WHITE);
    plot.setRangeGridlinePaint(Color.BLACK);
  }

}
