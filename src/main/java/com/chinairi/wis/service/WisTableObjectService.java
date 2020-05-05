package com.chinairi.wis.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.chinairi.wis.entity.WisObjectEntry;
import com.chinairi.wis.entity.WisTableObject;

public interface WisTableObjectService {
	/**
	 * 表对象
	 * 
	 * @param file  wis文件
	 * @param entry 对象入口
	 * @return
	 * @throws Exception
	 */
	WisTableObject getWisTableObject(File file, WisObjectEntry entry) throws Exception;

	List<WisTableObject> listWisTableObject(File file) throws Exception;

	List<Object> getTable(File file, WisTableObject tableObject) throws Exception;
	List<Object> getTable(File file, WisTableObject tableObject,int format) throws Exception;

	List<Object> getTable(File file, WisObjectEntry entry) throws Exception;
	List<Object> getTable(File file, WisObjectEntry entry,int format) throws Exception;
	
	List<Map<String,List<Object>>> listTables(File file) throws Exception;
	List<Map<String,List<Object>>> listTables(File file,int format) throws Exception;
}
