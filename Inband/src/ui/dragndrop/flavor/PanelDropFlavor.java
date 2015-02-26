package ui.dragndrop.flavor;

import java.awt.datatransfer.DataFlavor;

import javax.swing.JPanel;

public class PanelDropFlavor extends DataFlavor{
	public PanelDropFlavor(){
		super(JPanel.class, "Panel");
	}
}
