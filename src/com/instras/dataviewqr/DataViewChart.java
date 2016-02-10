/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instras.dataviewqr;

import com.codename1.charts.models.CategorySeries;
import com.codename1.charts.models.XYMultipleSeriesDataset;
import com.codename1.charts.models.XYSeries;
import com.codename1.charts.renderers.XYMultipleSeriesRenderer;
import com.codename1.charts.renderers.XYSeriesRenderer;
import com.codename1.charts.util.ColorUtil;
import com.codename1.charts.views.PointStyle;
import com.codename1.io.Util;
import com.codename1.l10n.L10NManager;
import com.codename1.ui.Component;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Tabs;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.util.MathUtil;
import com.instras.dataviewqr.model.XYData;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the base class for all XY data
 *
 * @author nathan
 */
public abstract class DataViewChart {

    protected L10NManager formatter = Display.getInstance().getLocalizationManager();

    protected XYMultipleSeriesDataset buildDataset(String[] titles, List<double[]> xyValues) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

        XYSeries series = getXYSeries(titles[0], xyValues.get(0), xyValues.get(1));
        dataset.addSeries(series);

        // we may have trend line data as well
        if (titles.length == 2 && xyValues.size() == 3) {
            series = getXYSeries(titles[1], xyValues.get(0), xyValues.get(2));
            dataset.addSeries(series);
        }

        return dataset;
    }

    /**
     * Method to return an xy series for the line chart
     *
     * @param title
     * @param xV
     * @param yV
     * @return
     */
    protected XYSeries getXYSeries(String title, double[] xV, double[] yV) {
        XYSeries series = new XYSeries(title);

        int seriesLength = xV.length;
        for (int i = 0; i < seriesLength; i++) {
            series.add(xV[i], yV[i]);
        }

        return series;
    }

    /**
     * Builds an XY multiple series renderer.
     *
     * @param colors the series rendering color
     * @param styles the series point style
     * @return the XY multiple series renderer
     */
    protected XYMultipleSeriesRenderer buildRenderer(int[] colors, PointStyle[] styles) {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        renderer.setAxisTitleTextSize(25);
        renderer.setChartTitleTextSize(25);
        renderer.setLabelsTextSize(25);
        renderer.setLegendTextSize(25);
        renderer.setPointSize(10f);
        
        renderer.setBackgroundColor(ColorUtil.BLACK);
        renderer.setApplyBackgroundColor(true);
        
        renderer.setMargins(new int[]{20, 30, 15, 20});

        for (int i = 0; i < colors.length; i++) {
            renderer.addSeriesRenderer(getXYSeriesRenderer(colors[i], styles[i]));
        }

        return renderer;
    }

    /**
     * Get the XYSeriesRenderer
     *
     * @param color
     * @param style
     * @return
     */
    protected XYSeriesRenderer getXYSeriesRenderer(int color, PointStyle style) {
        XYSeriesRenderer renderer = new XYSeriesRenderer();
        renderer.setColor(color);
        renderer.setPointStyle(style);
        renderer.setFillPoints(true);
        renderer.setLineWidth(8f);
        
        return renderer;
    }

    /**
     * Builds a bar multiple series renderer to use the provided colors.
     *
     * @param colors the series renderers colors
     * @return the bar multiple series renderer
     */
    protected XYMultipleSeriesRenderer buildBarRenderer(int[] colors) {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        renderer.setAxisTitleTextSize(25);
        renderer.setChartTitleTextSize(25);
        renderer.setLabelsTextSize(25);
        renderer.setLegendTextSize(25);
        
        renderer.setBackgroundColor(ColorUtil.BLACK);
        renderer.setApplyBackgroundColor(true);

        int length = colors.length;

        for (int i = 0; i < length; i++) {
            XYSeriesRenderer r = new XYSeriesRenderer();
            r.setColor(colors[i]);
            renderer.addSeriesRenderer(r);
        }

        return renderer;
    }

    /**
     * Builds a bar multiple series dataset using the provided values.
     *
     * @param titles the series titles
     * @param values the values
     * @return the XY multiple bar dataset
     */
    protected XYMultipleSeriesDataset buildBarDataset(String[] titles, List<double[]> values) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        int length = titles.length;
        for (int i = 0; i < length; i++) {
            CategorySeries series = new CategorySeries(titles[i]);
            double[] v = values.get(i);
            int seriesLength = v.length;
            for (int k = 0; k < seriesLength; k++) {
                series.add(v[k]);
            }
            dataset.addSeries(series.toXYSeries());
        }
        return dataset;
    }

    /**
     * Sets a few of the series renderer settings.
     *
     * @param renderer the renderer to set the properties to
     * @param title the chart title
     * @param xTitle the title for the X axis
     * @param yTitle the title for the Y axis
     * @param xMin the minimum value on the X axis
     * @param xMax the maximum value on the X axis
     * @param yMin the minimum value on the Y axis
     * @param yMax the maximum value on the Y axis
     * @param axesColor the axes color
     * @param labelsColor the labels color
     */
    protected void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle,
            String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor,
            int labelsColor) {

        renderer.setChartTitle(title);
        renderer.setXTitle(xTitle);
        renderer.setYTitle(yTitle);
        renderer.setXAxisMin(xMin);
        renderer.setXAxisMax(xMax);
        renderer.setYAxisMin(yMin);
        renderer.setYAxisMax(yMax);
        renderer.setAxesColor(axesColor);
        renderer.setLabelsColor(labelsColor);
    }

    /**
     * Method to return the data as a list in which position 0 are the x values
     * and position 1 are the y values
     *
     * @param xydata
     * @return
     */
    protected List<double[]> getDataAsList(String[][] xydata) {
        ArrayList<double[]> dataList = new ArrayList<double[]>();

        int length = xydata.length - 1;

        double[] xdata = new double[length];
        double[] ydata = new double[length];

        // lets assume we have a header information in position 0
        for (int i = 1; i <= length; i++) {
            xdata[i - 1] = Double.parseDouble(xydata[i][0]);
            ydata[i - 1] = Double.parseDouble(xydata[i][1]);
        }

        dataList.add(xdata);
        dataList.add(ydata);

        return dataList;
    }

    /**
     * Method to get the x and y min and max
     *
     * @param xdata
     * @param ydata
     * @return
     */
    protected double[] getXYLimits(double[] xdata, double[] ydata) {
        double xmin = 1000000;
        double xmax = -1000000;
        double ymin = 1000000;
        double ymax = -1000000;

        for (int i = 0; i < xdata.length; i++) {
            if (xdata[i] < xmin) {
                xmin = xdata[i];
            }

            if (ydata[i] < ymin) {
                ymin = ydata[i];
            }

            if (xdata[i] > xmax) {
                xmax = xdata[i];
            }

            if (ydata[i] > ymax) {
                ymax = ydata[i];
            }
        }

        double[] limits = {xmin, xmax, ymin, ymax};

        return limits;
    }

    /**
     * This formats a double which would be displayed as scientific values
     *
     * @param value
     * @param dp the decimal places
     * @return
     */
    protected String formatScientificNumber(double value, int dp) {
        // first get the scientific notation format
        String sn = Double.toString(value);

        // now split into two parts
        String[] sa = Util.split(sn, "E");

        String part1 = "" + round(Double.parseDouble(sa[0]), dp);

        String part2 = "";
        if (sa.length == 2) {
            part2 = "E" + sa[1];
        }

        return part1 + part2;
    }

    /**
     * This is used to round a double to certain number of decimal places
     *
     * @param valueToRound
     * @param numberOfDecimalPlaces
     * @return
     */
    protected double round(double valueToRound, int numberOfDecimalPlaces) {
        double multipicationFactor = MathUtil.pow(10, numberOfDecimalPlaces);
        double interestedInZeroDPs = valueToRound * multipicationFactor;
        return Math.round(interestedInZeroDPs) / multipicationFactor;
    }

    /**
     * Method to return a form which will contain the chart or multiple charts
     * in a tab
     *
     * @param title
     * @param c
     * @return
     */
    protected Form wrap(String title, Component c) {
        Form f = new Form(title);
        f.setLayout(new BorderLayout());
        f.addComponent(BorderLayout.CENTER, c);
        return f;
    }

    /**
     * Method to take an array of chart components, place them in a tab then
     * return a form that wraps everything up
     *
     * @param formTitle The title of the form
     * @param titles contains the titles of the tabs
     * @param components the tab components
     * @return Form containing the tab
     */
    protected Form wrap(String formTitle, String[] titles, Component[] components) {
        Tabs tabs = new Tabs();

        for (int i = 0; i < titles.length; i++) {
            tabs.addTab(titles[i], components[i]);
        }

        return wrap(formTitle, tabs);
    }

    public abstract Form getChart(XYData xyData);
}
