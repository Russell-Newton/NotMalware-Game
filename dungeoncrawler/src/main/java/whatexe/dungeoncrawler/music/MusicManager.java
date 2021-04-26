package whatexe.dungeoncrawler.music;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MusicManager {

    private static MusicManager instance;

    private final Map<String, MediaPlayer> mediaPlayerMap;
    private final List<String> activeTracks;
    private boolean isInTest;

    private MusicManager() {
        mediaPlayerMap = new HashMap<>();
        activeTracks = new ArrayList<>();
    }

    public static MusicManager getInstance() {
        if (instance == null) {
            instance = new MusicManager();
        }
        return instance;
    }

    public MediaPlayer addTrack(String songName) {
        if (mediaPlayerMap.containsKey(songName)) {
            throw new RuntimeException(
                    String.format("MusicManager already contains the track '%s'!", songName));
        }
        MediaPlayer newPlayer = new MediaPlayer(
                new Media(getClass().getResource(songName + ".mp3").toString()));
        newPlayer.setCycleCount(Integer.MAX_VALUE);
        if (!isInTest) {
            mediaPlayerMap.put(songName, newPlayer);
        }
        return newPlayer;
    }

    public void removeTrack(String songName) {
        if (!isInTest) {
            if (!mediaPlayerMap.containsKey(songName)) {
                throw new RuntimeException(
                        String.format("MusicManager doesn't contain the track '%s'!", songName));
            }
            mediaPlayerMap.get(songName).dispose();
            mediaPlayerMap.remove(songName);
            activeTracks.remove(songName);
        }
    }

    public void playTrack(String songName) {
        if (!isInTest) {
            if (!mediaPlayerMap.containsKey(songName)) {
                throw new RuntimeException(
                        String.format("MusicManager doesn't contain the track '%s'!", songName));
            }
            mediaPlayerMap.get(songName).play();
            activeTracks.add(songName);
        }
    }

    public void pauseTrack(String songName) {
        if (!isInTest) {
            if (!mediaPlayerMap.containsKey(songName)) {
                throw new RuntimeException(
                        String.format("MusicManager doesn't contain the track '%s'!", songName));
            }
            mediaPlayerMap.get(songName).pause();
            mediaPlayerMap.get(songName).seek(
                    mediaPlayerMap.get(songName).getCurrentTime().subtract(Duration.millis(4.5)));
            activeTracks.remove(songName);
        }
    }

    public void stopTrack(String songName) {
        if (!isInTest) {
            if (!mediaPlayerMap.containsKey(songName)) {
                throw new RuntimeException(
                        String.format("MusicManager doesn't contain the track '%s'!", songName));
            }
            mediaPlayerMap.get(songName).stop();
            activeTracks.remove(songName);
        }
    }

    public void stopAllTracks() {
        for (String song : mediaPlayerMap.keySet()) {
            stopTrack(song);
        }
    }

    /**
     * Pauses songs that are currently playing. Reversed by {@link MusicManager#unpauseActive()}.
     */
    public void pauseActive() {
        for (String activeTrack : activeTracks) {
            mediaPlayerMap.get(activeTrack).pause();
            mediaPlayerMap.get(activeTrack).seek(
                    mediaPlayerMap.get(activeTrack).getCurrentTime().subtract(Duration.millis(5)));
        }
    }

    /**
     * Resumes play of songs paused by {@link MusicManager#pauseActive()}.
     */
    public void unpauseActive() {
        for (String activeTrack : activeTracks) {
            mediaPlayerMap.get(activeTrack).play();
        }
    }

    public void seek(String songName, Duration time) {
        if (!isInTest) {
            if (!mediaPlayerMap.containsKey(songName)) {
                throw new RuntimeException(
                        String.format("MusicManager doesn't contain the track '%s'!", songName));
            }
            mediaPlayerMap.get(songName).seek(time);
        }
    }

    public Duration getCurrentTime(String songName) {
        if (!mediaPlayerMap.containsKey(songName)) {
            throw new RuntimeException(
                    String.format("MusicManager doesn't contain the track '%s'!", songName));
        }
        return mediaPlayerMap.get(songName).getCurrentTime();
    }

    public Map<String, MediaPlayer> getMediaPlayerMap() {
        return mediaPlayerMap;
    }

    public void setInTest(boolean inTest) {
        isInTest = inTest;
    }
}
