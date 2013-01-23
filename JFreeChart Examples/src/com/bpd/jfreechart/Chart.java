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

import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.FixedMillisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import com.bpd.utils.math.Interpolator;
import com.bpd.utils.math.Point;

/**
 * Test class.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@hawaii.edu>
 */
public class Chart {

  /**
   * Test program that will display a JFreeChart showing interpolated data points.
   * 
   * @param args <b>"1"</b> to display Series 1. <b>"2"</b> to display Series 2. <b>"0"</b> to
   * display both series.
   */
  public static void main(String... args) {
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

    List<Point<Number, Number>> list1 = new ArrayList<Point<Number, Number>>();
    list1.add(new Point<Number, Number>(100, 100));
    list1.add(new Point<Number, Number>(200, 200));
    list1.add(new Point<Number, Number>(300, 400));
    list1.add(new Point<Number, Number>(400, 200));
    list1.add(new Point<Number, Number>(500, 100));

    List<Point<Number, Number>> list2 = new ArrayList<Point<Number, Number>>();
    list2.add(new Point<Number, Number>(50, 100.0));
    list2.add(new Point<Number, Number>(150, 200.0));
    list2.add(new Point<Number, Number>(250, 400.0));
    list2.add(new Point<Number, Number>(350, 600.0));
    list2.add(new Point<Number, Number>(450, 400.0));
    list2.add(new Point<Number, Number>(550, 200.0));

    List<List<Point<Number, Number>>> lists = new ArrayList<List<Point<Number, Number>>>();
    List<Point<Number, Number>> extrapolatedList1 = new ArrayList<Point<Number, Number>>();
    List<Point<Number, Number>> extrapolatedList2 = new ArrayList<Point<Number, Number>>();
    if ("1".equals(option)) {
      lists.add(list1);
      extrapolatedList1 = Interpolator.interpolate(list1, lists);
    }
    else if ("2".equals(option)) {
      lists.add(list2);
      extrapolatedList2 = Interpolator.interpolate(list2, lists);
    }
    else if ("0".equals(option)) {
      lists.add(list1);
      lists.add(list2);
      extrapolatedList1 = Interpolator.interpolate(list1, lists);
      extrapolatedList2 = Interpolator.interpolate(list2, lists);
    }

    TimeSeries series1 = new TimeSeries("Series 1", FixedMillisecond.class);
    for (Point<Number, Number> dataPoint : extrapolatedList1) {
      if ("1".equals(option) || "0".equals(option)) {
        series1.add(new FixedMillisecond(dataPoint.getX().longValue()), dataPoint.getY());
      }
      System.err.println(dataPoint.getX() + "\t" + dataPoint.getY());
    }

    System.err.println();

    TimeSeries series2 = new TimeSeries("Series 2", FixedMillisecond.class);
    for (Point<Number, Number> dataPoint : extrapolatedList2) {
      if ("2".equals(option) || "0".equals(option)) {
        series2.add(new FixedMillisecond(dataPoint.getX().longValue()), dataPoint.getY());
      }
      System.err.println(dataPoint.getX() + "\t" + dataPoint.getY());
    }

    TimeSeriesCollection dataset = new TimeSeriesCollection();
    if ("1".equals(option)) {
      dataset.addSeries(series1);
    }
    else if ("2".equals(option)) {
      dataset.addSeries(series2);
    }
    else if ("0".equals(option)) {
      dataset.addSeries(series1);
      dataset.addSeries(series2);
    }

    JFreeChart chart =
        ChartFactory.createXYLineChart("Test", null, null, dataset, PlotOrientation.VERTICAL,
            false, true, false);

    XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) chart.getXYPlot().getRenderer();
    renderer.setSeriesShapesVisible(0, true);
    renderer.setSeriesShapesVisible(1, true);

    ChartPanel chartPanel = new ChartPanel(chart);
    chartPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

    JFrame frame = new JFrame();
    frame.getContentPane().add(chartPanel);
    frame.pack();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }

}
