/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hw3cs4450;
import java.io.File; 
import java.io.IOException; 
import java.util.Scanner; 
import javax.sound.sampled.AudioInputStream; 
import javax.sound.sampled.AudioSystem; 
import javax.sound.sampled.Clip; 
import javax.sound.sampled.LineUnavailableException; 
import javax.sound.sampled.UnsupportedAudioFileException; 
/**
 *
 * @author Robert Delfin
 * Purpose: Class Play Music plays a wav file music during the scene.
 * It can start and mute the music.
 */
public class PlayMusic 
{
	// to store current position 
	Long currentFrame; 
	Clip clip; 
	// current status of clip 
	String status = "start"; 
	AudioInputStream audioInputStream; 	 
	// constructor to initialize streams and clip 
	public PlayMusic() throws UnsupportedAudioFileException, IOException, LineUnavailableException{ 
		
		// create AudioInputStream object 
		audioInputStream = AudioSystem.getAudioInputStream(new File("ChillingMusic.wav").getAbsoluteFile()); 	
		// create clip reference 
		clip = AudioSystem.getClip(); 
		// open audioInputStream to the clip 
		clip.open(audioInputStream); 
		//clip.loop(Clip.LOOP_CONTINUOUSLY); 
	}
        
	// Method to play the audio 
	public void play() throws UnsupportedAudioFileException, IOException, LineUnavailableException
        { 
            //start the clip	
            clip.start(); 
            status = "play";
            System.out.println("Audio: ON");
	} 
        
	// Method to pause the audio 
	public void pause()
        {   
            this.currentFrame = this.clip.getMicrosecondPosition(); 
            clip.stop(); 	
            status = "paused";
            System.out.println("Audio: OFF");
	} 		
        
	// Method to reset audio stream 
        public void resetAudioStream() throws UnsupportedAudioFileException, IOException, LineUnavailableException{ 
            audioInputStream = AudioSystem.getAudioInputStream(new File("ChillingMusic.wav").getAbsoluteFile());
            clip.close();
            clip.open(audioInputStream); 
            clip.loop(Clip.LOOP_CONTINUOUSLY); 
	} 
}
