package com.rilintech.new_wireless_monitor;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * 数据库操作类用于管理数据库的创建和更新您的数据库。这个类通常也提供了其他类使用的Daos
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	// 数据库文件的名称为您的应用程序  -- change to something appropriate for your app
	private static final String DATABASE_NAME = "WMBase.db";
	// 任何时候你改变你的数据库对象,您可能需要增加数据库版本
	private static final int DATABASE_VERSION = 1;

	// 我们使用DAO对象访问 ParamsSettingData表
	private Dao<ParamsSettingData, Integer> ps_data = null;
	

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * This is called when the database is first created. Usually you should call createTable statements here to create
	 * the tables that will store your data.
	 */
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			Log.i(DatabaseHelper.class.getName(), "开始创建数据库");
			TableUtils.createTable(connectionSource, ParamsSettingData.class);

			// here we try inserting data in the on-create as a test
			//Dao<ParamsSettingData, Integer> ps_data = getParamsSettingDataDao();
			
			//ParamsSettingData simple = new ParamsSettingData(millis);
			//dao.create(simple);
			//simple = new SimpleData(millis + 1);
			//dao.create(simple);
			
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "创建数据库失败", e);
			throw new RuntimeException(e);
		}
	}
	/**
	 * Returns the Database Access Object (DAO) for our SimpleData class. It will create it or just give the cached
	 * value.
	 * 返回数据库访问对象(DAO)为我们SimpleData类。它将创建或给缓存的值。
	 */
	public Dao<ParamsSettingData, Integer> getParamsSettingDataDao() throws SQLException {
		if (ps_data == null) {
			ps_data = getDao(ParamsSettingData.class);
		}
		return ps_data;
	}
	/**
	 * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
	 * the various data to match the new version number.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		
	}

	
	/**
	 * 关闭数据库连接和清除缓存DAO。
	 */
	@Override
	public void close() {
		super.close();
		
	}
}
