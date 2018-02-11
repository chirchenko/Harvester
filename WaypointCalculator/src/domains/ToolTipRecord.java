package domains;

public interface ToolTipRecord {
	default String getTooltip(){
		return this.toString();
	};
}
