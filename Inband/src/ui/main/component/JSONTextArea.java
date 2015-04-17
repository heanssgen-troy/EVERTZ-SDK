package ui.main.component;

import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ui.frame.ComponentFrame;

public class JSONTextArea extends JTextArea {

	private static final long serialVersionUID = 1L;
	private DropTargetPanel targetPanel;
	private JSONObject metadata = new JSONObject();

	public JSONTextArea(DropTargetPanel panel) {
		this.targetPanel = panel;
		targetPanel.addContainerListener(new MetadataChangeListener());
		this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED), BorderFactory.createTitledBorder("Firmware Metadata")));
		this.setEditable(false);
	}

	public void recalculateMetadata() {
		StringBuffer buffer = new StringBuffer();
		if(JSONObject.getNames(metadata) != null){
			for(String s : JSONObject.getNames(metadata)){
				metadata.remove(s);
			}
		}
		for(JPanel p : targetPanel.getComponentFrames()){
			if(p instanceof ComponentFrame){
				ComponentFrame frame = (ComponentFrame) p;
				try {
					Object data = frame.calculateMetadata();
					metadata.put(frame.getDevName(), data);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
			buffer.append("$EVERTZFWIMG{ \n");
			if(JSONObject.getNames(metadata) != null)
			for(String s : JSONObject.getNames(metadata)){
				try {
					if(!(metadata.get(s) instanceof JSONArray)){
						buffer.append("    " +s +":\"" +metadata.get(s)+"\",\n");
					}else{
						buffer.append("    " +s +":" +metadata.get(s)+",\n");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			buffer.append("}");
			this.setText(buffer.toString());
	}
	
	private class MetadataChangeListener implements ContainerListener {
		@Override
		public void componentAdded(ContainerEvent arg0) {
			recalculateMetadata();
		}

		@Override
		public void componentRemoved(ContainerEvent arg0) {
			recalculateMetadata();
		}
	}
}
