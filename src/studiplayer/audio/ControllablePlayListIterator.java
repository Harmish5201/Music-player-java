package studiplayer.audio;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ControllablePlayListIterator implements Iterator<AudioFile>{
	List<AudioFile> list = new LinkedList<>();
	int currentPos = 0; 
	
	// Constructors
	public ControllablePlayListIterator(List<AudioFile> list){
		this.list = list;
		currentPos = 0;
	}
	
	public ControllablePlayListIterator(List<AudioFile> list, String search, SortCriterion sort) {
		currentPos = 0;
		if (search == null || search.isEmpty()) {
			for (AudioFile file : list) {
				this.list.add(file);  //adds all the files
			}
			
		}
		else {
			for (AudioFile af : list) {
				if (af instanceof WavFile) {
					WavFile file = (WavFile) af;	// Down casting
					if (file.getAuthor().contains(search) || file.getTitle().contains(search)) {
						this.list.add(file);
					}
				}
				else {
					TaggedFile file = (TaggedFile) af;	// Down casting
					if (file.getAuthor().contains(search) || file.getTitle().contains(search) || file.getAlbum().contains(search)) {
						this.list.add(file);
					}
				}
			}
			
		}
		
		// Sorting element
		if (sort == SortCriterion.AUTHOR) {
			this.list.sort(new AuthorComparator());
		}
		else if (sort == SortCriterion.TITLE) {
			Collections.sort(this.list, (a1, a2) -> a1.getTitle().compareTo(a2.getTitle()));   //lambda
		}
		else if (sort == SortCriterion.ALBUM) {
			this.list.sort(new AlbumComparator());
		}
		else if (sort == SortCriterion.DURATION) {
			this.list.sort(new DurationComparator());
		}
		else {
			// else : do nothing... (DEFAULT)	
		}
    }
	
	
	@Override
	public boolean hasNext() {
		if (currentPos < list.size()) {
			return true;
		}
		return false;
	}

	@Override
	public AudioFile next() {
		if (hasNext())
			return list.get(currentPos++);
		return null;
	}
	
	public AudioFile jumpToAudioFile(AudioFile file) {
		currentPos = list.indexOf(file);
		return list.get(currentPos++);
	}
	
	public List<AudioFile> getList() {
		return list;
	}
	
	

}
