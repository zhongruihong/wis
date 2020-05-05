package com.chinairi.wis.service.impl;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.chinairi.wis.entity.WisHead;
import com.chinairi.wis.service.WisHeadService;
import com.chinairi.wis.utils.MathUtil;
@Service
public class WisHeadServiceImpl implements WisHeadService {
	@Override
	public WisHead getWisHead(File file) throws Exception {
		if (file.exists()) {
			WisHead head = new WisHead();
			FileInputStream is = new FileInputStream(file);
			DataInputStream dis = new DataInputStream(is);
			try {
				byte[] itemBuf = new byte[66];
				dis.read(itemBuf, 0, 66);
				String version = new String(itemBuf, 0, 10);
				head.setVersion(version);
				head.setSys_type(MathUtil.byteToShort(Arrays.copyOfRange(itemBuf, 10, 12)));
				head.setMax_obj_num(MathUtil.byteToShort(Arrays.copyOfRange(itemBuf, 12, 14)));
				head.setFact_obj_num(MathUtil.byteToShort(Arrays.copyOfRange(itemBuf, 14, 16)));
				head.setBlock_size(MathUtil.byteToShort(Arrays.copyOfRange(itemBuf, 16, 18)));
				head.setObj_entry_offset(MathUtil.dwordBytesToLong(Arrays.copyOfRange(itemBuf, 18, 22)));
				head.setObj_data_offset(MathUtil.dwordBytesToLong(Arrays.copyOfRange(itemBuf, 22, 26)));
				head.setFile_size(MathUtil.dwordBytesToLong(Arrays.copyOfRange(itemBuf, 26, 30)));
				head.setFile_create_time(new Date(MathUtil.dwordBytesToLong(Arrays.copyOfRange(itemBuf, 30, 34)) * 1000));
				head.setFile_desc(MathUtil.byteToString(Arrays.copyOfRange(itemBuf, 34, 66)));
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				dis.close();
				is.close();
			}
			return head;
		}
		return null;
	}
}
