/* DAD Assignment 2

By:
    Oliver Sim Choo Howe (0327159)
    Dalia Abdulkareem (0321666)
    Farhana Islam (0320853)  */

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.swing.JOptionPane;

public class AUClient{
    
    public static void main(String[]args) throws Exception {
        Registry registry = LocateRegistry.getRegistry("127.0.0.1");
        AUInterface
        AUObject = (AUInterface)registry.lookup("ToShowAU");
        JOptionPane.showMessageDialog(null,AUObject.newAU());
    }
}