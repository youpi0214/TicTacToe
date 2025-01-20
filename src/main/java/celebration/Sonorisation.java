package celebration;

public enum Sonorisation {
	MOTTO("/clips/Motto.mp3");

	String path;

	private Sonorisation(String pPath) {
		path = pPath;
	}

	public String getPath() {
		return path;
	}

	public String toString() {
		return getPath();
	}

}
