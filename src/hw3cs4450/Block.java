/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hw3cs4450;

/*
* The clas is the block class that holds the ID information for each type of blocks
*/
public class Block {

private boolean isActive;
private BlockType blockType;
private float x,y,z; 

    //Enurmerated class that holds blocktype
    public enum BlockType {
        grass(0),
        sand(1),
        water(2),
        dirt(3),
        stone(4),
        bedrock(5);
        public int blockID;
        
        //uses enumerated number type to give Block ID
        BlockType(int i){
            blockID = i;
        }
        public int getID(){
             return blockID;
        }
        public void SetID(int i){
            blockID = i;
        }
    }

    public Block(BlockType type){
        blockType = type;
    }
    
    public void setCoords(float x, float y, float z){
        this.x = x;
        this.y =y;
        this.z = z;

    }
    public boolean isActive(){
        return isActive;
    }
    public void setActive(boolean active){
        isActive = active;
    }
    public int getId(){
        return blockType.getID();
    }
}