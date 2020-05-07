package com.chinairi.wis.utils;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class MathUtil {

	public static short byteToShort(byte[] b) {
		if (b != null && b.length > 0) {
			short s = 0;
			short s0 = (short) (b[0] & 0xff);
			short s1 = (short) (b[1] & 0xff);
			s = (short) (s0 | (s1 << 8));
			return s;
		}
		return 0;
	}

	public static int byteToInt(byte[] b) {
		if (b != null && b.length > 0) {
			int i;
			i = (int) ((b[0] & 0xFF) | ((b[1] & 0xFF) << 8) | ((b[2] & 0xFF) << 16) | ((b[3] & 0xFF) << 24));
			return i;
		}
		return 0;
	}

	public static long dwordBytesToLong(byte[] b) {
		if (b != null && b.length > 0)
			return getLong(b, 0);
		return 0;
	}

	public static long getLong(byte[] b, int index) {
		int firstByte = 0xFF & b[index];
		int secondByte = 0xFF & b[index + 1];
		int thirdByte = 0xFF & b[index + 2];
		int fourthByte = 0xFF & b[index + 3];
		long unsignedLong = (firstByte | secondByte << 8 | thirdByte << 16 | fourthByte << 24) & 0xFFFFFFFFL;
		return unsignedLong;

	}

	public static float getFloat(byte[] b) {
		if (b != null && b.length > 0) {
			int accum = 0;
			for (int shiftBy = 0; shiftBy < 4; shiftBy++)
				accum |= (b[shiftBy] & 0xFF) << shiftBy * 8;
			return Float.intBitsToFloat(accum);
		}
		return 0;
	}

	public static double byteToDouble(byte[] b) {
		if (b != null && b.length > 0) {
			long m;
			m = b[0];
			m &= 0xff;
			m |= ((long) b[1] << 8);
			m &= 0xffff;
			m |= ((long) b[2] << 16);
			m &= 0xffffff;
			m |= ((long) b[3] << 24);
			m &= 0xffffffffl;
			m |= ((long) b[4] << 32);
			m &= 0xffffffffffl;
			m |= ((long) b[5] << 40);
			m &= 0xffffffffffffl;
			m |= ((long) b[6] << 48);
			m &= 0xffffffffffffffl;
			m |= ((long) b[7] << 56);
			return Double.longBitsToDouble(m);
		}
		return 0.0;
	}

	public static String byteToString(byte[] b) {
		if (b != null && b.length > 0) {
			try {
				return new String(b, "GBK");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static char byteToChar(byte[] b) {
		char c = 0;
		if (b != null && b.length > 0)
			c = (char) (((b[0] & 0xFF) << 8) | (b[1] & 0xFF));
		return c;
	}

	public static String byteToUChar(byte[] b) {
		// TODO Auto-generated method stub
		return null;
	}

	public static String byteToUShort(byte[] b) {
		// TODO Auto-generated method stub
		return null;
	}

	public static int byteToUInt(byte[] b) {
		int s = 0;
		if (b != null && b.length > 0) {
			DataInputStream dis = new DataInputStream(new ByteArrayInputStream(b));
			try {
				s = dis.readUnsignedShort();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return s;
	}

	public static long byteToULong(byte[] b) {
		if (b != null && b.length > 0) {
			int firstByte = (0x000000FF & ((int) b[0]));
			int secondByte = (0x000000FF & ((int) b[1]));
			int ToUnsignedInt = ((int) (firstByte << 8 | secondByte)) & 0xFFFFFFFF;
			return ToUnsignedInt;
		}
		return 0;
	}
}
