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
public class MgOHFChart extends DataViewChart {

    private final String title = "MgO Heat Of Formation Data";

    public MgOHFChart() {

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
        String[] tabTitles = {"Temp Change", "Results"};

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
        List<double[]> dataList = getTemperatureList(xydata);
        double xmin = 0;
        double xmax = 3;
        double ymin = 0;
        double ymax = getMaxTemp(dataList);

        // construct the chart renderer
        int[] colors = new int[]{ColorUtil.CYAN, ColorUtil.BLUE};
        XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
        //renderer.setOrientation(XYMultipleSeriesRenderer.Orientation.VERTICAL);

        setChartSettings(renderer, "", "", "Temperature", xmin, xmax, ymin, ymax,
                ColorUtil.LTGRAY, ColorUtil.LTGRAY);

        renderer.setXLabels(2);
        renderer.setYLabels(4);
        renderer.addXTextLabel(1, "T1");
        renderer.addXTextLabel(2, "T2");

        int length = renderer.getSeriesRendererCount();
        for (int i = 0; i < length; i++) {
            XYSeriesRenderer seriesRenderer = (XYSeriesRenderer) renderer.getSeriesRendererAt(i);
            seriesRenderer.setDisplayChartValues(true);
        }

        return getChartComponent(renderer, dataList);
    }

    /**
     * Method to return the chart component
     *
     * @param dataList 
     * @return
     */
    private Component getChartComponent(XYMultipleSeriesRenderer renderer, List<double[]> dataList) {
        BarChart chart = new BarChart(buildBarDataset(new String[]{"Reation 1", "Reaction 2"}, dataList), 
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
        String[] header = {"Data", "Reation 1", "Reation 2"};
        String[][] data = new String[15][header.length];
        
        // first diplay the data that was read in
        String[] dataName = {"Vol HCl", "T2", "T1", "Mass Mg*"};
        int i;
        for (i = 0; i < dataName.length; i++) {
            data[i][0] = dataName[i];
            data[i][1] = xydata[i + 1][0];
            data[i][2] = xydata[i + 1][1];
        }
        
        // now display the calculated results
        data[i++][0] = " "; // add space
        
        // calculate the temp difference
        data[i][0] = "T2 - T1";
        double r1T = Double.parseDouble(xydata[2][0]) - Double.parseDouble(xydata[3][0]);
        data[i][1] = formatScientificNumber(r1T, 2);
        
        double r2T = Double.parseDouble(xydata[2][1]) - Double.parseDouble(xydata[3][1]);
        data[i][2] = formatScientificNumber(r2T, 2);        
        
        // calculate the q
        i++;
        data[i][0] = "Heat (q)";
        double r1Q = (4.18*r1T*(Double.parseDouble(xydata[1][0]) + Double.parseDouble(xydata[4][0])))/1000;
        data[i][1] = formatScientificNumber(r1Q, 2);
        
        double r2Q = (4.18*r2T*(Double.parseDouble(xydata[1][1]) + Double.parseDouble(xydata[4][1])))/1000;
        data[i][2] = formatScientificNumber(r2Q, 2);
        
        // calculate the delta H
        i++;
        data[i][0] = "delta H";
        double r1H = -1*r1Q;
        data[i][1] = formatScientificNumber(r1H, 2);
        
        double r2H = -1*r2Q;
        data[i][2] = formatScientificNumber(r2H, 2);
        
        // calculate the moles of solids
        i++;
        data[i][0] = "Moles Mg*";
        double r1M = Double.parseDouble(xydata[4][0])/40.3044; // Mg0
        data[i][1] = formatScientificNumber(r1M, 4);
        
        double r2M = Double.parseDouble(xydata[4][1])/24.305; // Mg solid
        data[i][2] = formatScientificNumber(r2M, 4);
        
        // calcuate the delta H devided by moles
        i++;
        data[i][0] = "KJ/mole";
        double r1HM = r1H/r1M; // Mg0
        data[i][1] = formatScientificNumber(r1HM, 2);
        
        double r2HM = r2H/r2M; // Mg solid
        data[i][2] = formatScientificNumber(r2HM, 2);
        
        /* calculate the heat of formation by combinind the three equations in the order below
        Mg(s) + ½ O2(g) -> MgO(s) (eq 4)
        
        MgCl2(aq) + H2O(l) -> MgO(s) + 2 HCl(aq) (r1 1)
        Mg(s) + 2 HCl(aq) -> MgCl2(aq) + H2(g)   (r2 2)
        H2(g) + ½ O2(g) -> H2O(l)                (r3 3)
        */
        i++;
        data[i][0] = " "; // add space
        
        i++;
        double dHF = -1*r1HM + r2HM + -285.8;
        data[i][0] = "delta H (exp)";
        data[i][1] = formatScientificNumber(dHF, 2);
        
        i++;
        double dHFA = -601.24; // NIST data 
        data[i][0] = "delta H (act)";
        data[i][1] = formatScientificNumber(dHFA, 2);
        
        // calculate the percent error
        i++;
        double pError = Math.abs(((dHF - dHFA)/dHFA)*100);
        data[i][0] = "% Error";
        data[i][1] = formatScientificNumber(pError, 2);
        
        // now create the table
        DefaultTableModel tableModel = new DefaultTableModel(header, data);

        Table table = new Table(tableModel);
        table.setScrollableX(true);
        table.setScrollableY(true);
        table.setBorderSpacing(10, 10);

        return table;
    }

    /**
     * Method to get the T2 and T1 temperatures for the 2 reactions
     *
     * @param xydata
     * @return
     */
    private List<double[]> getTemperatureList(String[][] xydata) {
        List<double[]> dataList = new ArrayList<double[]>();

        // add T1 and T2 data for reaction 1
        dataList.add(new double[]{Double.parseDouble(xydata[3][0]), Double.parseDouble(xydata[2][0])});

        // add T1 and T2 data for reaction 2
        dataList.add(new double[]{Double.parseDouble(xydata[3][1]), Double.parseDouble(xydata[2][1])});

        return dataList;
    }
    
    /**
     * Return the maximum temperature
     * 
     * @param dataList
     * @return 
     */
    private double getMaxTemp(List<double[]> dataList) {
        double[] r1data = dataList.get(0);
        double[] r2data = dataList.get(1);
        
        double maxTemp = Math.max(r1data[1], r2data[1]) + 2;
        
        return maxTemp;
    }
}
