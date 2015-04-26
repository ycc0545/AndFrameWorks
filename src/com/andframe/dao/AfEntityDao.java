package com.andframe.dao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import android.content.Context;

import com.andframe.annotation.db.Interpreter;
import com.andframe.application.AfExceptionHandler;
import com.andframe.util.java.AfReflecter;

public class AfEntityDao<T> extends AfDao<T>{

	public AfEntityDao(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public AfEntityDao(Context context, String dbname) {
		super(context, dbname);
		// TODO Auto-generated constructor stub
	}

	public AfEntityDao(Context context,String path, String dbname) {
		super(context, path, dbname);
		// TODO Auto-generated constructor stub
	}

	/**
	 * ��ȡȫ��
	 * @return
	 */
	public List<T> getAll(){
		return getListEntity(super.getModelsAll("*"));
	}

	/**
	 * ��ȡȫ��
	 * 
	 * @param order
	 * @return
	 */
	public List<T> getAll(String order) {
		return getListEntity(super.getModelsAll("*",order));
	}
	
	/**
	 * ��ҳ��ѯ
	 * @param num
	 * @param offset
	 * @return
	 */
	public List<T> getLimit(int num, int offset) {
		return getListEntity(super.getModelsLimit("*", num, offset));
	}

	/**
	 * ��ҳ��ѯ ������
	 * @param order
	 * @param num
	 * @param offset
	 * @return
	 */
	public List<T> getLimit(String order, int num,int offset) {
		return getListEntity(super.getModelsLimit("*",order, num, offset));
	}

	/**
	 * ��ҳ��ѯ ������ ����
	 * 
	 * @param where
	 * @param order
	 * @param num
	 * @param offset
	 * @return
	 */
	public List<T> getLimit(String where, String order,
			int num, int offset) {
		return getListEntity(super.getModelsLimit("*",where,order, num, offset));
	}

	/**
	 * ��ҳ��ѯ ������ ����
	 * 
	 * @param where
	 * @param order
	 * @return
	 */
	public List<T> getWhere(String where, String order) {
		return getListEntity(super.getModelsWhere("*",where,order));
	}
	/**
	 * ������ѯ ����ҳ
	 * 
	 * @param column
	 * @param where
	 * @param num
	 * @param offset
	 * @return
	 */
	public List<T> getWhere(String where, int num,int offset) {
		return getListEntity(super.getModelsWhere("*",where,num,offset));
	}

	/**
	 * ������ѯ
	 * 
	 * @param where
	 * @return
	 */
	public final List<T> getWhere(String where) {
		return getListEntity(super.getModelsWhere("*",where));
	}

	
	protected List<T> getListEntity(List<Model> models) {
		List<T> ltEntity = new ArrayList<T>();
		try {
			for (Model model : models) {
				ltEntity.add(getEntity(model));
			}
		} catch (Throwable ex) {
			// TODO Auto-generated catch block
			AfExceptionHandler.handler(ex, "AfEntityDao.getListEntity.getEntity-Exception");
		}
		return ltEntity;
	}
	
	protected T getEntity(Model model) throws Exception {
		// TODO Auto-generated method stub
		T entity = mClazz.newInstance();
		for (Field field : AfReflecter.getField(mClazz)) {
			if (Interpreter.isColumn(field)) {
				String column = Interpreter.getColumnName(field);
				Class<?> type = field.getType();
				field.setAccessible(true);
				if (type.equals(Short.class) || type.equals(short.class)) {
					field.set(entity, model.getShort(column));
				} else if (type.equals(Integer.class) || type.equals(int.class)) {
					field.set(entity, model.getInt(column));
				} else if (type.equals(Long.class) || type.equals(long.class)) {
					field.set(entity, model.getLong(column));
				} else if (type.equals(Float.class) || type.equals(float.class)) {
					field.set(entity, model.getFloat(column));
				} else if (type.equals(Double.class) || type.equals(double.class)) {
					field.set(entity, model.getDouble(column));
				} else if (type.equals(Boolean.class) || type.equals(boolean.class)) {
					field.set(entity, model.getBoolean(column));
				} else if (type.equals(Date.class)) {
					field.set(entity, model.getDate(column));
				} else if (type.equals(UUID.class)) {
					field.set(entity, model.getUUID(column));
				} else if (type.equals(String.class)) {
					field.set(entity, model.getString(column));
				}
			}
		}
		return entity;
	}
}