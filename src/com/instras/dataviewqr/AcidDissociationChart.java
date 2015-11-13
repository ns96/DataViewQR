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
import com.codename1.ui.table.DefaultTableModel;
import com.codename1.ui.table.Table;
import com.codename1.util.MathUtil;
import com.instras.dataviewqr.model.XYData;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nathan
 */
public class AcidDissociationChart extends DataViewChart {
    
    private final String title = "Weak Acid Dissociation Data";
    
    public AcidDissociationChart() {
        
    }
    
    /**
     * This generate the chart
     *
     * @param xyData
     * @return
     */
    @Override
    public Form getChart(XYData xyData) {
        List<double[]> dataList = getDataAsList(xyData.getXYdata());
        
        Component[] tabs = new Component[2];
        String[] tabTitles = {"[HA]/pH", "Results"};
        
        tabs[0] = getConcentrationVsPhChart(dataList);
        tabs[1] = getResultsTab(dataList);
        
        return wrap(title, tabTitles, tabs);
    }
    
    /**
     * Get the chart of the pH vs Volume
     * 
     * @param dataList
     * @return 
     */
    private Component getConcentrationVsPhChart(List<double[]> dataList) {
        double[] limits  = getXYLimits(dataList.get(0), dataList.get(1));
        
        // construct the chart renderer
        XYMultipleSeriesRenderer renderer = buildRenderer(new int[]{ColorUtil.BLUE}, new PointStyle[]{PointStyle.CIRCLE});

        setChartSettings(renderer, "", "[HA]", "pH", 0, 1, (limits[2] - 0.1), (limits[3] + 0.1),
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

    private Component getResultsTab(List<double[]> dataList) {
        double[] xdata = dataList.get(0);
        double[] ydata = dataList.get(1);
        double[] zdata = dataList.get(2);
        
        String[] header = {"HA", "pH", "[H+]", "Ka", "%ion"};
        String[][] data = new String[xdata.length + 3][header.length];
        
        double totalKa = 0;
        int i;
        for(i= 0; i < xdata.length; i++) {
            data[i][0] = Double.toString(xdata[i]) + "M";
            data[i][1] = Double.toString(ydata[i]);
            
            double concH = MathUtil.pow(10, -1*Double.parseDouble(data[i][1]));
            String fdata = formatScientificNumber(concH, 3);
            data[i][2] = " " + fdata + " ";
            
            double ka = (concH*concH)/xdata[i];
            fdata = formatScientificNumber(ka, 3);
            data[i][3] = fdata;
            
            totalKa += ka;
            
            double perIon = (concH/xdata[i])*100;
            fdata = formatter.format(perIon, 3);
            data[i][4] = fdata;
        }
        
        // get the average ka
        double avgKa = totalKa/xdata.length;
        String fdata = formatScientificNumber(avgKa, 3);
        data[i][0] = "Avg";
        data[i][3] = fdata;
        
        // add a psace
        i++;
        data[i][0] = " ";
        
        // caculate the concentration of Actic acid based on measure pH
        i++;
        double pH = zdata[0];
        double concH = MathUtil.pow(10, -1*pH);
        double concHA = (concH*concH)/avgKa;
        
        data[i][0] = " Vinegar ";
        data[i][1] = Double.toString(pH);
        data[i][2] = formatScientificNumber(concH, 3);
        data[i][3] = " Molarity ";
        data[i][4] = formatter.format(concHA, 3) + "M";
        
        DefaultTableModel tableModel = new DefaultTableModel(header, data);
        
        Table table = new Table(tableModel);
        table.setScrollableX(true);
        table.setScrollableY(true);
        table.setBorderSpacing(10, 10);
        
        return table;
    }
    
    /**
     * Method to return the data as a list in which position 0 are the x values
     * and position 1 are the y values
     *
     * @param xydata
     * @return
     */
    @Override
    protected List<double[]> getDataAsList(String[][] xydata) {
        ArrayList<double[]> dataList = new ArrayList<double[]>();
        
        int length = xydata.length - 2;

        double[] xdata = new double[length];
        double[] ydata = new double[length];
        
        // lets assume we have a header information in position 0
        int i;
        for (i = 1; i <= length; i++) {
            xdata[i - 1] = Double.parseDouble(xydata[i][0]);
            ydata[i - 1] = Double.parseDouble(xydata[i][1]);
        }

        dataList.add(xdata);
        dataList.add(ydata);

        // add the data which holds the pH measurement of vinegar
        double[] zdata = new double[1];
        zdata[0] = Double.parseDouble(xydata[i++][1]);
        dataList.add(zdata);
        
        return dataList;
    }
}
