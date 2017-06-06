package com.junjie.indoorj.database.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Nova on 2017/6/6.
 * cnhjj2008@gmail.com
 */
@DatabaseTable(tableName = "rssi")
public class RssiBean {
    public static final String COLUMNNAME_ID = "id";
    public static final String COLUMNNAME_RSSI1 = "rssi1";
    public static final String COLUMNNAME_RSSI2 = "rssi2";
    public static final String COLUMNNAME_RSSI3 = "rssi3";
    public static final String COLUMNNAME_RSSI4 = "rssi4";
    public static final String COLUMNNAME_RSSI5 = "rssi5";
    public static final String COLUMNNAME_RSSI6 = "rssi6";
    public static final String COLUMNNAME_X = "x";
    public static final String COLUMNNAME_Y = "y";

    @DatabaseField(generatedId = true, columnName = COLUMNNAME_ID, useGetSet = true)
    private int id;
    @DatabaseField(columnName = COLUMNNAME_RSSI1,useGetSet = true,canBeNull = false,defaultValue = "0")
    private int rssi1;
    @DatabaseField(columnName = COLUMNNAME_RSSI2,useGetSet = true,canBeNull = false,defaultValue = "0")
    private int rssi2;
    @DatabaseField(columnName = COLUMNNAME_RSSI3,useGetSet = true,canBeNull = false,defaultValue = "0")
    private int rssi3;
    @DatabaseField(columnName = COLUMNNAME_RSSI4,useGetSet = true,canBeNull = false,defaultValue = "0")
    private int rssi4;
    @DatabaseField(columnName = COLUMNNAME_RSSI5,useGetSet = true,canBeNull = false,defaultValue = "0")
    private int rssi5;
    @DatabaseField(columnName = COLUMNNAME_RSSI6,useGetSet = true,canBeNull = false,defaultValue = "0")
    private int rssi6;
    @DatabaseField(columnName = COLUMNNAME_X,useGetSet = true)
    private double x;
    @DatabaseField(columnName = COLUMNNAME_Y,useGetSet = true)
    private double y;

    public RssiBean() {
    }

    public RssiBean( int rssi1, int rssi2, int rssi3, int rssi4, int rssi5, int rssi6, double x, double y) {
        this.rssi1 = rssi1;
        this.rssi2 = rssi2;
        this.rssi3 = rssi3;
        this.rssi4 = rssi4;
        this.rssi5 = rssi5;
        this.rssi6 = rssi6;
        this.x = x;
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRssi1() {
        return rssi1;
    }

    public void setRssi1(int rssi1) {
        this.rssi1 = rssi1;
    }

    public int getRssi2() {
        return rssi2;
    }

    public void setRssi2(int rssi2) {
        this.rssi2 = rssi2;
    }

    public int getRssi3() {
        return rssi3;
    }

    public void setRssi3(int rssi3) {
        this.rssi3 = rssi3;
    }

    public int getRssi4() {
        return rssi4;
    }

    public void setRssi4(int rssi4) {
        this.rssi4 = rssi4;
    }

    public int getRssi5() {
        return rssi5;
    }

    public void setRssi5(int rssi5) {
        this.rssi5 = rssi5;
    }

    public int getRssi6() {
        return rssi6;
    }

    public void setRssi6(int rssi6) {
        this.rssi6 = rssi6;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
