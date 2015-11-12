/**
 * Your application code goes here
 */
package userclasses;

import com.codename1.codescan.CodeScanner;
import com.codename1.codescan.ScanResult;
import com.codename1.io.Log;
import generated.StateMachineBase;
import com.codename1.ui.*;
import com.codename1.ui.events.*;
import com.codename1.ui.list.MultiList;
import com.codename1.ui.util.Resources;
import com.instras.dataviewqr.DataViewChart;
import com.instras.dataviewqr.TitrationChart;
import com.instras.dataviewqr.model.XYData;
import com.instras.dataviewqr.utils.XYDataParser;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author Your name here
 */
public class StateMachine extends StateMachineBase {
    private final ArrayList<XYData> historyList = new ArrayList<XYData>();
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
        parser = new XYDataParser();
    }

    @Override
    protected void onMain_MultiListAction(Component c, ActionEvent event) {
        MultiList cmp = (MultiList) c;
        Map<String, Object> item = (Map<String, Object>) (cmp.getSelectedItem());
        String itemName = item.get("Line1").toString().toLowerCase();

        if (itemName.equals("scan titration data")) {
            TitrationChart titrationChart = new TitrationChart();
            getXYDataFromQRScan("titration.tsv", titrationChart);
        } else if (itemName.equals("exit")) {
            Display.getInstance().exitApplication();
        } else {
            System.out.println("Unkown application ...");
        }
    }

    /**
     * Method to return the XYData object for plotting
     *
     * @return
     */
    private void getXYDataFromQRScan(String dataType, final DataViewChart dataViewChart) {
        // setup the back command
        final Form mainForm = Display.getInstance().getCurrent();
        final Command back = new Command("Back") {
            @Override
            public void actionPerformed(ActionEvent ev) {
                mainForm.showBack();
            }
        };
        
        // get object to sotre data
        final XYData xyData = new XYData();
        xyData.setType(dataType);

        if (CodeScanner.getInstance() != null) {
            CodeScanner.getInstance().scanQRCode(new ScanResult() {
                public void scanCompleted(String contents, String formatName, byte[] rawBytes) {
                    xyData.setXYdata(parser.parse(contents));
            
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
            InputStream inputStream = fetchResourceFile().getData(dataType);
            xyData.setXYdata(parser.parse(inputStream));
            
            Form form = dataViewChart.getChart(xyData);
            form.setBackCommand(back);
            form.show();
        }
    }
}
