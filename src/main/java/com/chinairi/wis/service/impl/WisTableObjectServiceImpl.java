package com.chinairi.wis.service.impl;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.chinairi.wis.entity.WisObjectEntry;
import com.chinairi.wis.entity.WisTableField;
import com.chinairi.wis.entity.WisTableObject;
import com.chinairi.wis.service.WisObjectEntryService;
import com.chinairi.wis.service.WisTableObjectService;
import com.chinairi.wis.utils.MathUtil;

@Service
public class WisTableObjectServiceImpl implements WisTableObjectService {
	@Autowired
	private WisObjectEntryService entryService;

	@Override
	public WisTableObject getWisTableObject(File file, WisObjectEntry entry) throws Exception {
		if (file.exists() && entry != null) {
			if (entry.getObj_primary_attribute() == 2) {
				FileInputStream is = new FileInputStream(file);
				DataInputStream dis = new DataInputStream(is);
				WisTableObject wisTableObject = new WisTableObject();
				try {
					String tab_name = entry.getObj_name().trim();
					wisTableObject.setTab_name(tab_name);
					int offset = (int) entry.getObj_data_offset();
					wisTableObject.setTab_data_offset(offset);
					byte[] buf = new byte[offset];
					dis.read(buf, 0, offset);
					byte[] item = new byte[8];
					dis.read(item, 0, 8);
					wisTableObject.setTab_record_num(MathUtil.dwordBytesToLong(Arrays.copyOfRange(item, 0, 4)));
					long tab_filed_num = MathUtil.dwordBytesToLong(Arrays.copyOfRange(item, 4, 8));
					wisTableObject.setTab_filed_num(tab_filed_num);
					List<WisTableField> list = new ArrayList<WisTableField>();
					int rowLen = 0;
					for (int i = 0; i < tab_filed_num; i++) {
						WisTableField filed = new WisTableField();
						byte[] filedBuf = new byte[40];
						dis.read(filedBuf, 0, 40);
						String filed_name = MathUtil.byteToString(Arrays.copyOfRange(filedBuf, 0, 32));
						filed.setFiled_name(filed_name.split("\\x00")[0]);
						short filed_type = MathUtil.byteToShort(Arrays.copyOfRange(filedBuf, 32, 36));
						filed.setFiled_type(filed_type);
						byte[] length = Arrays.copyOfRange(filedBuf, 36, 40);
						// TODO: byte[] -> unsinged long type为6(string)的长度必须通过(36,40)位转换！
						// ==========================================
//						int byteToInt = MathUtil.byteToInt(length);
//						char byteToChar = MathUtil.byteToChar(length);
//						short byteToShort = MathUtil.byteToShort(length);
//						long dwordBytesToLong = MathUtil.dwordBytesToLong(length);
						// filed_length = MathUtil.byteToULong(length);
						// =========================================
						long filed_length = 0;
						filed.setFiled_length(filed_length);
						rowLen += filed_length;
						list.add(filed);
					}
					wisTableObject.setTab_row_length(rowLen);
					wisTableObject.setFiledList(list);
					return wisTableObject;
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					dis.close();
					is.close();
				}
			}
		}
		return null;
	}

	@Override
	public List<WisTableObject> listWisTableObject(File file) throws Exception {
		List<WisObjectEntry> entries = entryService.listWisObjectEntry(file);
		if (entries != null && entries.size() > 0) {
			List<WisTableObject> list = new ArrayList<WisTableObject>();
			for (int i = 0; i < entries.size(); i++) {
				WisTableObject wisTableObject = getWisTableObject(file, entries.get(i));
				if (wisTableObject != null)
					list.add(wisTableObject);
			}
			return list;
		}
		return null;
	}

	@Override
	public List<Object> getTable(File file, WisObjectEntry entry) throws Exception {
		return getTable(file, entry, 0);

	}

	@Override
	public List<Object> getTable(File file, WisTableObject tableObject, int format) throws Exception {
		if (file.exists() && tableObject != null) {
			FileInputStream is = new FileInputStream(file);
			DataInputStream dis = new DataInputStream(is);
			List<Object> table = new ArrayList<Object>();
			try {
				int record_num = (int) tableObject.getTab_record_num();
				int filed_num = (int) tableObject.getTab_filed_num();
				int row_length = tableObject.getTab_row_length();
				int offset = tableObject.getTab_data_offset();
				List<WisTableField> filedList = tableObject.getFiledList();
				byte[] buf = new byte[offset + 8 + filed_num * 40];
				dis.read(buf, 0, offset + 8 + filed_num * 40);
				for (int i = 0; i < record_num; i++) {
					StringBuffer sb = new StringBuffer();
					for (int j = 0; j < filed_num; j++) {// one row
						byte[] row = new byte[row_length];
						dis.read(row, 0, row_length);
						int type = filedList.get(j).getFiled_type();
						int length = (int) filedList.get(j).getFiled_length();
						byte[] b = Arrays.copyOfRange(row, 0, length);
						Object o = byteToContent(type, b);
						sb.append(o + "\t");
					}
					if (format == 0) {
						table.add(sb.toString());
					} else if (format == 1) {
						table.add(sb.toString().split("\t"));
					}
				}
				return table;
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				dis.close();
				is.close();
			}
		}
		return null;
	}

	@Override
	public List<Object> getTable(File file, WisObjectEntry entry, int type) throws Exception {
		WisTableObject wisTableObject = getWisTableObject(file, entry);
		if (wisTableObject != null)
			return getTable(file, wisTableObject, 0);
		return null;
	}

	public List<Object> getTable(File file, WisTableObject tableObject) throws Exception {
		return getTable(file, tableObject, 0);
	}

	@Override
	public List<Map<String, List<Object>>> listTables(File file) throws Exception {
		return listTables(file, 0);
	}

	@Override
	public List<Map<String, List<Object>>> listTables(File file, int format) throws Exception {
		List<WisTableObject> tableObjects = listWisTableObject(file);
		if (tableObjects != null) {
			List<Map<String, List<Object>>> list = new ArrayList<Map<String, List<Object>>>();
			for (WisTableObject tableObject : tableObjects) {
				if (tableObject != null) {
					Map<String, List<Object>> map = new HashMap<String, List<Object>>();
					map.put(tableObject.getTab_name(), getTable(file, tableObject, format));
					list.add(map);
				}
			}
			return list;
		}
		return null;
	}

	private Object byteToContent(int type, byte[] b) {
		Object o = new Object();
		switch (type) {
		case 1:
			o = MathUtil.byteToInt(b);
			break;
		case 2:
			o = MathUtil.byteToShort(b);
			break;
		case 3:
			o = MathUtil.dwordBytesToLong(b);
			break;
		case 4:
			o = MathUtil.getFloat(b);
			break;
		case 5:
			o = MathUtil.byteToDouble(b);
			break;
		case 6:
			o = MathUtil.byteToString(b);
			break;
		case 7:
			o = MathUtil.byteToChar(b);
			break;
		case 8:
			o = MathUtil.byteToUChar(b);
			break;
		case 9:
			o = MathUtil.byteToUShort(b);
			break;
		case 10:
			o = MathUtil.byteToUInt(b);
			break;
		case 11:
			o = MathUtil.byteToULong(b);
			break;
		default:
			break;
		}
		return o;
	}
}
