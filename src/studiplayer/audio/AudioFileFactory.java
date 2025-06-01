package studiplayer.audio;
public class AudioFileFactory{
	public static AudioFile createAudioFile(String path) throws NotPlayableException{
		if(path.toLowerCase().endsWith(".wav")) {
			WavFile wf = new WavFile(path); 
			return wf;
		}
		else if(path.toLowerCase().endsWith(".mp3") || path.toLowerCase().endsWith(".ogg")){
			TaggedFile tf = new TaggedFile(path);
			return tf;
		}
		else {
			throw new NotPlayableException();
		}
	}
}
