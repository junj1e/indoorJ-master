package com.junjie.indoorj.database.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


/**
 * Created by Nova on 2017/6/6.
 * cnhjj2008@gmail.com
 */
/**
 * UserBean实体类，存储数据库中user表中的数据
 * <p>
 * 注解：
 * DatabaseTable：通过其中的tableName属性指定数据库名称
 * DatabaseField：代表数据表中的一个字段
 * ForeignCollectionField：一对多关联，表示一个UserBean关联着多个ArticleBean（必须使用ForeignCollection集合）
 * <p>
 * 属性：
 * id：当前字段是不是id字段（一个实体类中只能设置一个id字段）
 * columnName：表示当前属性在表中代表哪个字段
 * generatedId：设置属性值在数据表中的数据是否自增
 * useGetSet：是否使用Getter/Setter方法来访问这个字段
 * canBeNull：字段是否可以为空，默认值是true
 * unique：是否唯一
 * defaultValue：设置这个字段的默认值
 */

@DatabaseTable(tableName = "magnet") // 指定数据表的名称
public class MagnetBean {
    // 定义字段在数据库中的字段名
    public static final String COLUMNNAME_ID = "id";
    public static final String COLUMNNAME_XMAGNET = "xmagnet";
    public static final String COLUMNNAME_YMAGNET = "ymagnet";
    public static final String COLUMNNAME_ZMAGNET = "zmagnet";
    public static final String COLUMNNAME_X = "x";
    public static final String COLUMNNAME_Y = "y";

    @DatabaseField(generatedId = true, columnName = COLUMNNAME_ID, useGetSet = true)
    private int id;
    @DatabaseField(columnName = COLUMNNAME_XMAGNET,useGetSet = true,canBeNull = false)
    private double xmagnet;
    @DatabaseField(columnName = COLUMNNAME_YMAGNET,useGetSet = true,canBeNull = false)
    private double ymagnet;
    @DatabaseField(columnName = COLUMNNAME_ZMAGNET,useGetSet = true,canBeNull = false)
    private double zmagnet;
    @DatabaseField(columnName = COLUMNNAME_X,useGetSet = true)
    private double x;
    @DatabaseField(columnName = COLUMNNAME_Y,useGetSet = true)
    private double y;

    public MagnetBean(){

    }

    public MagnetBean(double xmagnet, double ymagnet, double zmagnet, double x, double y) {
        this.xmagnet = xmagnet;
        this.ymagnet = ymagnet;
        this.zmagnet = zmagnet;
        this.x = x;
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getXmagnet() {
        return xmagnet;
    }

    public void setXmagnet(double xmagnet) {
        this.xmagnet = xmagnet;
    }

    public double getYmagnet() {
        return ymagnet;
    }

    public void setYmagnet(double ymagnet) {
        this.ymagnet = ymagnet;
    }

    public double getZmagnet() {
        return zmagnet;
    }

    public void setZmagnet(double zmagnet) {
        this.zmagnet = zmagnet;
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

    @Override
    public String toString() {
        return "MagnetBean{" +
                "id=" + id +
                ", xmagnet=" + xmagnet +
                ", ymagnet=" + ymagnet +
                ", zmagnet=" + zmagnet +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
