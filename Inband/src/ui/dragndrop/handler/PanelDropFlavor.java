package ui.dragndrop.handler;

import java.awt.datatransfer.DataFlavor;

import javax.swing.JPanel;

public class PanelDropFlavor extends DataFlavor{
	public PanelDropFlavor(){
		super(JPanel.class, "Panel");
	}
}
