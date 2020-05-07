package com.chinairi.wis.entity;

import lombok.Data;

@Data
public class WisTableField {
	private String filed_name;//字段名
	private int filed_type;//字段类型
	private long filed_length;
	private String keep;
}
