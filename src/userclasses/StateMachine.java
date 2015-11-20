/**
 * Your application code goes here
 */
package userclasses;

import com.codename1.codescan.CodeScanner;
import com.codename1.codescan.ScanResult;
import generated.StateMachineBase;
import com.codename1.ui.*;
import com.codename1.ui.events.*;
import com.codename1.ui.list.DefaultListModel;
import com.codename1.ui.list.MultiList;
import com.codename1.ui.util.Resources;
import com.instras.dataviewqr.AcidDissociationChart;
import com.instras.dataviewqr.DataViewChart;
import com.instras.dataviewqr.TitrationChart;
import com.instras.dataviewqr.XYDataChart;
import com.instras.dataviewqr.model.XYData;
import com.instras.dataviewqr.utils.XYDataParser;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Your name here
 */
public class StateMachine extends StateMachineBase {
    
    private LinkedHashMap<String, XYData> historyList;
    private XYDataParser parser;

    public StateMachine(String resFile) {
        super(resFile);
        // do not modify, write code in initVars and initialize class members there,
        // the constructor might be invoked too late due to race conditions that might occur
    }

    /**
     * this method should be used to initialize variables instead of the
     * constructor/class scope to avoid race conditions
     */
    @Override
    protected void initVars(Resources res) {
        historyList = new LinkedHashMap<String, XYData>();
        parser = new XYDataParser();
    }

    @Override
    protected void onMain_MultiListAction(Component c, ActionEvent event) {
        MultiList cmp = (MultiList) c;
        Map<String, Object> item = (Map<String, Object>) (cmp.getSelectedItem());
        String scanName = item.get("Line1").toString();
        displayChart(scanName, null);
    }
    
    /**
     * Get the proper chart to display
     * @param scanName
     * @param xyData 
     */
    private void displayChart(String scanName, XYData xyData) {
        if (scanName.equals("Scan XY Data")) {
            XYDataChart xydataChart = new XYDataChart();
            getXYDataFromQRCode(scanName, "xydata.tsv", xydataChart, xyData);
        } else if (scanName.equals("Scan Titration Data")) {
            TitrationChart titrationChart = new TitrationChart();
            getXYDataFromQRCode(scanName, "titration.tsv", titrationChart, xyData);
        } else if (scanName.equals("Scan Acid Dissociation Data")) {
            AcidDissociationChart acidDissociationChart = new AcidDissociationChart();
            getXYDataFromQRCode(scanName, "acid_dissociation.tsv", acidDissociationChart, xyData);
        } else if (scanName.equals("History")) {
            showForm("HistoryForm", null);
        } else if (scanName.equals("Exit")) {
            Display.getInstance().exitApplication();
        } else {
            System.out.println("Unknown scan type: " + scanName);
        }
    }

    /**
     * Method to return the XYData object for plotting
     *
     * @return
     */
    private void getXYDataFromQRCode(final String scanName, String sampleData, final DataViewChart dataViewChart, XYData storedData) {
        // setup the back command
        final Form mainForm = Display.getInstance().getCurrent();
        final Command back = new Command("Back") {
            @Override
            public void actionPerformed(ActionEvent ev) {
                mainForm.showBack();
            }
        };

        if(storedData != null) {
            Form form = dataViewChart.getChart(storedData);
            form.setBackCommand(back);
            form.show();
        } else if (CodeScanner.getInstance() != null) {
            CodeScanner.getInstance().scanQRCode(new ScanResult() {
                public void scanCompleted(String contents, String formatName, byte[] rawBytes) {
                    XYData xyData = new XYData();
                    xyData.setScanName(scanName);
                    xyData.setXYdata(parser.parse(contents));
                    historyList.put(xyData.getDate().toString(), xyData);

                    Form form = dataViewChart.getChart(xyData);
                    form.setBackCommand(back);
                    form.show();
                }

                public void scanCanceled() {
                    Dialog.show("QRData Csncelled", " Scan canceled", "OK", null);
                }

                public void scanError(int errorCode, String message) {
                    Dialog.show("QRData Error", message, "OK", null);
                }
            });
        } else {
            // this else is for testing in the desktop environmments
            System.out.println("No QRCode Scanner .. loading dummy data");
            InputStream inputStream = fetchResourceFile().getData(sampleData);
            
            XYData xyData = new XYData();
            xyData.setScanName(scanName);
            xyData.setXYdata(parser.parse(inputStream));
            historyList.put(xyData.getDate().toString(), xyData);
            
            Form form = dataViewChart.getChart(xyData);
            form.setBackCommand(back);
            form.show();
        }
    }
    
    /**
     * Override the base method to allow the loading stored data in the history list
     * @param form 
     */
    @Override
    protected void beforeShow(Form form) {
        super.beforeShow(form);

        if (form.getName().equals("HistoryForm")) {
            MultiList multiList = (MultiList) findHistoryMultiList(form);
            DefaultListModel listModel = new DefaultListModel();
            
            // get the default list model
            HashMap<String, String> map;
            
            for(XYData xyData: historyList.values()) {
                map = new HashMap<String, String>();
                map.put("Line1", xyData.getScanName());
                map.put("Line2", xyData.getDate().toString());   
                listModel.addItem(map);
            }
            
            multiList.setModel(listModel);
        }
    }
    
    /**
     * Display the data stored in the history
     * @param c
     * @param event 
     */
    @Override
    protected void onHistoryForm_HistoryMultiListAction(Component c, ActionEvent event) {
        MultiList cmp = (MultiList) c;
        Map<String, Object> item = (Map<String, Object>) (cmp.getSelectedItem());
        String scanName = item.get("Line1").toString();
        String dateTime = item.get("Line2").toString();
        
        XYData xyData = historyList.get(dateTime);
        displayChart(scanName, xyData);
    }
}
