/*
 * A class to plot titration data and make finding the equivalence point easier 
 **/
package com.instras.dataviewqr;

import com.codename1.charts.ChartComponent;
import com.codename1.charts.renderers.XYMultipleSeriesRenderer;
import com.codename1.charts.renderers.XYSeriesRenderer;
import com.codename1.charts.util.ColorUtil;
import com.codename1.charts.views.BarChart;
import com.codename1.ui.Component;
import com.codename1.ui.Form;
import com.codename1.ui.table.DefaultTableModel;
import com.codename1.ui.table.Table;
import com.instras.dataviewqr.model.XYData;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nathan
 */
public class SolubilityChart extends DataViewChart {
    
    private final String title = "Solubility Of Salts";

    private double maxSolubilty = 0;  // The maximum solubility used for ploting data
    
    public SolubilityChart() {

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
        String[] tabTitles = {"Solubility", "Results"};

        String[][] xydata = xyData.getXYdata();
        tabs[0] = getBarChart(xydata);
        tabs[1] = getResultsTab(xydata);

        return wrap(title, tabTitles, tabs);
    }

    /**
     * Get the chart of the pH vs Volume
     *
     * @param dataList
     * @return
     */
    private Component getBarChart(String[][] xydata) {
        String[] saltNames = getSaltNames(xydata);
        List<double[]> dataList = getSolubilityList(xydata);
        
        double xmin = 0.99;
        double xmax = 1.01;
        double ymin = 0;
        double ymax = maxSolubilty + 5;

        // construct the chart renderer
        int[] colors = new int[]{ColorUtil.CYAN, ColorUtil.BLUE, ColorUtil.GREEN, ColorUtil.MAGENTA, ColorUtil.YELLOW};
        XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
        //renderer.setOrientation(XYMultipleSeriesRenderer.Orientation.VERTICAL);

        setChartSettings(renderer, "", "", "g/100", xmin, xmax, ymin, ymax,
                ColorUtil.LTGRAY, ColorUtil.LTGRAY);

        renderer.setXLabels(1);
        renderer.setYLabels(4);
        renderer.addXTextLabel(1, "Salts");
        renderer.setBarWidth(100f);

        int length = renderer.getSeriesRendererCount();
        for (int i = 0; i < length; i++) {
            XYSeriesRenderer seriesRenderer = (XYSeriesRenderer) renderer.getSeriesRendererAt(i);
            seriesRenderer.setDisplayChartValues(true);
        }

        return getChartComponent(renderer, saltNames, dataList);
    }

    /**
     * Method to return the chart component
     *
     * @param dataList 
     * @return
     */
    private Component getChartComponent(XYMultipleSeriesRenderer renderer, String[] saltNames, List<double[]> dataList) {
        BarChart chart = new BarChart(buildBarDataset(saltNames, dataList), 
                renderer, BarChart.Type.DEFAULT);
        
        ChartComponent chartComponent = new ChartComponent(chart);

        return chartComponent;
    }

    /**
     * Return the tab which hold the raw data
     *
     * @param xydata
     * @return
     */
    private Component getResultsTab(String[][] xydata) {
        String[] header = new String[xydata[0].length];
        String[][] data = new String[xydata.length*2 + 2][header.length];
        
        // first diplay the data that was read in
        int i;
        for (i = 0; i < header.length; i++) {
            header[i] = xydata[0][i];
        }
        
        // load the data now
        for (i = 1; i < xydata.length; i++) {
            for(int j = 0; j < header.length; j++) {
                data[i-1][j] = xydata[i][j];
            }
        }
        
        // add a space
        i++;
        data[i][0] = " ";
        
        // add the header and calculations for the solubility 
        i++;
        data[i][0] = "Salt";
        data[i][1] = "Sol (g/100ml)";
        data[i][2] = "M (g/mol)";
        data[i][3] = "T (C)";
        
        for(int j = 1; j < xydata.length; j++) {
            i++;
            data[i][0] = xydata[j][0];
            
            // calculate the solubility in grams/100 ml
            double gp100 = (Double.parseDouble(xydata[j][1])*100)/Double.parseDouble(xydata[j][2]);
            data[i][1] = "" + round(gp100, 2);
            
            // calculate the molarity using the measured density
            // must catch number format exception since the salt might not
            // be soluble
            try {
                double density = Double.parseDouble(xydata[j][4]);
                double fw = Double.parseDouble(xydata[j][5]);
                double molarity = (gp100/(gp100 + 100))*density*1000*(1/fw);
                
                data[i][2] = formatScientificNumber(molarity, 2);
            } catch(NumberFormatException nfe) {
                data[i][2] = xydata[j][4];
            }
            
            // get the temparature
            data[i][3] = xydata[j][3];
        }
      
        
        // now create the table
        DefaultTableModel tableModel = new DefaultTableModel(header, data);

        Table table = new Table(tableModel);
        table.setScrollableX(true);
        table.setScrollableY(true);
        table.setBorderSpacing(10, 10);

        return table;
    }

    /**
     * Method to get solubility data for the salts
     *
     * @param xydata
     * @return
     */
    private List<double[]> getSolubilityList(String[][] xydata) {
        List<double[]> dataList = new ArrayList<double[]>();

        // get the solubilty data in grams/per 100 ml. We need to skip the header
        for(int i = 1; i < xydata.length; i++) {
            double ms = Double.parseDouble(xydata[i][1]); // mass of salt
            double mw = Double.parseDouble(xydata[i][2]); // mass of water
            
            double sp100 = round((ms*100)/mw, 2); // solubility per 100 g H2O

            dataList.add(new double[]{sp100});
            
            // set the max solubility of needed
            if(sp100 > maxSolubilty) {
                maxSolubilty = sp100;
            }
        }
        
        return dataList;
    }
        
    /**
     * Method to get the name of the slats
     * 
     * @param xydata
     * @return 
     */
    private String[] getSaltNames(String[][] xydata) {
        String[] saltNames = new String[xydata.length - 1];
        
        for(int i = 1; i < xydata.length; i++) {
            saltNames[i-1] = xydata[i][0];
        }
        
        return saltNames;
    }
}
