/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hw3cs4450;

import java.nio.FloatBuffer;
import java.util.Random;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Chunk{
    
    
    static final int CHUNK_SIZE = 32;
    static final int CUBE_LENGTH = 2;
    static final double PERSISTANCE = 0.3;
    static final int SEED = new Random().nextInt();
    private Block[][][] Blocks;
    private int vBOVertexHandle;
    private int vBOColorHandle;
    private int vBOTextureHandle;
    private Texture texture;
    private double[][] heightMap;
    private int StartX;
    private int StartY;
    private int StartZ;
    
    public void render(){
        glPushMatrix();
        glBindBuffer(GL_ARRAY_BUFFER,vBOVertexHandle);
        glVertexPointer(3, GL_FLOAT,0,0L);
        glBindBuffer(GL_ARRAY_BUFFER,vBOColorHandle);
        glColorPointer(3, GL_FLOAT,0,0L);
        glBindBuffer(GL_ARRAY_BUFFER, vBOTextureHandle);
        glBindTexture(GL_TEXTURE_2D,1);
        glTexCoordPointer(2,GL_FLOAT,0,0L);
        glDrawArrays(GL_QUADS, 0, CHUNK_SIZE*CHUNK_SIZE*CHUNK_SIZE*24);
        glPopMatrix();
    }
    
    //Has a simplex method
    public void rebuildMesh(float startX, float startY, float startZ) {
        vBOColorHandle = glGenBuffers();
        vBOVertexHandle = glGenBuffers();
        vBOTextureHandle = glGenBuffers();
        FloatBuffer VertexPositionData = BufferUtils.createFloatBuffer((CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
        FloatBuffer VertexColorData = BufferUtils.createFloatBuffer((CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
        FloatBuffer VertexTextureData = BufferUtils.createFloatBuffer((CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 8);
        for(float x = 0; x < CHUNK_SIZE; x++) {
            for(float z = 0; z < CHUNK_SIZE; z++) {
                for(float y = 0; y < CHUNK_SIZE; y++) {
                    if(Blocks[(int)x][(int)y][(int)z] == null)
                        continue;
                    //VertexPositionData.put(createCube((float)(startX + x * CUBE_LENGTH), (float)(y * CUBE_LENGTH + (int)(CHUNK_SIZE * .8)), (float)(startZ + z * CUBE_LENGTH)));
                    VertexPositionData.put(createCube((float) (startX + x * CUBE_LENGTH) + CUBE_LENGTH / 2, (float) (y * CUBE_LENGTH) + CUBE_LENGTH / 2, (float) (startZ + z * CUBE_LENGTH) + CUBE_LENGTH));
                    VertexColorData.put(createCubeVertexCol(getCubeColor(Blocks[(int)x][(int)y][(int)z])));
                    VertexTextureData.put(createTexCube(0f, 0f, Blocks[(int)x][(int)y][(int)z]));
                }
            }
        }
        VertexTextureData.flip();
        VertexColorData.flip();
        VertexPositionData.flip();
        //binds the texture
        glBindBuffer(GL_ARRAY_BUFFER, vBOTextureHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexTextureData,GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER,0);
        //binds the vertex
        glBindBuffer(GL_ARRAY_BUFFER, vBOVertexHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexPositionData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER,0);
        //binds the colors
        glBindBuffer(GL_ARRAY_BUFFER, vBOColorHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexColorData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }
    private float[] createCubeVertexCol(float[] CubeColorArray){
        float[] cubeColors = new float[CubeColorArray.length *4 *6];
        for(int i =0; i < cubeColors.length; i++){
            cubeColors[i] = CubeColorArray[i%CubeColorArray.length];
        }
        return cubeColors;
    }
    public static float[] createCube(float x, float y, float z){
        int offset = CUBE_LENGTH/2;
        
        return new float[]{
            //Top Quad
            x + offset, y + offset, z,
            x - offset, y + offset, z,
            x - offset, y + offset, z - CUBE_LENGTH,
            x + offset, y + offset, z - CUBE_LENGTH,
            //Bottom Quad
            x + offset, y - offset, z - CUBE_LENGTH,
            x - offset, y - offset, z - CUBE_LENGTH,
            x - offset, y - offset, z,
            x + offset, y - offset, z,
            //Front Quad
            x + offset, y + offset, z - CUBE_LENGTH,
            x - offset, y + offset, z - CUBE_LENGTH,
            x - offset, y - offset, z - CUBE_LENGTH,
            x + offset, y - offset, z - CUBE_LENGTH,
            //Back Quad
            x + offset, y - offset, z,
            x - offset, y - offset, z,
            x - offset, y + offset, z,
            x + offset, y + offset, z,
            //Left Quad
            x - offset, y + offset, z -CUBE_LENGTH,
            x - offset, y + offset, z,
            x - offset, y - offset, z,
            x - offset, y - offset, z - CUBE_LENGTH,
            //Right Quad
            x + offset, y + offset, z,
            x + offset, y + offset, z - CUBE_LENGTH,
            x + offset, y - offset, z - CUBE_LENGTH,
            x + offset, y - offset, z};
    }
    private float[] getCubeColor(Block block)
    {
        return new float[] {1f,1f,1f};
    }

    public Chunk (int startX, int startY, int startZ){
        try {
            texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("terrain.png"));
        } catch(Exception e) {
        }
        
        vBOColorHandle = glGenBuffers();
        vBOVertexHandle = glGenBuffers();
        vBOTextureHandle = glGenBuffers();
        StartX = startX;
        StartY = startY;
        StartZ = startZ;
        
        Random r = new Random(SEED);
        generateHeightMap();
        Blocks = new Block[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE];
        int fillLevel = CHUNK_SIZE;
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int z = 0; z < CHUNK_SIZE; z++) {
                int height = (int) heightMap[x][z];
                if (height < fillLevel) { //update min height(fill level)
                    fillLevel = height;
                }
                for (int y = 0; y < CHUNK_SIZE; y++) {
                    if (y > heightMap[x][z]) {
                        Blocks[x][y][z] = null;
                    } else if (y == 0) { //bottom level bedrock
                        Blocks[x][y][z] = new Block(Block.BlockType.bedrock);
                    } else if (y < CHUNK_SIZE / 6) { // stone level
                        Blocks[x][y][z] = new Block(Block.BlockType.stone);
                    } else if (y == height) { 
                        Blocks[x][y][z] = new Block(Block.BlockType.grass);           
                    } else { //lower than top, higher than stone (dirt)
                        Blocks[x][y][z] = new Block(Block.BlockType.dirt);             
                    }
                }
            }
        }
        
        fillLevel++;
        for (int i = 0; i < CHUNK_SIZE; i++) {
            if(Blocks[i][fillLevel][0] == null) {
                Blocks[i][fillLevel][0] = new Block(Block.BlockType.grass);
                Blocks[i][fillLevel - 1][0] = new Block(Block.BlockType.dirt);
            }
            if(Blocks[i][fillLevel][CHUNK_SIZE - 1] == null) {
                Blocks[i][fillLevel][CHUNK_SIZE - 1] = new Block(Block.BlockType.grass);
                Blocks[i][fillLevel - 1][CHUNK_SIZE - 1] = new Block(Block.BlockType.dirt);
            }
            if(Blocks[0][fillLevel][i] == null) {
                Blocks[0][fillLevel][i] = new Block(Block.BlockType.grass);
                Blocks[0][fillLevel - 1][i] = new Block(Block.BlockType.dirt);                
            }
            if(Blocks[CHUNK_SIZE - 1][fillLevel][i] == null) {
                Blocks[CHUNK_SIZE - 1][fillLevel][i] = new Block(Block.BlockType.grass);
                Blocks[CHUNK_SIZE - 1][fillLevel - 1][i] = new Block(Block.BlockType.dirt);
            }
        }

         //Turn edges of where water is going to be into sand      
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int z = 0; z < CHUNK_SIZE; z++) {
                if (Blocks[x][fillLevel][z] == null) {
                    continue;
                }
                if ((x + 1 < CHUNK_SIZE && Blocks[x + 1][fillLevel][z] == null) || (x - 1 >= 0 && Blocks[x - 1][fillLevel][z] == null)
                        || (z + 1 < CHUNK_SIZE && Blocks[x][fillLevel][z + 1] == null) || (z - 1 >= 0 && Blocks[x][fillLevel][z - 1] == null)) {
                    Blocks[x][fillLevel][z] = new Block(Block.BlockType.sand);
                }
            }
        }

        //fill water in, set sand below the water if below the water isnt the bottom level
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int z = 0; z < CHUNK_SIZE; z++) {
                if (Blocks[x][fillLevel][z] == null) {
                    Blocks[x][fillLevel][z] = new Block(Block.BlockType.water);
                    if (fillLevel - 1 >= 1) {
                        Blocks[x][fillLevel - 1][z] = new Block(Block.BlockType.sand);
                    }
                }
            }
        }

        
        rebuildMesh(startX, startY, startZ);
    
    }
     
    public static float[] createTexCube(float x, float y, Block block)
    {
        float offset = (1024f/16) / 1024f;

        switch(block.getId()){
            case 0://Grass
                return new float[]{
                    //TOP QUAD(DOWN=+Y)
                    x+offset*3, y+offset*10,//green wool for top of grass
                    x+offset*2, y+offset*10,
                    x+offset*2, y+offset*9,
                    x+offset*3, y+offset*9,
                    //BOTTOM QUAD
                    x+offset*3, y+offset*1,
                    x+offset*2, y+offset*1,
                    x+offset*2, y+offset*0,
                    x+offset*3, y+offset*0,
                    //FRONT QUAD
                    x+offset*3, y+offset*0,
                    x+offset*4, y+offset*0,
                    x+offset*4, y+offset*1,
                    x+offset*3, y+offset*1,
                    //BACK QUAD
                    x+offset*4, y+offset*1,
                    x+offset*3, y+offset*1,
                    x+offset*3, y+offset*0,
                    x+offset*4, y+offset*0,
                    //LEFT QUAD
                    x+offset*3, y+offset*0,
                    x+offset*4, y+offset*0,
                    x+offset*4, y+offset*1,
                    x+offset*3, y+offset*1,
                    //RIGHT QUAD
                    x+offset*3, y+offset*0,
                    x+offset*4, y+offset*0,
                    x+offset*4, y+offset*1,
                    x+offset*3, y+offset*1
                };
            case 1://Sand
                    return sameTextureOnAllSides(x, y, offset, 2, 1);
            case 2://Water
                    return sameTextureOnAllSides(x, y, offset, 14, 0);
            case 3://Dirt
                    return sameTextureOnAllSides(x, y, offset, 2, 0);
            case 4://Stone
                    return sameTextureOnAllSides(x, y, offset, 1, 0);
            case 5://Bedrock
                    return sameTextureOnAllSides(x, y, offset, 1, 1);
        }
        throw new RuntimeException("No texture mapping for block id: " + block.getId());
   }  
    
    private static float[] sameTextureOnAllSides(float x, float y, float offset, float left, float top){
        float right = left+1;
        float bottom = top+1;
        return new float[] {
            //TOP QUAD(DOWN=+Y)
            x+offset*right, y+offset*bottom,
            x+offset*left, y+offset*bottom,
            x+offset*left, y+offset*top,
            x+offset*right, y+offset*top,
            //BOTTOM QUAD
            x+offset*right, y+offset*top,
            x+offset*left, y+offset*top,
            x+offset*left, y+offset*bottom,
            x+offset*right, y+offset*bottom,
            //FRONT QUAD
            x+offset*left, y+offset*bottom,
            x+offset*right, y+offset*bottom,
            x+offset*right, y+offset*top,
            x+offset*left, y+offset*top,
            //BACK QUAD
            x+offset*right, y+offset*top,
            x+offset*left, y+offset*top,
            x+offset*left, y+offset*bottom,
            x+offset*right, y+offset*bottom,
            //LEFT QUAD
            x+offset*left, y+offset*bottom,
            x+offset*right, y+offset*bottom,
            x+offset*right, y+offset*top,
            x+offset*left, y+offset*top,
            //RIGHT QUAD
            x+offset*left, y+offset*bottom,
            x+offset*right, y+offset*bottom,
            x+offset*right, y+offset*top,
            x+offset*left, y+offset*top
        };
    }
    
    private void generateHeightMap()
    { 
        SimplexNoise noise = new SimplexNoise(CHUNK_SIZE, PERSISTANCE, SEED);
        heightMap = new double[CHUNK_SIZE][CHUNK_SIZE];

        for (int i = 0; i < CHUNK_SIZE; i++) {
            for (int j = 0; j < CHUNK_SIZE; j++) {
                for (int k = 0; k < CHUNK_SIZE; k++) {
                    heightMap[i][j] = (noise.getNoise(StartX + i, StartY + j, StartZ + k) + 1) * 8;
                }
            }
        }
        
    }
    
}
