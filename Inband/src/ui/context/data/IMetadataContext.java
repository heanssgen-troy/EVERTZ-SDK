package ui.context.data;

import java.io.File;
import java.util.List;

public interface IMetadataContext {
	public List<ContextType> getContextTypes();
	public void loadContext(File filepath);
	public void saveContext(File filepath);
}
