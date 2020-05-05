package com.chinairi.wis.entity;

import java.util.Date;
import lombok.Data;
/**
 * 对象入口
 * @author zrh
 *
 */
@Data
public class WisObjectEntry {
	private String obj_name;// 对象名称
	
	private long obj_status;// 对象状态

	private short obj_primary_attribute;// 主属性

	private short obj_sub_attribute;// 子属性

	private long obj_data_offset;// 对象数据体从文件开始处的偏移量

	private long obj_block_num;// 对象数据体占用磁盘块数

	private Date obj_create_time;// 对象产生时间

	private Date obj_update_time;// 对象最近修改时间

	private String obj_desc;// 保留字节
}
