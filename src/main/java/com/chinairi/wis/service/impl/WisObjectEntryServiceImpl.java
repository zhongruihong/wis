package com.chinairi.wis.service.impl;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinairi.wis.entity.WisHead;
import com.chinairi.wis.entity.WisObjectEntry;
import com.chinairi.wis.service.WisHeadService;
import com.chinairi.wis.service.WisObjectEntryService;
import com.chinairi.wis.utils.MathUtil;
@Service
public class WisObjectEntryServiceImpl implements WisObjectEntryService {

	@Autowired
	private WisHeadService headService;
	@Override
	public List<WisObjectEntry> listWisObjectEntry(File file, WisHead head) throws Exception {
		if (file.exists() && head != null) {
			FileInputStream is = new FileInputStream(file);
			DataInputStream dis = new DataInputStream(is);
			List<WisObjectEntry> list = new ArrayList<WisObjectEntry>();
			int offset = (int) head.getObj_entry_offset();
			int objnum = (int) head.getFact_obj_num();
			try {
				byte[] buf = new byte[offset];
				dis.read(buf, 0, offset);
				int size = 72;
				for (int a = 0; a < objnum; a++) {
					WisObjectEntry entry = new WisObjectEntry();
					byte[] itemBuf = new byte[size];
					dis.read(itemBuf, 0, size);
					entry.setObj_name(MathUtil.byteToString( Arrays.copyOfRange(itemBuf, 0, 16)));
					entry.setObj_status(MathUtil.byteToShort(Arrays.copyOfRange(itemBuf, 16, 20)));
					entry.setObj_primary_attribute(MathUtil.byteToShort(Arrays.copyOfRange(itemBuf, 20, 22)));
					entry.setObj_sub_attribute(MathUtil.byteToShort(Arrays.copyOfRange(itemBuf, 22, 24)));
					entry.setObj_data_offset(MathUtil.dwordBytesToLong(Arrays.copyOfRange(itemBuf, 24, 28)));
					entry.setObj_block_num(MathUtil.dwordBytesToLong(Arrays.copyOfRange(itemBuf, 28, 32)));
					entry.setObj_create_time(new Date(MathUtil.dwordBytesToLong(Arrays.copyOfRange(itemBuf, 32, 36)) * 1000));
					entry.setObj_update_time(new Date(MathUtil.dwordBytesToLong(Arrays.copyOfRange(itemBuf, 36, 40)) * 1000));
					entry.setObj_desc(MathUtil.byteToString(Arrays.copyOfRange(itemBuf, 40, 72)));
					list.add(entry);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				dis.close();
				is.close();
			}
			return list;
		}
		return null;
	}
	@Override
	public List<WisObjectEntry> listWisObjectEntry(File file) throws Exception {
		WisHead head = headService.getWisHead(file);
		if (head != null)
			return listWisObjectEntry(file, head);
		return null;
	}
}
