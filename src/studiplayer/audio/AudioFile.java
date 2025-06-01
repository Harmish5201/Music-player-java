package studiplayer.audio;
public abstract class AudioFile {
	// Attributes
	private String pathname = "", filename = "", author = "", title = "";	// NOTE : Some attributes are set from other classes/files as well. Setters are created.
	private String osName = System.getProperty("os.name").toLowerCase();
	
	// Abstract methods
	public abstract void play() throws NotPlayableException;
	public abstract void togglePause();
	public abstract void stop();
	public abstract String formatDuration();
	public abstract String formatPosition();
	
	// Getters
	public String getPathname() {
		return pathname;
	}

	public String getFilename() {
		return filename;
	}

	public String getAuthor() {
		return author;
	}

	public String getTitle() {
		return title;
	}
	
	// Setters : Currently used in only TaggedFile.java
	public void setAuthor(String author) {
		this.author = author;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	// toString
	@Override
	public String toString() {
		if (getAuthor().isEmpty()) {
			return title;
		} else {
			return author + " - " + title;
		}
	}
	
	// method() to check if windows
	public boolean IsWindows() {
		if (osName.contains("windows")) {
			return true;
		}
		return false;
	}
	
	// important method below, dont change
	public void parsePathname(String path) {
		if (IsWindows()) {
			path = path.replace("/", "\\");
		} else {
			path = path.replace("\\", "/");
			if (path.contains(":")) {
				path = path.replace(":", "");
				path = "/" + path;
			}
		}
		if (path.contains("/") || path.contains("\\")) {
			for (int i = 0; i < 10; i++) {
				path = path.replace("//", "/");
				path = path.replace("\\\\", "\\");
			}
			for (int i = path.length() - 1; i >= 0; i--) {
				if (path.charAt(i) == '/' || path.charAt(i) == '\\') {
					if (i == (path.length() - 1)) {
						filename = "";
						pathname = path;
						break;
					} else {
						filename = path.substring(i + 1);
						pathname = path;
						break;
					}
				}
			}
		} else {
			if (path.contains(".")) {
				pathname = filename = path;
			} else {
				pathname = filename = "";
			}
		}
		if (path.contains("-")) {
			path = path.replace(" ", "");
			path = path.replace("-", "");
			if (path == "") {
				filename = pathname = "-";
			}
		}
		pathname = pathname.trim();
		filename = filename.trim();
	}
	
	// important method below, dont change
	public void parseFilename(String f) {
		filename = f;

		if (filename.equals("-")) {
			author = "";
			title = "-";
		} else {
			filename = filename.trim();
			int ExtensionPos = filename.length();
			for (int i = filename.length() - 1; i >= 0; i--) {
				if (filename.charAt(i) == '.') {
					ExtensionPos = i;
					break;
				}
			}

			if (filename.contains(" - ")) {
				author = filename.substring(0, filename.indexOf(" - "));
				title = filename.substring(filename.indexOf(" - ") + 3, ExtensionPos);
			} else {
				if (filename.contains(".")) {
					author = "";
					title = filename.substring(0, ExtensionPos);
				}
				if (ExtensionPos == 0) {
					author = "";
					title = "";
				}
			}
			title = title.trim();
			author = author.trim();
			while (title.endsWith(".")) {
				title = title.substring(0, title.length() - 1);
			}
		}
	}

	// Constructors
	public AudioFile(String s) throws NotPlayableException{
		String path = s;
		parsePathname(path);
		parseFilename(filename);
	}

	public AudioFile() throws NotPlayableException{
		// Do nothing...
	}


}
