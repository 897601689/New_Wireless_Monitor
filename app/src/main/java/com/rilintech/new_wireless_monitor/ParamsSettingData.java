package com.rilintech.new_wireless_monitor;

import com.j256.ormlite.field.DatabaseField;

/**
 * A simple demonstration object we are creating and persisting to the database.
 * 我们正在创建一个简单的演示对象持久化到数据库
 */
public class ParamsSettingData {

	// ID 是数据库自动生成的
	@DatabaseField(generatedId = true)
	int id;
	@DatabaseField(index = true)//声明string为数据库字段
	String param_name;//参数名称
	@DatabaseField
	String param_value;//参数值
	

	ParamsSettingData() {
		// needed by ormlite
	}
	//同时设置参数值和参数名称
	public ParamsSettingData(String p_name,String p_value) {
		
		this.param_name = p_name;
		this.param_value = p_value;
	}
	//获取参数名称
	public String getParamName()
	{
		return this.param_name;
	}
	//获取参数值
	public String getParamValue()
	{
		return this.param_value;
	}
	//设置参数名称
	public void setParamName(String p_name)
	{
		this.param_name = p_name;
	}
	//设置参数值
	public void setParamValue(String p_value)
	{
		this.param_value = p_value;
	}

	
}
