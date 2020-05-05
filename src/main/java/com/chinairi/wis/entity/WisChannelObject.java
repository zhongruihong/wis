package com.chinairi.wis.entity;

import java.util.List;

import lombok.Data;

@Data
public class WisChannelObject {
	private int channel_data_offset;

	private int channel_block_num;
	
	private String channel_name;

	private String channel_unit;// 单位

	private String channel_alias;// 别名

	private String channel_unit_alias;// 单位别名

	private short channel_data_type;// 数据类型

	private short channel_data_type_length;// 数据类型长度

	private float channel_minus;// 最小值

	private float channel_maximum;// 最大值

	private short channel_keep;// 保留字节

	private short channel_dimension_num;// 维数量

	private List<WisChannelDimension> channel_dimension_info;// 维信息
}
