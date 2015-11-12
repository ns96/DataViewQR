/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instras.dataviewqr.utils;

import com.codename1.io.Log;
import com.codename1.io.Util;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 *
 * @author nathan
 */
public class XYDataParser {
    private String delim = "\t";
    
    public XYDataParser() {}
    
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
        if(content.indexOf("\r\n") != -1) {
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
}
