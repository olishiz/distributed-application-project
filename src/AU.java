/* DAD Assignment 2

By:
    Oliver Sim Choo Howe (0327159)
    Dalia Abdulkareem (0321666)
    Farhana Islam (0320853)  */

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

public class AU extends UnicastRemoteObject implements AUInterface{
    
    public AU() throws RemoteException{}

    public String newAU() throws RemoteException{
   
       return "\nVersion 2.1.1\nThis feature is brought to you by RMI.\n"
               + "By CVT Technologies Sdn Bhd. \n"
               + "All Rights Reserved. 2017."; 
    }
}
