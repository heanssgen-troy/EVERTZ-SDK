package ui.dragndrop.handler;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

public class PanelTransferHandler extends TransferHandler {
	/**
	 * 
	 */
	private static final long serialVersionUID = -612996439047801978L;

	public int getSourceActions(JComponent c) {
		return COPY_OR_MOVE;
	}

	public Transferable createTransferable(JComponent c) {
		JList<?> model = ((JList<?>) c);
		return (Transferable) model.getSelectedValue();
	}

	public void exportDone(JComponent c, Transferable t, int action) {
		if (action == TransferHandler.COPY) {
			DefaultListModel<?> model = (DefaultListModel<?>) ((JList<?>) c)
					.getModel();
			try {
				model.removeElement(t.getTransferData(new PanelDropFlavor()));
			} catch (UnsupportedFlavorException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public DataFlavor[] getDataFlavors() {
		return new DataFlavor[] { new PanelDropFlavor() };
	}

	public int getSourceDropActions() {
		return TransferHandler.COPY_OR_MOVE;
	}

	public boolean canImport(TransferHandler.TransferSupport support) {
		return false;
	}

	public boolean importData(TransferHandler.TransferSupport support) {
		return false;
	}
}
