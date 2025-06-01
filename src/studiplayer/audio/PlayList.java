package studiplayer.audio;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.io.FileWriter;

public class PlayList implements Iterable<AudioFile>{
	// Attributes..
	private List<AudioFile> list = new LinkedList<>();
	private String search;
	private SortCriterion sortCriterion = SortCriterion.DEFAULT;
	private ControllablePlayListIterator i1;
	
	public PlayList() {
		// Do nothing..
	}
	public PlayList(String m3uPathname) {
		loadFromM3U(m3uPathname);
		i1 = new ControllablePlayListIterator(list, search, sortCriterion);
	}
	
	public void add(AudioFile file) {
		list.add(file);
		i1 = new ControllablePlayListIterator(list, search, sortCriterion);
	}
	
	public void remove(AudioFile file) {
		list.remove(file);
		i1 = new ControllablePlayListIterator(list, search, sortCriterion);
	}
	
	public int size() {
		return list.size();
	}
	
	public AudioFile currentAudioFile() {
		if (list.isEmpty()) {		// Did not use i1.list because if List = empty, then object i1 not created
			return null;
		}
		if (i1.hasNext()) {
			return i1.list.get(i1.currentPos);
		}
		System.out.println(i1.currentPos);
		return null;
	}

	public void nextSong() {
		try {
			if (!list.isEmpty()) {
				if ((i1.currentPos + 1) >= i1.list.size()) {
					i1.currentPos = 0;
				} else {
					i1.next();
				}
			}
		} catch (IndexOutOfBoundsException e) {
			i1.currentPos = 0;
		}
	}
	
	public void loadFromM3U(String pathname) {
		list.clear();
		File file = new File(pathname);
		if(file.canRead()) {
			try {
				Scanner scanner = new Scanner(file);
				while(scanner.hasNextLine()) {
					String Audiofile = scanner.nextLine().trim();
					if (!Audiofile.startsWith("#") && Audiofile != "") {
						File newFile = new File(Audiofile);
						if (newFile.canRead()) {
							add(AudioFileFactory.createAudioFile(Audiofile));
						}
					}
				}
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			throw new RuntimeException(pathname + "File does not exist");
		}
		//System.out.println(list);
	}
	
	public void saveAsM3U(String pathname) {
		try {
        	FileWriter file = new FileWriter(pathname);
        	for (AudioFile audioFile : list) {
                file.write(audioFile.getPathname());
                file.write("\n");
        	}
            file.close();
        }
            catch(Exception e) {
            	System.out.println("Error");
            	e.printStackTrace();
            }
	}
	
	public List<AudioFile> getList() {
		return list;
	}
	
	public SortCriterion getSortCriterion() {
		return sortCriterion;
	}

	public void setSortCriterion(SortCriterion sortCriterion) {
		this.sortCriterion = sortCriterion;
		i1 = new ControllablePlayListIterator(list, search, sortCriterion);
	}

	public String getSearch() {
		return search;
	}
	
	public void setSearch(String search) {
		this.search = search;
		i1 = new ControllablePlayListIterator(list, search, sortCriterion);
	}
	
	public void jumpToAudioFile(AudioFile file) {
		i1.currentPos = i1.list.indexOf(file);
	}

	@Override
	public Iterator<AudioFile> iterator() {
		i1 = new ControllablePlayListIterator(list, search, sortCriterion);
		return i1;
	}

	@Override
	public String toString() {
		return list.toString();
	}
	
	public void setCurrentPos(int Pos) {
		i1.currentPos = Pos;
	}
	
	public void decrementCurrentPos() {
		i1.currentPos -= 1;
	}

}
