package com.junjie.indoorj.database.dao;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.junjie.indoorj.database.entity.MagnetBean;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by junjihua on 2017/6/7.

 * 操作User数据表的Dao类，封装这操作User表的所有操作
 * 通过DatabaseHelper类中的方法获取ORMLite内置的DAO类进行数据库中数据的操作
 * <p>
 * 调用dao的create()方法向表中添加数据
 * 调用dao的delete()方法删除表中的数据
 * 调用dao的update()方法修改表中的数据
 * 调用dao的queryForAll()方法查询表中的所有数据
 */

public class MagnetDAO {
    private Context context;
    private Dao<MagnetBean, Integer> dao;
   // private DatabaseHelper helper;

    public MagnetDAO(Context context) {
        this.context = context;
        try {
            this.dao=DatabaseHelper.getInstance(context).getDao(MagnetBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("construct","error");
        }
    }
    // 向表中添加一条数据
    public void insert(MagnetBean data) {
        try {
            dao.create(data);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("inserts","Merror");
        }
    }
    //向表中添加多条数据
    public void insert(ArrayList<MagnetBean> datas ){
        try {
            dao.create(datas);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("insertsMul","Merror");
        }
    }

    // 删除表中的一条数据
    public void delete(MagnetBean data) {
        try {
            dao.delete(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 修改表中的一条数据
    public void update(MagnetBean data) {
        try {
            dao.update(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 查询表中的所有数据
    public List<MagnetBean> selectAll() {
        List<MagnetBean> magnets = null;
        try {

            magnets = dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return magnets;
    }

    // 根据ID取出magnet信息
    public MagnetBean queryById(int id) {
        MagnetBean magnet = null;
        try {
            magnet = dao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return magnet;
    }


}
