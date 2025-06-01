package studiplayer.audio;
import java.util.Map;
import studiplayer.basic.TagReader;

public class TaggedFile extends SampledFile{
	// Attributes
	private String album = "";

	// Constructors
	
	public TaggedFile(String path){
		parsePathname(path);
		parseFilename(getFilename());
		
		readAndStoreTags();
	}
	
	
	public void readAndStoreTags() {
		String tempString = "";
		long tempDuration = 0;
		Map<String, Object> tagMap;
		try {
			tagMap = TagReader.readTags(getPathname());
		}
		catch (Exception e) {
			throw new NotPlayableException();
		}
		tempString = (String) tagMap.get("title");
		if (tempString != null){
			setTitle(tempString.trim());
		}
		
		tempString = (String) tagMap.get("author");
		if (tempString != null){
			setAuthor(tempString.trim());
		}
		
		tempString = (String) tagMap.get("album");
		if (tempString != null){
			album = (tempString).trim();
		}
		
		tempDuration = (long) tagMap.get("duration");
		setDuration(tempDuration);
	}
	
	// Getters
	public String getAlbum() {
		return album;
	}

	// toString() method
	@Override
	public String toString() {
		if (getAuthor().isEmpty()) {
			if(!getAlbum().isEmpty()) {
				return getTitle() + " - " + getAlbum() + " - " + formatDuration();
			}
			else {
				return getTitle() + " - " + formatDuration();
			}
		} 
		else if (getAlbum().isEmpty()){
			return getAuthor() + " - " + getTitle() + " - " + formatDuration();
		}
		else {
			return getAuthor() + " - " + getTitle() + " - " + getAlbum() + " - " + formatDuration();
		}
	}
}
