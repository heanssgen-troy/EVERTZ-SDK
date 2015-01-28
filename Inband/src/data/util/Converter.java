package data.util;

import java.nio.ByteBuffer;

public class Converter {

	public static byte convertIntToByte(int number){
		return (byte)number;
	}

	public static String convertLongToStandardHex(long number){
		String returnable = Long.toHexString(number);
		if(returnable.length() % 2 == 1){
			returnable = "0" + returnable;
		}
		if(number == 0){
			returnable = "00";
		}
		return returnable;
	}
	
	public static byte[] convertShortToByteArray(Integer assocProgram) {

		ByteBuffer buffer = ByteBuffer.allocate(2);
		buffer.putShort(Short.parseShort("" + assocProgram));

		return buffer.array();
	}
	
}
