package com.chinairi.wis.service;

import java.io.File;
import java.util.List;
import com.chinairi.wis.entity.WisHead;
import com.chinairi.wis.entity.WisObjectEntry;

public interface WisObjectEntryService {

	/**
	 * 对象入口
	 * @param file wis文件
	 * @param head 头部对象
	 * @return
	 * @throws Exception 
	 */
	List<WisObjectEntry> listWisObjectEntry(File file) throws Exception;
	
	List<WisObjectEntry> listWisObjectEntry(File file,WisHead head) throws Exception;
}
