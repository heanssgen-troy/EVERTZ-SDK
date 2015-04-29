package ui.context.dragndrop;

import java.awt.datatransfer.DataFlavor;

import ui.context.data.ContextType;

public class ContextFlavor extends DataFlavor {
	public ContextFlavor(){
		super(ContextType.class, "Context");
	}
}
