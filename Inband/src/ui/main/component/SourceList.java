package ui.main.component;

import java.io.File;
import java.net.URL;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ui.dragndrop.handler.PanelTransferHandler;
import ui.frame.ComponentFrame;
import ui.loader.DragLoader;
import ui.loader.data.DragType;
import ui.loader.data.DragTypeContainer;

public class SourceList extends JList<JPanel> {
	private static final long serialVersionUID = 5934328328824127967L;
	private DragTypeContainer sourceContainer;
	public SourceList(){
		super();
		this.init();
		this.setTransferHandler(new PanelTransferHandler());
	}

	private void init(){
		try{
			URL url = DragType.class.getResource("Metadata-Firmware.xml"); 	
			this.sourceContainer = DragLoader.loadDragTypes(new File(url.toURI()));
		}catch(Exception e){
			e.printStackTrace();
		}
		DefaultListModel<JPanel> model = new DefaultListModel<JPanel>();
		for(DragType type : sourceContainer.getList()){
				JTextField componentField = new JTextField();
				ComponentFrame frame = new ComponentFrame(type.getName(),type.getDevName(), type.getType(),type.getDefaultEnabled(),componentField);
				DropTargetPanel.frameValues.put(type.getName(), componentField);
				model.addElement(frame);
		}
		this.setDragEnabled(true);
		this.setModel(model);
	}
}
