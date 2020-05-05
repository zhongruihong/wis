package com.chinairi.wis.entity;

import lombok.Data;

@Data
public class WisChannelDimension {
	private String dim_name;// 名称

	private String dim_unit;// 单位

	private String dim_alias;// 别名

	private float dim_value_start;// 起始值

	private float dim_value_increment;// 采集或者计算增量

	private long dim_collect_num;// 数据采样点数

	private long dim_collect_maxnum;// 数据采样最大点数

	private long dim_sample_size;// 该维上每一采样点所占用字节数

	private short dim_data_type;// 数据类型

	private short dim_keep;// 保留字节
}
