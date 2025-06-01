package studiplayer.audio;

public class NotPlayableException extends RuntimeException{
	// Constructors..
	private String pathname = "", msg = "";
	private Throwable t;
	
	public NotPlayableException() {
		System.out.println("Default Constructor Error");
	}
	public NotPlayableException(String pathname, String msg) {
		this.msg = msg;
		this.pathname = pathname;
	}
	
	public NotPlayableException(String pathname, Throwable t) {
		this.t = t;
		this.pathname = pathname;
	}
	public NotPlayableException(String pathname, String msg, Throwable t) {
		this.msg = msg;
		this.pathname = pathname;
		this.t = t;
	}
	
	@Override
	public String toString() {
		return "studiplayer.audio.NotPlayableException" + this.pathname + " " + this.msg + this.t;
	}
	
	public Throwable getCause() {
		return new RuntimeException();
	}
}
