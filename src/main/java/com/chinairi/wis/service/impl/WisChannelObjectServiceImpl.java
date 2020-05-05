package com.chinairi.wis.service.impl;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.chinairi.wis.entity.WisChannelDimension;
import com.chinairi.wis.entity.WisChannelObject;
import com.chinairi.wis.entity.WisObjectEntry;
import com.chinairi.wis.service.WisChannelObjectService;
import com.chinairi.wis.service.WisObjectEntryService;
import com.chinairi.wis.utils.MathUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WisChannelObjectServiceImpl implements WisChannelObjectService {

	@Autowired
	private WisObjectEntryService entryService;
	@Override
	public WisChannelObject getWisChannelObject(File file, WisObjectEntry entry) throws Exception {
		if (entry.getObj_primary_attribute() == 1 && entry.getObj_sub_attribute() == 1) {
			if (file.exists()) {
				FileInputStream is = new FileInputStream(file);
				DataInputStream dis = new DataInputStream(is);
				WisChannelObject channelObj = new WisChannelObject();
				try {
					String obj_name = entry.getObj_name();
					channelObj.setChannel_name(obj_name);
					int offset = (int) entry.getObj_data_offset();
					channelObj.setChannel_data_offset(offset);
					int block_bum = (int) entry.getObj_block_num();
					channelObj.setChannel_block_num(block_bum);
					byte[] item = new byte[offset];
					dis.read(item, 0, offset);
					byte[] buf = new byte[280];
					dis.read(buf, 0, 280);
					channelObj.setChannel_unit(MathUtil.byteToString(Arrays.copyOfRange(buf, 0, 8)));
					channelObj.setChannel_alias(MathUtil.byteToString(Arrays.copyOfRange(buf, 8, 24)));
					channelObj.setChannel_unit_alias(MathUtil.byteToString(Arrays.copyOfRange(buf, 24, 40)));
					channelObj.setChannel_data_type(MathUtil.byteToShort(Arrays.copyOfRange(buf, 40, 42)));
					channelObj.setChannel_data_type_length(MathUtil.byteToShort(Arrays.copyOfRange(buf, 42, 44)));
					channelObj.setChannel_minus(MathUtil.getFloat(Arrays.copyOfRange(buf, 44, 48)));
					channelObj.setChannel_maximum(MathUtil.getFloat(Arrays.copyOfRange(buf, 48, 52)));
					channelObj.setChannel_keep(MathUtil.byteToShort(Arrays.copyOfRange(buf, 52, 54)));
					short Channel_dimension_num = MathUtil.byteToShort(Arrays.copyOfRange(buf, 54, 56));
					channelObj.setChannel_dimension_num(Channel_dimension_num);
					List<WisChannelDimension> dimslist = new ArrayList<WisChannelDimension>();
					for (int dimsnum = 0; dimsnum < Channel_dimension_num; dimsnum++) {
						int incrmentbyte = 56 * dimsnum;
						WisChannelDimension dims = new WisChannelDimension();
						dims.setDim_name(MathUtil.byteToString(Arrays.copyOfRange(buf, 56 + incrmentbyte, 64 + incrmentbyte)));
						dims.setDim_unit(MathUtil.byteToString(Arrays.copyOfRange(buf, 64 + incrmentbyte, 72 + incrmentbyte)));
						dims.setDim_alias(MathUtil.byteToString(Arrays.copyOfRange(buf, 72 + incrmentbyte, 88 + incrmentbyte)));
						dims.setDim_value_start(MathUtil.getFloat(Arrays.copyOfRange(buf, 88 + incrmentbyte, 92 + incrmentbyte)));
						dims.setDim_value_increment(MathUtil.getFloat(Arrays.copyOfRange(buf, 92 + incrmentbyte, 96 + incrmentbyte)));
						dims.setDim_collect_num(MathUtil.dwordBytesToLong(Arrays.copyOfRange(buf, 96 + incrmentbyte, 100 + incrmentbyte)));
						dims.setDim_collect_maxnum(MathUtil.dwordBytesToLong(Arrays.copyOfRange(buf, 100 + incrmentbyte, 104 + incrmentbyte)));
						dims.setDim_sample_size(MathUtil.dwordBytesToLong(Arrays.copyOfRange(buf, 104 + incrmentbyte, 108 + incrmentbyte)));
						dims.setDim_data_type(MathUtil.byteToShort(Arrays.copyOfRange(buf, 108 + incrmentbyte, 110 + incrmentbyte)));
						dims.setDim_keep(MathUtil.byteToShort(Arrays.copyOfRange(buf, 110 + incrmentbyte, 112 + incrmentbyte)));
						dimslist.add(dims);
					}
					channelObj.setChannel_dimension_info(dimslist);
					log.info("通道:" + obj_name + "\r\n" + channelObj.toString());
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					dis.close();
					is.close();
				}
				return channelObj;
			}
		}
		return null;
	}
	@Override
	public List<String> getChannelCurve(File file, WisObjectEntry entry) throws Exception {
		WisChannelObject wisChannelObject = getWisChannelObject(file, entry);
		if (wisChannelObject != null)
			return getChannelCurve(file, wisChannelObject);
		return null;
	}
	@Override
	public List<String> getChannelCurve(File file, WisChannelObject channelObject) throws Exception {
		short dimension_num = channelObject.getChannel_dimension_num();
		List<WisChannelDimension> list = channelObject.getChannel_dimension_info();
		if (dimension_num == 1) {
			WisChannelDimension channelDim = list.get(0);
			FileInputStream is = new FileInputStream(file);
			DataInputStream dis = new DataInputStream(is);
			try {
				int offset = (int) channelObject.getChannel_data_offset();
				float dimension_value_start = channelDim.getDim_value_start();
				int block_num = channelObject.getChannel_block_num();
				long dimension_collect_num = channelDim.getDim_collect_num();
				float dimension_value_increment = channelDim.getDim_value_increment();
				int count = 0;
				double depth = dimension_value_start;
				byte[] buf1 = new byte[offset];
				dis.read(buf1, 0, offset);
				byte[] itembuf = new byte[280];
				dis.read(itembuf, 0, 280);
				List<String> values = new ArrayList<String>();
				for (int i = 0; i < block_num; i++) {
					byte[] temp = new byte[1024];
					dis.read(temp, 0, 1024);
					for (int j = 0; j + 4 <= 1024; j += 4) {
						count++;
						if (count > dimension_collect_num)
							break;
						if (i + j > 0)
							depth += dimension_value_increment;
						String depth2 = (new BigDecimal(depth)).setScale(3, 4).toString();
						double val = MathUtil.getFloat(Arrays.copyOfRange(temp, j, j + 4));// 对每块信息每4位获取，确定深度处对应的具体数值
						values.add(depth2 + "\t" + String.valueOf(val));
					}
					if (count > dimension_collect_num)
						break;
				}
				return values;
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				dis.close();
				is.close();
			}
		}
		if (dimension_num == 2) {
			WisChannelDimension dim1 = list.get(0);
			WisChannelDimension dim2 = list.get(1);
			FileInputStream is = new FileInputStream(file);
			DataInputStream dis = new DataInputStream(is);
			try {
				int offset = (int) channelObject.getChannel_data_offset();
				float dim1_value_start = dim1.getDim_value_start();
				int dim1_collect_num = (int) dim1.getDim_collect_num();
				int dim2_collect_num = (int) dim1.getDim_collect_num();
				float dim1_value_increment = dim1.getDim_value_increment();
				int dim1_sample_size = (int) dim1.getDim_sample_size();
				int dim2_sample_size = (int) dim2.getDim_sample_size();
				double depth = dim1_value_start;
				int count = 0;
				byte[] buf1 = new byte[offset];
				dis.read(buf1, 0, offset);
				byte[] itembuf = new byte[1024];
				dis.read(itembuf, 0, 1024);
				List<String> values = new ArrayList<String>();
				for (int i = 0; i < dim1_collect_num; i++) {
					count = 0;
					if (i > 0) {
						depth += dim1_value_increment;
					}
					String depth2 = (new BigDecimal(depth)).setScale(3, 4).toString();
					byte[] databuff = new byte[dim1_sample_size];
					dis.read(databuff, 0, dim1_sample_size);
					StringBuffer sb = new StringBuffer();
					for (int j = 0; j + dim2_sample_size <= dim1_sample_size; j += dim2_sample_size) {
						count++;
						if (count > dim2_collect_num)
							break;
						float content = MathUtil.getFloat(Arrays.copyOfRange(databuff, j, j + dim2_sample_size));// 对每块一维信息根据二维的一个数据点所占的位数位获取该点的数据值，即确定深度处对应的具体数值
						sb.append(content).append("\t");
					}
					values.add(depth2 + "\t" + sb.toString());
				}
				return values;
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
	public List<WisChannelObject> listWisChannelObject(File file) throws Exception {
		List<WisObjectEntry> entries = entryService.listWisObjectEntry(file);
		if (entries != null && entries.size() > 0) {
			List<WisChannelObject> list = new ArrayList<WisChannelObject>();
			for (int i = 0; i < entries.size(); i++) {
				WisChannelObject wisChannelObject = getWisChannelObject(file, entries.get(i));
				if (wisChannelObject != null)
					list.add(wisChannelObject);
			}
			return list;
		}
		return null;
	}
	@Override
	public List<Map<String, List<String>>> listChannelCurves(File file) throws Exception {
		List<WisChannelObject> channels = listWisChannelObject(file);
		if (channels != null && channels.size() > 0) {
			List<Map<String, List<String>>> list = new ArrayList<Map<String, List<String>>>();
			Map<String, List<String>> map = new HashMap<String, List<String>>();
			for (int i = 0; i < channels.size(); i++) {
				log.info("开始解析曲线:" + channels.get(i).getChannel_alias());
				List<String> channleCurve = getChannelCurve(file, channels.get(i));
				map.put(channels.get(i).getChannel_name(), channleCurve);
				list.add(map);
			}
			return list;
		}
		return null;
	}
}
