/* DAD Assignment 2

By:
    Oliver Sim Choo Howe (0327159)
    Dalia Abdulkareem (0321666)
    Farhana Islam (0320853)  */

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AUInterface extends Remote{
	
    public String newAU() throws RemoteException;      

}
