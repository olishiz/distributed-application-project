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
import java.util.concurrent.ArrayBlockingQueue;

public class agentServer extends Thread{
    
    private final static int THREAD_POOL_SIZE = 2;
    private final static int QUEUE_CAPACITY = 20;
    private static ArrayBlockingQueue<Socket> connectionQueue;

    private ServerSocket agentSocket;
    private int port ;
    private writeGUI write;

    public agentServer(writeGUI write, int port){
        this.write = write;
        this.port = port;
        
        try{ agentSocket = new ServerSocket(port); }
        catch(IOException e){ e.printStackTrace(); }
    }

    public void run(){
        Socket agent;
        try{
            while((agent = agentSocket.accept()) != null){

                BufferedReader read = new BufferedReader(new InputStreamReader(agent.getInputStream()));

                String message = read.readLine();
                if(read!= null){ write.writeGUI1("Agent: " + message); }
            }
        }
        catch(IOException e){ e.printStackTrace(); }
    }
    public agentServer(){
        
        try{ agentSocket = new ServerSocket(port); }
        catch(IOException e){ e.printStackTrace(); }
    }	
}