package com.chinairi.wis.controller;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chinairi.wis.service.WisFileService;
import com.chinairi.wis.utils.FileUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value="/wis")
@Slf4j
public class TestController {

	@Autowired
	private WisFileService fileService;
	@GetMapping("/file")
	//http://localhost:8080/wis/file?path=C://Users//zrh//Desktop//test//1.WIS
	public Object get(String path) throws Exception {
		try {
			log.info("==========wis file start=============");
			if(FileUtil.hasWis(path)) {
				File file = new File(path);
				if(file.exists()) {
					List<Map<String, List<Object>>> tables = fileService.getTables(file);
					List<Map<String, String>> streamContent = fileService.getStreamContents(file);
					List<Map<String, List<String>>> channelCurves = fileService.getChannelCurves(file);
					return tables;
				}
			}
			log.info("==========wis file end=============");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
