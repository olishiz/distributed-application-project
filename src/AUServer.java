/* DAD Assignment 2

By:
    Oliver Sim Choo Howe (0327159)
    Dalia Abdulkareem (0321666)
    Farhana Islam (0320853)  */

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

public class AUServer{
    
    public static void main (String[] args) throws Exception{

        Registry registry = LocateRegistry.createRegistry(1099);
        AUInterface showAU = new AU();
        registry.rebind("ToShowAU", showAU);
        System.out.printf("RMI server is successfully started!");
    }       
}

