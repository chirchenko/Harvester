package tools;

public enum Parameter {
	APP_LOGO_IMAGE      ("resource.image.logo", "img/logo.jpg"),
	APP_ICON_PATH       ("resource.image.icon", "img/icon64.png"),
	APP_BLANK_MAP       ("resource.image.emptymap", "img/blank.png"),
	APP_EXPORT_DIR      ("resource.dir.export", "export"),
	APP_SCRIPT_DIR      ("resource.dir.scripts", "scripts"),
	GRAPH_POINT_CAPTION ("graphics.point.captions", "true"),
	APP_RECREATE_DB     ("db.recreate", "false");

	private final String key;
	private final String defaultValue;

	Parameter(String key, String defaultValue) {
		this.key = key;
		this.defaultValue = defaultValue;
	}

	public String getKey() {
		return key;
	}

	public String getDefaultValue() {
		return defaultValue;
	}
}
