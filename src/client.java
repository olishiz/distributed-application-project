/* DAD Assignment 2

By:
    Oliver Sim Choo Howe (0327159)
    Dalia Abdulkareem (0321666)
    Farhana Islam (0320853)  */

import java.io.IOException;
import java.net.Socket;

public class client extends Thread{

    private String message;
    private int port;
    private String serverIP;

    public client(String message, String serverIP, int port){
        this.message = message;
        this.serverIP = serverIP;
        this.port = port; }

    public void run(){
        try{
            Socket socket = new Socket(serverIP, port);
            socket.getOutputStream().write(message.getBytes());
            socket.close();
        }
        catch(IOException e){ e.printStackTrace(); }
    }
}