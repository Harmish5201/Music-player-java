package studiplayer.audio;

import static org.junit.Assert.fail;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import studiplayer.basic.BasicPlayer;

public class Zmain {
	public static void main(String[] args) throws MalformedURLException, InterruptedException {
		TaggedFile f1 = new TaggedFile("audiofiles\\Haydn - Symphonie # 96 Motiv.ogg");
		new Thread() {
            public void run() {
                try {
                    f1.play();
                } catch (NotPlayableException e) {
                    fail("Cannot play " + f1 + " " + e);
                }
            }
        }.start();
        Thread.sleep(1000);
        f1.stop();
	}
}
