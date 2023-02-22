package hw3cs4450;

import java.awt.Component;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.util.Random;
import java.sql.Time;
import java.io.IOException;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.Sys;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import java.nio.FloatBuffer;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.Sys;
import org.lwjgl.util.Point;

        
/**
 *Purpose: The class FP CameraControl is the first person controller
 */
public class FPCameraController{
    
    private Vector3f position = null;
    private Vector3f lPosition = null;
    private float yaw = 0f;
    private float pitch = 0f;
    private Vector3f camera;
    private final int size = 4;
    private Chunk[][] chunks; 
    CameraSettings cameraSettings = CameraSettings.getInstance();
    public boolean acceptRandomization;
    
    public FPCameraController(float x, float y, float z) { 
        //instantiate position Vector3f to the x y z params.
        position = new Vector3f(x, y, z);
        lPosition = new Vector3f(x,y,z);
        lPosition.x = 0f;
        lPosition.y = 200f;
        lPosition.z = 0f;
        
        acceptRandomization = cameraSettings.returnRandomizationSettings();     
        
        if (acceptRandomization == true)
        {
            float location[] = new float[3];
            location = randomizeCameraLocation();
            position.x = location[0];
            position.y = location[1];
            position.z = location[2];
            lPosition.x = location[0];
            lPosition.y = location[1];
            lPosition.z = location[2];
            
        }
        
        
        chunks = new Chunk[size][size];
        int half = size/2;
        int limit = size % 2 == 0 ? half : half + 1;
        for(int i = -half; i < limit; i++) {
            for (int j = -half; j < limit; j++) {
                chunks[i + half][j + half] = new Chunk(i * Chunk.CHUNK_SIZE * Chunk.CUBE_LENGTH, -100, j * Chunk.CHUNK_SIZE * Chunk.CUBE_LENGTH);
            }
        }
    }
    
    
    public float[] randomizeCameraLocation()
    {
        //declarations
        //index 0 = x axis
        //index 1 = y axis
        //index 2 = z axis
        float[] location = new float[3];
        int xLimit = 200;
        int yLimit = 200;
        int zLimit = 200;
        float xFinal;
        float yFinal;
        float zFinal;
        
        //instantiations
        Random random = new Random();
        
        //calculations
        xFinal = random.nextInt(xLimit);
        yFinal = random.nextInt(yLimit);
        zFinal = random.nextInt(zLimit);
       
        //import values into array for output
        location[0] = xFinal;
        location[1] = yFinal;
        location[2] = zFinal;    
        
        //output
        return location;
    }
   //increment the camera's current yaw rotation
    public void yaw(float amount) {
       //increment the yaw by the amount param
       yaw += amount;
   }
    //increment the camera's current yaw rotation
    public void pitch(float amount) {
        //increment the pitch by the amount param
        pitch -= amount;
    }
    //moves the camera forward relative to its current rotation (yaw)
    public void walkForward(float distance) {
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw));
        position.x -= xOffset;
        position.z += zOffset;
        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(lPosition.x-=xOffset).put(lPosition.y).put(lPosition.z+=zOffset).put(1.0f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
    }

    //moves the camera backward relative to its current rotation (yaw)
    public void walkBackwards(float distance) {
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw));
        position.x += xOffset;
        position.z -= zOffset;
        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(lPosition.x+=xOffset).put(lPosition.y).put(lPosition.z-=zOffset).put(1.0f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
    }

    //strafes the camera left relative to its current rotation (yaw)
    public void strafeLeft(float distance) {
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw-90));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw-90));
        position.x -= xOffset;
        position.z += zOffset;
        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(lPosition.x-=xOffset).put(lPosition.y).put(lPosition.z+=zOffset).put(1.0f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
    }

    //strafes the camera right relative to its current rotation (yaw)
    public void strafeRight(float distance) {
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw+90));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw+90));
        position.x -= xOffset;
        position.z += zOffset;
        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(lPosition.x-=xOffset).put(lPosition.y).put(lPosition.z+=zOffset).put(1.0f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
    }
    //moves the camera up relative to its current rotation (yaw)
    public void moveUp(float distance) {
        position.y -= distance;
    }
    //moves the camera down
    public void moveDown(float distance) {
        position.y += distance;
    }

    //translates and rotate the matrix so that it looks through the camera
    //this does basically what gluLookAt() does
    public void lookThrough() {
        //roatate the pitch around the X axis
        glRotatef(pitch, 1.0f, 0.0f, 0.0f);
        //roatate the yaw around the Y axis
        glRotatef(yaw, 0.0f, 1.0f, 0.0f);
        //translate to the position vector's location
        glTranslatef(position.x, position.y, position.z);
        
        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(lPosition.x).put(lPosition.y).put(lPosition.z).put(1.0f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
    }
    
    // keep the game session until user presses ESC
    public void gameLoop() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        FPCameraController camera = new FPCameraController(0f, -65f, 0f);
        PlayMusic music = new PlayMusic();
        float dx = 0.0f;
        float dy = 0.0f;
        float dt = 0.0f; //length of frame
        float lastTime = 0.0f; // when the last frame was
        long time = 0;
        float mouseSensitivity = 0.09f;
        float movementSpeed = .35f;
        //hide the mouse
        Mouse.setGrabbed(true); 
        // keep looping till the display window is closed the ESC key is down
        while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
            time = Sys.getTime();
            lastTime = time;
            //distance in mouse movement
            //from the last getDX() call.
            dx = Mouse.getDX();
            //distance in mouse movement
            //from the last getDY() call.
            dy = Mouse.getDY();
            //when passing in the distance to move
            //we times the movementSpeed with dt this is a time scale
            //so if its a slow frame u move more then a fast frame
            //so on a slow computer you move just as fast as on a fast computer
            camera.yaw(dx * mouseSensitivity);
            camera.pitch(dy * mouseSensitivity);
            if (Keyboard.isKeyDown(Keyboard.KEY_W))//move forward          
                camera.walkForward(movementSpeed);           
            if (Keyboard.isKeyDown(Keyboard.KEY_S))//move backward            
                camera.walkBackwards(movementSpeed);           
            if (Keyboard.isKeyDown(Keyboard.KEY_A))//strafe left            
                camera.strafeLeft(movementSpeed);           
            if (Keyboard.isKeyDown(Keyboard.KEY_D))//strafe right            
                camera.strafeRight(movementSpeed);           
            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))//move up            
                camera.moveUp(movementSpeed);           
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) //move down  
                camera.moveDown(movementSpeed);               
            if (Keyboard.isKeyDown(Keyboard.KEY_E)) // gain mouse control for desktop
                Mouse.setGrabbed(false);     
            if (Keyboard.isKeyDown(Keyboard.KEY_R)) // return mouse control 
                Mouse.setGrabbed(true);  
            //Music Input Setting
            if(Keyboard.isKeyDown(Keyboard.KEY_M)) // play music
                music.play();
            if(Keyboard.isKeyDown(Keyboard.KEY_N)) // stop music
                music.pause();
            if(Keyboard.isKeyDown(Keyboard.KEY_X)) // restart music
                music.resetAudioStream();            
                
            //set the modelview matrix back to the identity
            glLoadIdentity();
            //look through the camera before you draw anything
            camera.lookThrough();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            //you would draw your scene here.
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    chunks[i][j].render();
                }
            }      
            //draw the buffer to the screen
            Display.update();
            Display.sync(60);
        }
        Display.destroy();
    }

    // draw the cube (outdated)
    private void render() {
        // Left
        glColor3f(0f, 1f, 0f);
        glBegin(GL_QUADS);
        glVertex3f(-1f, 1f, 1f);
        glVertex3f(-1f, 1f, -1f);
        glVertex3f(-1f, -1f, -1f);
        glVertex3f(-1f, -1f, 1f);
        glEnd();
        
        // Right
        glColor3f(0f, 1f, 1f);
        glBegin(GL_QUADS);
        glVertex3f(1f, 1f, -1f);
        glVertex3f(1f, 1f, 1f);
        glVertex3f(1f, -1f, 1f);
        glVertex3f(1f, -1f, -1f);
        glEnd();
        
        // Top
        glColor3f(1f, 0f, 1f);
        glBegin(GL_QUADS);
        glVertex3f(1f, 1f, -1f);
        glVertex3f(-1f, 1f, -1f);
        glVertex3f(-1f, 1f, 1f);
        glVertex3f(1f, 1f, 1f);
        glEnd();
        
        // Bottom
        glColor3f(1f, 1f, 0f);
        glBegin(GL_QUADS);
        glVertex3f(1f, -1f, 1f);
        glVertex3f(-1f, -1f, 1f);
        glVertex3f(-1f, -1f, -1f);
        glVertex3f(1f, -1f, -1f);
        glEnd();
        
        // Front
        glColor3f(1f, 0f, 0f);
        glBegin(GL_QUADS);
        glVertex3f(1f, 1f, 1f);
        glVertex3f(-1f, 1f, 1f);
        glVertex3f(-1f, -1f, 1f);
        glVertex3f(1f, -1f, 1f);
        glEnd();
        
        // Back
        glColor3f(0f, 0f, 1f);
        glBegin(GL_QUADS);
        glVertex3f(1f, -1f, -1f);
        glVertex3f(-1f, -1f, -1f);
        glVertex3f(-1f, 1f, -1f);
        glVertex3f(1f, 1f, -1f);
        glEnd();   
    }
}