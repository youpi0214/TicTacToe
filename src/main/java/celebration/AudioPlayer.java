package celebration;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class AudioPlayer {

	private MediaPlayer mediaPlayer;
	private Media media;

	public AudioPlayer(Sonorisation son) {
		media = new Media(getClass().getResource(son.getPath()).toExternalForm());
		mediaPlayer = new MediaPlayer(media);
		mediaPlayer.setCycleCount(Integer.MAX_VALUE);
		mediaPlayer.setVolume(0.7);

	}

	public void stopPlaying() {
		mediaPlayer.stop();
	}

	public void startPlaying() {
		mediaPlayer.play();
	}

	public MediaPlayer getMedia() {
		return mediaPlayer;
	}

}
