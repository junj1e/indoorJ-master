package com.junjie.indoorj.database.dao;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.junjie.indoorj.database.entity.RssiBean;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by junjihua on 2017/6/7.
 */

public class RssiDAO {
    private Context context;
    private Dao<RssiBean, Integer> dao;
    //private DatabaseHelper helper;

    public RssiDAO(Context context) {
        this.context = context;
        try {
            this.dao=DatabaseHelper.getInstance(context).getDao(RssiBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // 向表中添加一条数据
    public void insert(RssiBean data) {
        try {
            dao.create(data);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("inserts","Rerror");
        }
    }

    //向表中添加多条数据
    public void insert(List<RssiBean> datas ){
        try {
            dao.create(datas);
            Toast.makeText(context, "收集完成", Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("insertsMul","Rerror");
        }
    }
    // 删除表中的一条数据
    public void delete(RssiBean data) {
        try {
            dao.delete(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 修改表中的一条数据
    public void update(RssiBean data) {
        try {
            dao.update(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 查询表中的所有数据
    public List<RssiBean> selectAll() {
        List<RssiBean> Rssis = null;
        try {

            Rssis = dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Rssis;
    }

    // 根据ID取出Rssi信息
    public RssiBean queryById(int id) {
        RssiBean Rssi = null;
        try {
            Rssi = dao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Rssi;
    }
}
