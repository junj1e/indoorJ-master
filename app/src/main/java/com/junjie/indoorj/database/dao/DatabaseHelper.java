package com.junjie.indoorj.database.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.junjie.indoorj.database.entity.MagnetBean;
import com.junjie.indoorj.database.entity.RssiBean;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nova on 2017/6/6.
 * cnhjj2008@gmail.com
 * 数据库操作管理工具类
 * <p>
 * 我们需要自定义一个类继承自ORMlite给我们提供的OrmLiteSqliteOpenHelper，创建一个构造方法，重写两个方法onCreate()和onUpgrade()
 * 在onCreate()方法中使用TableUtils类中的createTable()方法初始化数据表
 * 在onUpgrade()方法中我们可以先删除所有表，然后调用onCreate()方法中的代码重新创建表
 * <p>
 * 我们需要对这个类进行单例，保证整个APP中只有一个SQLite Connection对象
 * <p>
 * 这个类通过一个Map集合来管理APP中所有的DAO，只有当第一次调用这个DAO类时才会创建这个对象（并存入Map集合中）
 * 其他时候都是直接根据实体类的路径从Map集合中取出DAO对象直接调用
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    // 数据库名称
    public static final String DATABASE_NAME = "rssidb.db";
    private static final int DATABASE_VERSION = 1;

    // 本类的单例实例
    private static DatabaseHelper instance;

    // 存储APP中所有的DAO对象的Map集合
    private Map<String, Dao> daos = new HashMap<>();

    // 获取本类单例对象的方法
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null) {
                    instance = new DatabaseHelper(context);
                }
            }
        }
        return instance;
    }

    // 私有的构造方法
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // 根据传入的DAO的路径获取到这个DAO的单例对象（要么从daos这个Map中获取，要么新创建一个并存入daos）
    public synchronized Dao getDao(Class clazz) throws SQLException {
        Dao dao = null;
        String className = clazz.getSimpleName();
        if (daos.containsKey(className)) {
            dao = daos.get(className);
        }
        if (dao == null) {
            dao = super.getDao(clazz);
            daos.put(className, dao);
        }
        return dao;
    }

    @Override// 创建数据库时调用的方法
    public synchronized void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, RssiBean.class);
            TableUtils.createTableIfNotExists(connectionSource, MagnetBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("create","error");
        }


    }

    @Override// 数据库版本更新时调用的方法
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, RssiBean.class, true);
            TableUtils.dropTable(connectionSource, MagnetBean.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override//释放资源
    public void close() {
        super.close();
        for (String key : daos.keySet()) {
            Dao dao = daos.get(key);
            dao = null;
        }
    }



}
