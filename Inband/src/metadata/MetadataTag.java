package metadata;

import java.text.DecimalFormat;
import java.util.HashMap;

public final class MetadataTag {

	private HashMap<MetadataTag.Metadata, Object> metadataValues = new HashMap<MetadataTag.Metadata, Object>();
	public MetadataTag(){
		this.init();
	}
	private void init() {
		for (MetadataTag.Metadata tag : Metadata.values()) {
			metadataValues.put(tag, null);
		}
	}
	public MetadataTag(String name, Object[] value){
		this.init();
	}
	public void putValue(MetadataTag.Metadata tag, Object[] value) {
		metadataValues.put(tag, value);
	}
	public Object getValue(MetadataTag.Metadata tag){
		return metadataValues.get(tag);
	}
	public void setTimecode(String hour, String minute, String second) {
		StringBuffer timecodeString = new StringBuffer();

		if (hour.length() > 2 || Integer.parseInt(hour) < 0
				|| Integer.parseInt(hour) > 23) {
			return;
		}
		if (minute.length() > 2 || Integer.parseInt(minute) < 0
				|| Integer.parseInt(minute) > 59) {
			return;
		}
		if (second.length() > 5 || Double.parseDouble(second) < 0
				|| Double.parseDouble(second) > 59.99) {
			return;
		}

		hour = new DecimalFormat("00").format(Double.parseDouble(hour));
		minute = new DecimalFormat("00").format(Double.parseDouble(minute));
		second = new DecimalFormat("00.00").format(Double.parseDouble(second));

		timecodeString.append("" + hour + ":");
		timecodeString.append("" + minute + ":");
		timecodeString.append("" + second);

		putValue(MetadataTag.Metadata.TIMECODE, new Object[] { timecodeString });
	}

	public static void main(String[] args) {
		MetadataTag tag = new MetadataTag();
		tag.setTimecode("2", "44", "59.99");
		
	}

	public enum Metadata {
		HEADER_VERSION,
		HEADER_SIZE,
		PROGRAM_REFERENCE,
		ASSOCIATION_TYPE,
		ASSOCIATION_SOURCE,
		ASSOCIATION_PROGRAM,
		ASSOCIATION_PID,
		INJECT_TIME,
		PAYLOAD_LENGTH,
		PTS_MODE,
		PTS_OFFSET,
		RESERVED,
		TIMECODE
	}

}