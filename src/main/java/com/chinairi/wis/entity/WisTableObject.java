package com.chinairi.wis.entity;

import java.util.List;

import lombok.Data;

@Data
public class WisTableObject {
	private int tab_data_offset;
	
	private int tab_row_length;
	
	private String tab_name;
	
	private long tab_record_num;//表的记录数。

	private long tab_filed_num;//表的字段数

	private List<WisTableField> filedList;//表字段信息
}
