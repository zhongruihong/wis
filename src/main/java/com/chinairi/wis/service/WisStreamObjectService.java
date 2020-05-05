package com.chinairi.wis.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.chinairi.wis.entity.WisObjectEntry;
import com.chinairi.wis.entity.WisStreamObject;

public interface WisStreamObjectService {
	/**
	 * 流对象
	 * @param file wis文件
	 * @param entry 对象入口
	 * @return
	 * @throws Exception 
	 */
	WisStreamObject getWisStreamObject(File file,WisObjectEntry entry) throws Exception;
	
	List<WisStreamObject> listWisStreamObject(File file) throws Exception;
	
	List<Map<String,String>>listStreamContent(File file) throws Exception;

}
