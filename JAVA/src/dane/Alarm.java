package dane;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.*;
import sun.audio.*;

/**
 * this class implements alarm sound, reading sound from a external file. Class is opening and handling stream to audio file.  
 * @author Mateusz Keller
 *
 */

public class Alarm { //implements java.io.Serializable
	
	/**
	 * before Time when alarm should be displayed
	 */
	private LocalDateTime before;
	/**
	 * name of an audio file containing sound to be displayed
	 */
	private String sound; //"alarm1.wav"
	/**
	 * stream handling audio data 
	 */
	private ContinuousAudioDataStream loop;

	
	/**
	 * 
	 * @param before constructs alarm with given display date. File name for the sound field is given by Singleton instance, which passes it from program arguments.
	 */
	public Alarm(LocalDateTime before) {
		this(before, AppParameters.getInstance().getSound());
	}
	/**
	 * constructs alarm object with given display date and audio file name
	 * @param before time, when alarm is displayed
	 * @param sound name of the audio file to play
	 */
	public Alarm(LocalDateTime before, String sound)
	{
		this.before = before;
		this.sound = AppParameters.getInstance().getSound();
		try 
		{
			AudioStream audio = new AudioStream(new FileInputStream(sound));
			AudioData audioD = audio.getData();
			loop = new ContinuousAudioDataStream(audioD); 
			
		} catch (FileNotFoundException e) { e.printStackTrace();}
		  catch (IOException e) { e.printStackTrace(); }
	}
	 
	/**
	 * returns date and time when alarm will be displayed
	 * @return LocalDateTime
	 */
	public LocalDateTime getBefore() { return before; }
	
	/**
	 * sets date and time,when alarm will be displayed
	 * @param before time, when alarm is displayed
	 */
	public void setBefore(LocalDateTime before) { this.before = before; }
	
	/**
	 * returns name of the audio file containing sound to be displayed
	 * @return String
	 */
	public String getSound() { return sound; }
	
	/**
	 * sets name of the audio file containing sound to be displayed
	 * @param sound name of audio file containing sound to display
	 */
	public void setSound(String sound) { this.sound = sound; }

	/**
	 * returns string containing information about alarm date and time in readable format
	 * @return String
	 */
	public String toString(){
		String ret = "";
		DateTimeFormatter timeFormat;
		if(before.getHour() == 0)
		{
			timeFormat = DateTimeFormatter.ofPattern("m");
			ret += before.format(timeFormat) + " minut przed"; 
		}
		else
		{
			timeFormat = DateTimeFormatter.ofPattern("H");
			ret += before.format(timeFormat) + " godzin przed";
		}	
		return ret;
	}
	
	/**
	 * opens stream with audio file and plays the sound in loop for 1500 ms
	 */
	public void playSound(){
		AudioPlayer.player.start(loop);
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		AudioPlayer.player.stop(loop);		
	}
	
	/**
	 * closes stream with audio file
	 */
	public void stopSound()
	{
		AudioPlayer.player.stop(loop);
	}

}
