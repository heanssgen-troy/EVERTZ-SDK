package ui.frame;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class DashedGraphicsFrame extends JPanel {
	private static final long serialVersionUID = -8814235014347382288L;
	public DashedGraphicsFrame(){
		super();
		this.setBorder(BorderFactory.createDashedBorder(Color.GRAY,10.0f, 4.5f));
	}
}
