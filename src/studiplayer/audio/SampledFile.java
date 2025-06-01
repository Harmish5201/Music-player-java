package studiplayer.audio;
import studiplayer.basic.BasicPlayer;

public abstract class SampledFile extends AudioFile {
	// Attributes
	private long duration;
	
	// Getter & Setter for Duration
	public long getDuration() {
		formatDuration();
		return duration;
	}

	public void setDuration(long d) {
		duration = d;
	}
	
	
	// Constructors
	public SampledFile(){
		// Do nothing...
	}
	
	public SampledFile(String path) {
		parsePathname(path);
		parseFilename(getFilename());
	}
	
	// Public overrided methods below until next comment : play(), togglePause(), stop(), formatDuration(), formatPosition();
	
	@Override
	public void play() {
		try {
			BasicPlayer.play(getPathname());
		}
		catch (Exception e) {
			throw new NotPlayableException();
		}
	}
	
	@Override
	public void togglePause() {
		BasicPlayer.togglePause();
	}
	
	@Override
	public void stop() {
		BasicPlayer.stop();
	}
	
	@Override
	public String formatDuration() {
		return timeFormatter(duration);
	}
	
	@Override
	public String formatPosition() {
		return timeFormatter(BasicPlayer.getPosition());
	}
	
	
	// Used by methods formatDuration() & formatPosition()
	public static String timeFormatter(long time) {
		long min = 0, sec = 0;
		if (time > 0) {
			sec = time / 1000000;
			while(sec>59 && min <= 100) {
				sec = sec - 60;
				min = min + 1;
			}
		}
		if (min > 99 || time < 0) {
			throw new RuntimeException();
		}
		String formattedTime = String.format("%02d", min) + ":" + String.format("%02d", sec);
		return formattedTime;
	}
}


