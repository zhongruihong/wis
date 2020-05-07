package com.chinairi.wis.service.impl;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.chinairi.wis.entity.WisObjectEntry;
import com.chinairi.wis.entity.WisStreamObject;
import com.chinairi.wis.service.WisObjectEntryService;
import com.chinairi.wis.service.WisStreamObjectService;
import com.chinairi.wis.utils.MathUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WisStreamObjectServiceImpl implements WisStreamObjectService {
	@Autowired
	private WisObjectEntryService entryService;
	@Autowired
	private WisStreamObjectService streamService;
	@Override
	public WisStreamObject getWisStreamObject(File file, WisObjectEntry entry) throws Exception {
		if (file.exists() && entry != null) {
			if (entry.getObj_primary_attribute() == 3) {
				FileInputStream is = new FileInputStream(file);
				DataInputStream dis = new DataInputStream(is);
				WisStreamObject streamObj = new WisStreamObject();
				try {
					String obj_name = entry.getObj_name();
					streamObj.setStream_name(obj_name);
					int offset = (int) entry.getObj_data_offset();
					byte[] buf = new byte[offset];
					dis.read(buf, 0, offset);
					byte[] item = new byte[4];
					dis.read(item, 0, 4);
					long length = MathUtil.dwordBytesToLong(item);
					streamObj.setStream_length(length);
					byte[] stream = new byte[(int) length];
					dis.read(stream, 0, (int) length);
					streamObj.setContent(MathUtil.byteToString(stream));
					log.info("流:"+obj_name +"\r\t"+ streamObj.toString());
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					dis.close();
					is.close();
				}
				return streamObj;
			}
		}
		return null;
	}
	
	private List<WisStreamObject> listWisStreamObject(File file, List<WisObjectEntry> entries) throws Exception {
		List<WisStreamObject> list = new ArrayList<WisStreamObject>();
		for (WisObjectEntry entry : entries) {
			WisStreamObject wisStreamObject = getWisStreamObject(file, entry);
			if (wisStreamObject != null)
				list.add(wisStreamObject);
		}
		return list;
	}
	
	@Override
	public List<WisStreamObject> listWisStreamObject(File file) throws Exception {
		List<WisObjectEntry> entries = entryService.listWisObjectEntry(file);
		if (entries != null)
			return listWisStreamObject(file, entries);
		return null;
	}
	@Override
	public List<Map<String, String>> listStreamContent(File file) throws Exception {
		List<WisStreamObject> streams = streamService.listWisStreamObject(file);
		if (streams != null) {
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			for (WisStreamObject stream : streams) {
				if (stream != null) {
					Map<String, String> map = new HashMap<String, String>();
					map.put(stream.getStream_name(), stream.getContent());
					list.add(map);
				}
			}
			return list;
		}
		return null;
	}
}
