/*
 * A class to plot titration data and make finding the equivalence point easier 
 **/
package com.instras.dataviewqr;

import com.codename1.charts.ChartComponent;
import com.codename1.charts.renderers.XYMultipleSeriesRenderer;
import com.codename1.charts.util.ColorUtil;
import com.codename1.charts.views.CubicLineChart;
import com.codename1.charts.views.PointStyle;
import com.codename1.ui.Component;
import com.codename1.ui.Form;
import com.codename1.util.MathUtil;
import com.instras.dataviewqr.model.XYData;
import com.instras.dataviewqr.ssj.functionfit.SmoothingCubicSpline;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nathan
 */
public class TitrationChart extends DataViewChart {

    private final String title = "Titration Data";

    /**
     * This generate the chart
     *
     * @param xyData
     * @return
     */
    @Override
    public Form getChart(XYData xyData) {
        List<double[]> dataList = getDataAsList(xyData.getXYdata());
        
        Component[] charts = new Component[3];
        String[] tabTitles = {"Vol/pH", "DV/DpH", "D(DV/DpH)"};
        
        // get the first and second derivatives
        List<double[]> derList = getDerivative(dataList.get(0), dataList.get(1), 1);
        List<double[]> sderList = getDerivative(dataList.get(0), dataList.get(1), 2);
        
        charts[0] = getVolumeVsPhChart(dataList);
        charts[1] = getVolumeVsPhFirstDerivative(derList);
        charts[2] = getVolumeVsPhSecondDerivative(sderList);
        
        return wrap(title, tabTitles, charts);
    }
    
    /**
     * Get the chart of the pH vs Volume
     * 
     * @param dataList
     * @return 
     */
    private Component getVolumeVsPhChart(List<double[]> dataList) {
        double[] limits  = getXYLimits(dataList.get(0), dataList.get(1));
        
        // construct the chart renderer
        XYMultipleSeriesRenderer renderer = buildRenderer(new int[]{ColorUtil.BLUE}, new PointStyle[]{PointStyle.CIRCLE});

        setChartSettings(renderer, "", "Volume (ml)", "pH", 0, (limits[1] + 1), 0, (limits[3] + 1),
                ColorUtil.LTGRAY, ColorUtil.LTGRAY);

        return getChartComponent(renderer, dataList);
    }

    private Component getVolumeVsPhFirstDerivative(List<double[]> dataList) {
        double[] limits  = getXYLimits(dataList.get(0), dataList.get(1));
        double ymin = limits[2] - Math.abs(0.1*limits[2]);
        double ymax = limits[3] + Math.abs(0.1*limits[3]);
        
        // construct the chart renderer
        XYMultipleSeriesRenderer renderer = buildRenderer(new int[]{ColorUtil.MAGENTA}, new PointStyle[]{PointStyle.CIRCLE});

        setChartSettings(renderer, "", "Average Volume (ml)", "avgV/(DpH/DV)", 0, (limits[1] + 1), ymin, ymax,
                ColorUtil.LTGRAY, ColorUtil.LTGRAY);
        
        return getChartComponent(renderer, dataList);
    }
    
    /**
     * Method to get the second derivative plot
     * 
     * @param dataList
     * @return 
     */
    private Component getVolumeVsPhSecondDerivative(List<double[]> dataList) {
        double[] limits  = getXYLimits(dataList.get(0), dataList.get(1));
        double ymin = limits[2] - Math.abs(0.1*limits[2]);
        double ymax = limits[3] + Math.abs(0.1*limits[3]);
        
        // construct the chart renderer
        XYMultipleSeriesRenderer renderer = buildRenderer(new int[]{ColorUtil.CYAN}, new PointStyle[]{PointStyle.CIRCLE});

        setChartSettings(renderer, "", "Average Volume (ml)", "avgV/D(DpH/DV)", 0, (limits[1] + 1), ymin, ymax,
                ColorUtil.LTGRAY, ColorUtil.LTGRAY);
        
        return getChartComponent(renderer, dataList);
    }
    
    /**
     * Method to return the chart component
     * 
     * @param renderer
     * @return 
     */
    private Component getChartComponent(XYMultipleSeriesRenderer renderer, List<double[]> dataList) {
        renderer.setXLabels(12);
        renderer.setYLabels(10);
        renderer.setShowGrid(true);
        renderer.setXLabelsAlign(Component.RIGHT);
        renderer.setYLabelsAlign(Component.RIGHT);

        CubicLineChart chart = new CubicLineChart(buildDataset(new String[]{"data"}, dataList), renderer, 0.33f);
        
        ChartComponent chartComponent = new ChartComponent(chart);
        
        return chartComponent;
    }
    
    /**
     * Method to get the first derivative
     * 
     * @param dataList
     * @paran n the nth derivative
     * @return 
     */
    private List<double[]> getDerivative(double[] xdata, double[] ydata, int n) {
        SmoothingCubicSpline scs = new SmoothingCubicSpline(xdata, ydata, 0.8);
                
        int length = ydata.length;
        
        double[] dXdata = new double[length];
        double[] dYdata = new double[length];
        
        for(int i = 0; i < length; i++) {
            dXdata[i] = xdata[i];
            dYdata[i] = scs.derivative(xdata[i], n);
        }
        
        ArrayList<double[]> derList = new ArrayList<double[]>();
        derList.add(dXdata);
        derList.add(dYdata);
        
        return derList;
    } 

}
