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
 * ���ݿ���������ڹ������ݿ�Ĵ����͸����������ݿ⡣�����ͨ��Ҳ�ṩ��������ʹ�õ�Daos
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	// ���ݿ��ļ�������Ϊ����Ӧ�ó���  -- change to something appropriate for your app
	private static final String DATABASE_NAME = "WMBase.db";
	// �κ�ʱ����ı�������ݿ����,��������Ҫ�������ݿ�汾
	private static final int DATABASE_VERSION = 1;

	// ����ʹ��DAO������� ParamsSettingData��
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
			Log.i(DatabaseHelper.class.getName(), "��ʼ�������ݿ�");
			TableUtils.createTable(connectionSource, ParamsSettingData.class);

			// here we try inserting data in the on-create as a test
			//Dao<ParamsSettingData, Integer> ps_data = getParamsSettingDataDao();
			
			//ParamsSettingData simple = new ParamsSettingData(millis);
			//dao.create(simple);
			//simple = new SimpleData(millis + 1);
			//dao.create(simple);
			
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "�������ݿ�ʧ��", e);
			throw new RuntimeException(e);
		}
	}
	/**
	 * Returns the Database Access Object (DAO) for our SimpleData class. It will create it or just give the cached
	 * value.
	 * �������ݿ���ʶ���(DAO)Ϊ����SimpleData�ࡣ����������������ֵ��
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
	 * �ر����ݿ����Ӻ��������DAO��
	 */
	@Override
	public void close() {
		super.close();
		
	}
}
