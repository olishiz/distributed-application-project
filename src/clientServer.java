/* DAD Assignment 2

By:
    Oliver Sim Choo Howe (0327159)
    Dalia Abdulkareem (0321666)
    Farhana Islam (0320853)  */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class clientServer extends Thread{
    ServerSocket serverSocket;
    int port;
    writeGUI write; 

    public clientServer(writeGUI write, int port){
        this.write = write;
        this.port = port;

        try{ serverSocket = new ServerSocket(port); }
        catch(IOException e){ e.printStackTrace(); }
    }

    public void run(){
        Socket client;
        try{
            while((client = serverSocket.accept()) != null){
                BufferedReader read = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String message = read.readLine();
                write.writeGUI1("" +message); }
        }
        catch(IOException e){ e.printStackTrace(); }
    }
}
