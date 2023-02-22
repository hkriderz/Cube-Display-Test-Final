package hw3cs4450;

import javax.swing.JOptionPane;

public class CameraSettings 
{
     //creating singleton class
    private static CameraSettings cameraSettings_instance = null;
    public static CameraSettings getInstance()
    {
        if (cameraSettings_instance == null)
        {
            cameraSettings_instance = new CameraSettings();
        }
        return cameraSettings_instance;
    }
    
    private static boolean acceptRandomization;
    public void checkRandomization()
    {
        
        //declarations
        Object[] options = {"Randomize Spawn", "Do Not Randomize Spawn"};
        
        //create JOptionPane
        int selection = JOptionPane.showOptionDialog(null,
        "Please choose spawn.",
        "Randomize Spawn?",
        JOptionPane.YES_NO_CANCEL_OPTION,
        JOptionPane.QUESTION_MESSAGE,null, options, options[1]);
        
        if (selection == 0)
        {
            acceptRandomization = true;
        }
        if (selection == 1)
        {
            acceptRandomization = false;
        }
        this.acceptRandomization = acceptRandomization;
    }
     
    public boolean returnRandomizationSettings()
    {
        return acceptRandomization;
    }
     
}
