package com.chinairi.wis.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.chinairi.wis.entity.WisChannelObject;
import com.chinairi.wis.entity.WisObjectEntry;

public interface WisChannelObjectService {

	WisChannelObject getWisChannelObject(File file, WisObjectEntry entry) throws Exception;
	
	List<WisChannelObject> listWisChannelObject(File file) throws Exception;

	List<String> getChannelCurve(File file, WisObjectEntry entry)throws Exception;
	
	List<String> getChannelCurve(File file,WisChannelObject channelObject)throws Exception;
	
	List<Map<String,List<String>>> listChannelCurves(File file) throws Exception;
}
