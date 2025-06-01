package studiplayer.audio;
import java.io.File;
import studiplayer.basic.WavParamReader;

public class WavFile extends SampledFile {

	// Constructors
	
	public WavFile(String path) {
		parsePathname(path);
		parseFilename(getFilename());
		// Checks if the File exists in path name or not!
		File file = new File(getPathname());
		if (!file.canRead()) {
			throw new NotPlayableException();
		}
		readAndSetDurationFromFile();
	}

	
	// methods() below : readAndSetDurationFromFile(), computeDuration(long, float);
	
	public void readAndSetDurationFromFile() {
		// Attributes
		float frameRate;
		long numberOfFrames;
		try {
		WavParamReader.readParams(getPathname());
		}
		catch(Exception e) {
			throw new NotPlayableException();
		}
		frameRate = WavParamReader.getFrameRate();
		numberOfFrames = WavParamReader.getNumberOfFrames();
		setDuration(computeDuration(numberOfFrames, frameRate));
	}
	
	public static long computeDuration(long numberOfFrames, float frameRate) {
		return ((long) ((numberOfFrames/frameRate) * (1000000)));
	}
	
	//toString() method
	@Override
	public String toString() {
		if (getAuthor().isEmpty()) {
			return getTitle() + " - " + formatDuration();
		} else {
			return getAuthor() + " - " + getTitle() + " - " + formatDuration();
		}
	}
}


