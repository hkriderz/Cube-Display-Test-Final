/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hw3cs4450;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.glu.GLU;

import static org.lwjgl.opengl.GL11.*;

import java.nio.FloatBuffer;
import java.util.Vector;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector3f;

public class Basic {
    CameraSettings cameraSettings = CameraSettings.getInstance();
    
    public static void initGL(DisplayMode displayMode) {
        glClearColor(0.529f, 0.808f, 0.922f, 1f);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        GLU.gluPerspective(100f, (float)displayMode.getWidth() / (float)displayMode.getHeight(), 0.1f, 300f);
        
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);
        
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        
        FloatBuffer lightPosition, whiteLight;
        lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(0.0f).put(0.0f).put(0.0f).put(1.0f).flip();
        whiteLight = BufferUtils.createFloatBuffer(4);
        whiteLight.put(1.0f).put(1.0f).put(1.0f).put(0.0f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition); 
        glLight(GL_LIGHT0, GL_SPECULAR, whiteLight);
        glLight(GL_LIGHT0, GL_DIFFUSE, whiteLight);
        glLight(GL_LIGHT0, GL_AMBIENT, whiteLight);
        glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);
        
        glMatrixMode(GL_MODELVIEW);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
    }

    //Creates the window
    public DisplayMode createWindow() throws Exception
    {
        //declarations
        //parameters: index 0 is width (x axis)
        //parameters: index 1 is height (y axis)
        int[] parameters = new int[1];
        final int bitsPerPixel = 32;

        //instantiations
        DisplaySettings displaySettings = new DisplaySettings();
        CameraSettings cameraSettings = new CameraSettings();
        
        //retrieve parameters (width x height)
        parameters = displaySettings.setParameters();
        cameraSettings.checkRandomization();
        
        
	Display.setFullscreen(false);
	DisplayMode d[] = Display.getAvailableDisplayModes();
        DisplayMode displayMode = null;
        //gives the size for the window 
	for (int i = 0; i < d.length; i++) 
        {
	    if (d[i].getWidth() == parameters[0]
		&& d[i].getHeight() == parameters[1]
		&& d[i].getBitsPerPixel() == bitsPerPixel) 
            {
		displayMode = d[i];
		break;
	    }
	}
        
	Display.setDisplayMode(displayMode);
	Display.setTitle("Final Project Final Checkpoint");
	Display.create();
        return displayMode;
    }
    
    public void startDrawing(){
               
        FPCameraController fp;
        DisplayMode displayMode = null;
        try {
            displayMode = createWindow();
            initGL(displayMode);
            fp = new FPCameraController(0f, 0f, 0f);
            fp.gameLoop();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}