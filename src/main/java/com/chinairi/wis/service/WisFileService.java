package com.chinairi.wis.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.chinairi.wis.entity.WisHead;

public interface WisFileService {
	WisHead getWisHead(File file) throws Exception;

	List<Map<String,String>>getStreamContents(File file) throws Exception;

	List<Map<String,List<String>>> getChannelCurves(File file) throws Exception;

	List<Map<String,List<Object>>> getTables(File file) throws Exception;
	
	List<Map<String,List<Object>>> getTables(File file,int format) throws Exception;
}
