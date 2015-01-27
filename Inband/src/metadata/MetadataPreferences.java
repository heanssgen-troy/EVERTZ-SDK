package metadata;

public class MetadataPreferences {
	public boolean isUseTimecode() {
		return useTimecode;
	}
	public void setUseTimecode(boolean useTimecode) {
		this.useTimecode = useTimecode;
	}
	public boolean isApplyLocal() {
		return applyLocal;
	}
	public void setApplyLocal(boolean applyLocal) {
		this.applyLocal = applyLocal;
	}
	public boolean isPassThrough() {
		return passThrough;
	}
	public void setPassThrough(boolean passThrough) {
		this.passThrough = passThrough;
	}
	
	private boolean useTimecode = false;
	private boolean applyLocal = false;
	private boolean passThrough = true;
	private boolean setMandatory = false;
	public boolean isSetMandatory() {
		return setMandatory;
	}
	public void setSetMandatory(boolean setMandatory) {
		this.setMandatory = setMandatory;
	}
}
