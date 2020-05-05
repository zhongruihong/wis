package com.chinairi.wis.service.impl;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinairi.wis.entity.WisHead;
import com.chinairi.wis.service.WisChannelObjectService;
import com.chinairi.wis.service.WisFileService;
import com.chinairi.wis.service.WisHeadService;
import com.chinairi.wis.service.WisStreamObjectService;
import com.chinairi.wis.service.WisTableObjectService;
@Service
public class WisFileServiceImpl implements WisFileService {

	@Autowired
	private WisTableObjectService tableService;
	@Autowired
	private WisChannelObjectService channelService;
	@Autowired
	private WisStreamObjectService streamService;
	@Autowired
	private WisHeadService headService;
	@Override
	public WisHead getWisHead(File file) throws Exception {
		return headService.getWisHead(file);
	}
	@Override
	public List<Map<String, String>> getStreamContents(File file) throws Exception {
		return streamService.listStreamContent(file);
	}
	@Override
	public List<Map<String, List<String>>> getChannelCurves(File file) throws Exception {
		return channelService.listChannelCurves(file);
	}
	
	@Override
	public List<Map<String,List<Object>>> getTables(File file) throws Exception {
		return getTables(file,0);
	}

	@Override
	public List<Map<String,List<Object>>> getTables(File file, int format) throws Exception {
		return tableService.listTables(file,format);
	}

}
