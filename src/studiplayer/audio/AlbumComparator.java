package studiplayer.audio;

import java.util.Comparator;

public class AlbumComparator implements Comparator<AudioFile>{

	@Override
	public int compare(AudioFile a1, AudioFile a2) {
		if (a1 instanceof TaggedFile && a2 instanceof TaggedFile) {
			TaggedFile o1 = (TaggedFile) a1;
			TaggedFile o2 = (TaggedFile) a2;
			return o1.getAlbum().compareTo(o2.getAlbum());
		}
		if (a1 instanceof TaggedFile && !(a2 instanceof TaggedFile)) {
			return 1;
		}
		if (!(a1 instanceof TaggedFile) && !(a2 instanceof TaggedFile)) {
			return 0;
		}
		return -1;
	}

}
