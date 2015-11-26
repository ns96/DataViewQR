/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instras.dataviewqr.utils;

import com.codename1.io.Log;
import com.codename1.io.Util;
import com.instras.dataviewqr.model.XYData;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 *
 * @author nathan
 */
public class XYDataParser {

    private String delim = "\t";

    public XYDataParser() {
    }

    public XYDataParser(String delim) {
        this.delim = delim;
    }

    /**
     * Method to return an multi-dimensional array
     *
     * @param content
     * @return
     */
    public String[][] parse(String content) {
        ArrayList<String[]> dataList = new ArrayList<String[]>();

        String lineDelim = "\n";
        if (content.indexOf("\r\n") != -1) {
            lineDelim = "\r\n";
        }

        String[] lines = Util.split(content, lineDelim);

        for (String line : lines) {
            if (line.trim().length() != 0) {
                String[] sa = Util.split(line, delim);
                dataList.add(sa);
            }
        }

        int length = dataList.get(0).length;
        String[][] xydata = new String[dataList.size()][length];

        for (int i = 0; i < dataList.size(); i++) {
            xydata[i] = dataList.get(i);
        }

        return xydata;
    }

    /**
     * InputStream the input stream
     *
     * @param is
     * @return
     */
    public String[][] parse(InputStream is) {
        try {
            String content = Util.readToString(is);
            return parse(content);
        } catch (IOException ex) {
            Log.e(ex);
            return null;
        }
    }

    /**
     * Method to merge the xy data in the list into a single XY data. It assumes
     * that the header is only in the first xydata object and the rest only
     * contain data
     *
     * @param scanName
     * @param mergeList
     * @return
     */
    public XYData mergeXYData(String scanName, ArrayList<XYData> mergeList) {
        if (mergeList == null || mergeList.isEmpty()) {
            return null;
        }

        StringBuilder sb = new StringBuilder();

        for (XYData xyData : mergeList) {
            String[][] xydata = xyData.getXYdata();
            for (int i = 0; i < xydata.length; i++) {
                sb.append(xydata[i][0]).append("\t").append(xydata[i][1]).append("\n");
            }
        }

        // now get the meerge xydata
        XYData xyData = new XYData();
        xyData.setScanName(scanName);
        xyData.setXYdata(parse(sb.toString()));
        
        return xyData;
    }
}
