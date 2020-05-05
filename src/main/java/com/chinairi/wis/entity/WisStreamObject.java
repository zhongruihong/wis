package com.chinairi.wis.entity;

import lombok.Data;

@Data
public class WisStreamObject {
	private String stream_name;
	
	private long stream_length;

	private String content;
}
