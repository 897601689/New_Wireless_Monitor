package com.rilintech.new_wireless_monitor;

import com.j256.ormlite.field.DatabaseField;

/**
 * A simple demonstration object we are creating and persisting to the database.
 * �������ڴ���һ���򵥵���ʾ����־û������ݿ�
 */
public class ParamsSettingData {

	// ID �����ݿ��Զ����ɵ�
	@DatabaseField(generatedId = true)
	int id;
	@DatabaseField(index = true)//����stringΪ���ݿ��ֶ�
	String param_name;//��������
	@DatabaseField
	String param_value;//����ֵ
	

	ParamsSettingData() {
		// needed by ormlite
	}
	//ͬʱ���ò���ֵ�Ͳ�������
	public ParamsSettingData(String p_name,String p_value) {
		
		this.param_name = p_name;
		this.param_value = p_value;
	}
	//��ȡ��������
	public String getParamName()
	{
		return this.param_name;
	}
	//��ȡ����ֵ
	public String getParamValue()
	{
		return this.param_value;
	}
	//���ò�������
	public void setParamName(String p_name)
	{
		this.param_name = p_name;
	}
	//���ò���ֵ
	public void setParamValue(String p_value)
	{
		this.param_value = p_value;
	}

	
}
