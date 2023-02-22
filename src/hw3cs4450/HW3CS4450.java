/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hw3cs4450;

import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/***************************************************************
* file: HW3CS4450.java
* author: Robert Delfin, Greg Kalaydjian, Hari Kifle
* Team: Lobsters
* class: CS 4450 – Computer Graphics
*
*
* assignment: program 3 Final Program Check Point 2
* date last modified: 10/26/2020
*
* 
* purpose: Display a cube in 3D space witch each face colored differently as well as navigate through the 
* environment using w,a,s,d keys as dwell as space bar and left shift keys to move in and out. Builds on check point 2
* to add chunking method and block type to create a 30x 30 environment.
* 
****************************************************************/ 
public class HW3CS4450 {

    /**
     * Is the main method that starts the program. Calls the Basic.java class
     */
    public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException
    {
        Basic basic = new Basic(); 
        basic.startDrawing();
    }
    
}
