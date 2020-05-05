package com.chinairi.wis.service;

import java.io.File;

import com.chinairi.wis.entity.WisHead;

public interface WisHeadService {

	/**
	 * wis头部
	 * @param file wis文件
	 * @return
	 * @throws Exception 
	 */
	WisHead getWisHead(File file) throws Exception;
}
