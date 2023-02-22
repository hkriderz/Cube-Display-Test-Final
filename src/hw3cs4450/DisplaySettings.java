package hw3cs4450;
import java.awt.Component;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class DisplaySettings 
{
    public int[] setParameters()
    {
        //declarations
        int parameters[] = new int[2];
        Object[] options = {"640x480", "1920x1080", "2560x1440"};
        
        //create JOptionPane
        int selection = JOptionPane.showOptionDialog(null,
        "Please chose the appropriate resolution.",
        "Display Settings",
        JOptionPane.YES_NO_CANCEL_OPTION,
        JOptionPane.QUESTION_MESSAGE,null, options, options[2]);
        
        
       //modify resolution based off of user input
       if (selection == 0)
       {
            parameters[0] = 640;
            parameters[1] = 480;
       }
       if (selection == 1)
       {
            parameters[0] = 1920;
            parameters[1] = 1080;
       }
       if (selection == 2)
       {
            parameters[0] = 2560;
            parameters[1] = 1440;
       }

        return parameters;
    }
}
