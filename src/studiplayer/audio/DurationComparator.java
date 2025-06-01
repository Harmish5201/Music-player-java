package studiplayer.audio;

import java.util.Comparator;

public class DurationComparator implements Comparator<AudioFile>{

	@Override
	public int compare(AudioFile a1, AudioFile a2) {
		if (a1 instanceof SampledFile && a2 instanceof SampledFile) {
			SampledFile o1 = (SampledFile) a1;
			SampledFile o2 = (SampledFile) a2;
			return Long.compare(o1.getDuration(), o2.getDuration());
		}
		return -1;
		
	}

}
