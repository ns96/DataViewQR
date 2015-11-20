/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instras.dataviewqr.model;

import java.util.Date;

/**
 *
 * @author nathan
 */
public class XYData {

    private String type;
    private String description;
    private String[][] xydata;
    private String scanName;
    private Date date;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[][] getXYdata() {
        return xydata;
    }

    public void setXYdata(String[][] xydata) {
        this.xydata = xydata;
        date = new Date();
    }

    public int length() {
        return xydata.length;
    }

    public String getScanName() {
        return scanName;
    }

    public void setScanName(String scanName) {
        this.scanName = scanName;
    }

    public Date getDate() {
        return date;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Data Length: ").append(xydata.length).append("\n");

        for (int i = 0; i < xydata.length; i++) {
            sb.append(xydata[i][0]).append(" : ").append(xydata[i][1]).append("\n");
        }

        return sb.toString();
    }
}
