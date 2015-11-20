/*
 * A class to plot titration data and make finding the equivalence point easier 
 **/
package com.instras.dataviewqr;

import com.codename1.charts.ChartComponent;
import com.codename1.charts.renderers.XYMultipleSeriesRenderer;
import com.codename1.charts.util.ColorUtil;
import com.codename1.charts.views.PointStyle;
import com.codename1.charts.views.ScatterChart;
import com.codename1.ui.Component;
import com.codename1.ui.Form;
import com.codename1.ui.table.DefaultTableModel;
import com.codename1.ui.table.Table;
import com.instras.dataviewqr.model.XYData;
import java.util.List;

/**
 *
 * @author nathan
 */
public class XYDataChart extends DataViewChart {
    
    private final String title = "Generic XY Data";
    
    public XYDataChart() {
        
    }
    
    /**
     * This generate the chart
     *
     * @param xyData
     * @return
     */
    @Override
    public Form getChart(XYData xyData) {
        Component[] tabs = new Component[2];
        String[] tabTitles = {"X/Y Plot", "Data"};
        
        String[][] xydata = xyData.getXYdata();
        tabs[0] = getXYChart(xydata);
        tabs[1] = getDataTab(xydata);
        
        return wrap(title, tabTitles, tabs);
    }
    
    /**
     * Get the chart of the pH vs Volume
     * 
     * @param dataList
     * @return 
     */
    private Component getXYChart(String[][] xydata) {
        List<double[]> dataList = getDataAsList(xydata);
        double[] limits  = getXYLimits(dataList.get(0), dataList.get(1));
        double xmin = limits[0] - 0.1*limits[0];
        double xmax = limits[1] + 0.1*limits[1];
        double ymin = limits[2] - 0.1*limits[2];
        double ymax = limits[3] + 0.1*limits[3];
        
        // construct the chart renderer
        XYMultipleSeriesRenderer renderer = buildRenderer(new int[]{ColorUtil.BLUE}, new PointStyle[]{PointStyle.SQUARE});
        
        setChartSettings(renderer, "", xydata[0][0], xydata[0][1], xmin, xmax, ymin, ymax,
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

        ScatterChart chart = new ScatterChart(buildDataset(new String[]{"    data"}, dataList), renderer);
        
        ChartComponent chartComponent = new ChartComponent(chart);
        
        return chartComponent;
    }
    
    /**
     * Return the tab which hold the raw data
     * @param xydata
     * @return 
     */
    private Component getDataTab(String[][] xydata) {
        String[] header = {xydata[0][0], xydata[0][1]};
        String[][] data = new String[xydata.length -1][header.length];
        
        for(int i= 0; i < data.length; i++) {
            data[i][0] = xydata[i+1][0];
            data[i][1] = xydata[i+1][1];
        }
        
        DefaultTableModel tableModel = new DefaultTableModel(header, data);
        
        Table table = new Table(tableModel);
        table.setScrollableX(true);
        table.setScrollableY(true);
        table.setBorderSpacing(10, 10);
        
        return table;
    }
}
