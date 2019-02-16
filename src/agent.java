/* DAD Assignment 2

By:
    Oliver Sim Choo Howe (0327159)
    Dalia Abdulkareem (0321666)
    Farhana Islam (0320853)  */

import java.io.IOException;
import java.net.Socket;

public class agent extends Thread{
    
    String message; 
    String host;
    int port; 

    public agent(String message, String host ,int port){
        this.message = message;
        this.host = host;
        this.port = port;
    }

    public void run(){
        try{

            Socket socket = new Socket(host,port);
            socket.getOutputStream().write(message.getBytes());
            socket.close(); }
        
        catch(IOException e){ e.printStackTrace(); }
    }
}