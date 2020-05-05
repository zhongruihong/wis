package com.chinairi.wis.entity;

import java.util.Date;

import lombok.Data;

@Data
public class WisHead {
	
	private String version;// 文件标识

	private short sys_type;// 机器类型

	private short max_obj_num;// 允许记录的最大对象数

	private short fact_obj_num;// 实际对象记录数

	private short block_size;// 块大小

	private long obj_entry_offset;// 入口记录从文件开始的偏移量

	private long obj_data_offset;// 数据记录从文件开始的偏移量

	private long file_size;// WIS文件字节数

	private Date file_create_time;// WIS文件产生时间

	private String file_desc;// 保留字节
	
}
