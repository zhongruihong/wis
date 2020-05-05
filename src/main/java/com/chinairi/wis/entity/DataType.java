package com.chinairi.wis.entity;
@Deprecated
public enum DataType {
	INT(1,4),
	SHORT(2,2),
	LONG(3,4),
	FLOAT(4,4),
	DOUBLE(5,8),
	STRING(6,1),
	CHAR(7,1),
	UCHAR(8,1),
	USHORT(9,2),
	UINT(10,4),
	ULONG(11,4);

    private Integer id; 
    private Integer length; 
	private  DataType(Integer id,Integer length) {
		this.id=id;
		this.length=length;
	}
	
    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}


	public static Integer getLength(int id) {
    	DataType[] types = DataType.values();
		for (DataType type : types) {
			if (id == type.getId()) {
				return type.getLength();
			}
		}
		return null;
	}
	
	public static DataType getType(String typeName) {
		DataType[] types = DataType.values();
		for (DataType type : types) {
			if (type.name().equalsIgnoreCase(typeName)) {
				return type;
			}
		}
		return null;
	}
}
