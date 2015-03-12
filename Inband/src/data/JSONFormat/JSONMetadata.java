package data.JSONFormat;

import java.nio.ByteBuffer;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import data.JSONFormat.format.Format;

public class JSONMetadata {
	private JSONObject data = new JSONObject();
	private String name;
	private HashMap<String, Boolean[]> objectParameters = new HashMap<String, Boolean[]>();
	private String timeCode;
	private boolean useJSONFormat = true;
	private Format format;

	public JSONMetadata(String name) {
		this.name = name;

	}

	public String getName() {
		return this.name;
	}
	public void setName(String name){
		this.name = name;
	}
	public void setTimeCode(String s) {
		this.timeCode = s;
		try {
			this.data.put(name, data.get(name));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void flagUseJSONFormat(boolean useFormat) {
		this.useJSONFormat = useFormat;
	}

	public void setNonJSONFormat(Format f) {
		this.format = f;
	}

	public void flagIsPassThrough(String name, boolean isPassThrough) {
		this.objectParameters.get(name)[0] = isPassThrough;
		try {
			this.putValue(name, data.get(name));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void flagIsApplyLocal(String name, boolean isApplyLocal) {
		this.objectParameters.get(name)[1] = isApplyLocal;
		try {
			this.putValue(name, data.get(name));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void flagIsUseTimecode(String name, boolean isUseTimecode) {
		this.objectParameters.get(name)[2] = isUseTimecode;
		try {
			this.putValue(name, data.get(name));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public JSONObject getData(String name) {
		try {
			return data.getJSONObject(name);
		} catch (JSONException e) {
			return data;
		}
	}

	public void putValue(String name, Object value) {
		StringBuffer buffer = new StringBuffer();
		if(data.isNull(name)){
			objectParameters.put(name, new Boolean[]{false,false,false});
		}
		try {
			if (value == null) {
				data.put(name, JSONObject.NULL);
				
			} else if (value.getClass().isArray()) {
				if (!(value instanceof String[])) {
					throw new JSONException(
							"Cannot convert object arrays to JSON data");
				}
				JSONArray array = new JSONArray();

				String[] arrayValues = (String[]) value;
				for (String jsonValues : arrayValues) {
					array.put(jsonValues);
				}
				if (this.objectParameters.get(name)[0]) {
					buffer.append("s");
				}
				if (this.objectParameters.get(name)[1]) {
					buffer.append("p");
				}

				if (buffer.length() > 0) {
					array.put(buffer.toString());
				}
				if (this.objectParameters.get(name)[2]) {
					array.put(timeCode);
				}
				data.put(name, array);
			} else {
				if(this.objectParameters.get(name)[0] || this.objectParameters.get(name)[1] || this.objectParameters.get(name)[2]){
					JSONArray array = new JSONArray();
					array.put(value);
					if (this.objectParameters.get(name)[0]) {
						buffer.append("s");
					}
					if (this.objectParameters.get(name)[1]) {
						buffer.append("p");
					}
					array.put(buffer.toString());
					data.put(name, array);
				}else{
					data.put(name, value);
				}
				
			}
		} catch (JSONException e) {

		}
	}

	public byte[] JSONtoData() {
		ByteBuffer buffer = null;
		if (this.useJSONFormat) {
			final String JSONBuffer = data.toString();
			buffer = ByteBuffer.allocate(JSONBuffer.length() + name.length());
			for(char c : this.name.toCharArray()){
				buffer.put((byte)c);
			}
			for (int i = 0; i < JSONBuffer.length(); i++) {
				buffer.put((byte)JSONBuffer.charAt(i));
			}
		} else {
			buffer = JSONToValueData(buffer);
		}
		return buffer.array();
	}

	private ByteBuffer JSONToValueData(ByteBuffer buffer) {
		return format.format(this);
	}
}
