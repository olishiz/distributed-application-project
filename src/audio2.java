/* DAD Assignment 2

By:
    Oliver Sim Choo Howe (0327159)
    Dalia Abdulkareem (0321666)
    Farhana Islam (0320853)  */

import java.io.*;
import java.net.*;
import javax.sound.sampled.*;

public class audio2 {
    
    private static boolean call = false;
    private static InetAddress customerIP;
    private int sendPort = 8181; // The port number to which the agent wishes to send
    private int receivePort = 9191; // The agent's own port number, to listen on for traffic
    private DatagramSocket receiveSocket; // The socket to receive audio on
    private DatagramSocket sendSocket; // The socket to send audio on 
    private static boolean stopCapture;
    private static boolean stopPlay;
    AudioFormat audioFormat;
    TargetDataLine targetDataLine;
    AudioInputStream audioInputStream;
    SourceDataLine sourceDataLine;

    public audio2() throws UnknownHostException{
        this.customerIP = InetAddress.getByName("127.0.0.1"); }

    // A method to create and returns an AudioFormat object for a given set of format parameters. 
    private AudioFormat getAudioFormat() {
      
        float sampleRate = 16000.0F;
        int sampleInbits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = false;
        return new AudioFormat(sampleRate, sampleInbits, channels, signed, bigEndian); }
    
    //  A method to capture audio input from a microphone and sends it as a UDP packet.
    class CaptureAudio implements Runnable {

        private byte[] tempBuffer;
        private DatagramSocket socket;
        private InetAddress ip;
        private int port;

        public CaptureAudio(DatagramSocket socket, InetAddress ip, int port) {
            this.socket = socket;
            this.ip = ip;
            this.port = port;
            this.tempBuffer = new byte[14000]; }

        public void run() {  
            try {
                stopCapture = false;

                // Set up everything for capture
                audioFormat = getAudioFormat();
                DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
                targetDataLine = (TargetDataLine)
                AudioSystem.getLine(dataLineInfo);
                targetDataLine.open(audioFormat);
                targetDataLine.start();

                try{
                    // Loop until stopCapture is set by another thread.
                    while(!stopCapture){
                        // Read data from the internal buffer of the data line.
                        int cnt = targetDataLine.read(tempBuffer, 0, tempBuffer.length);
                        if(cnt > 0){
                            DatagramPacket outPacket = new DatagramPacket(tempBuffer, tempBuffer.length, this.ip, this.port);
                            this.socket.send(outPacket); }
                    }
                }
                catch (Exception e) {}
            } 
            catch (Exception e) {} }
    }

    // A method to receive audio input from a UDP packet and plays it. 
    class PlayAudio implements Runnable {

        private DatagramSocket socket;
        private byte[] tempBuffer;

        public PlayAudio(DatagramSocket socket)	{
            this.socket = socket;
            this.tempBuffer = new byte[14000]; }

        public void run() {
            try{
                DatagramPacket inPacket;
                stopPlay = false;

                // Loop until stopPlay is set by another thread.
                while (!stopPlay) {
                    //Put received data into a byte array object
                    inPacket = new DatagramPacket(tempBuffer, tempBuffer.length);
                    this.socket.receive(inPacket);

                    byte[] audioData = inPacket.getData();

                    //Get an input stream on the byte array containing the data
                    InputStream byteArrayInputStream = new ByteArrayInputStream(audioData);
                    AudioFormat audioFormat = getAudioFormat();
                    audioInputStream = new AudioInputStream(byteArrayInputStream, audioFormat, audioData.length/audioFormat.getFrameSize());
                    DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
                    sourceDataLine = (SourceDataLine)AudioSystem.getLine(dataLineInfo);
                    sourceDataLine.open(audioFormat);
                    sourceDataLine.start();

                    try { 
                        int cnt;
                        // Keep looping until the input read method returns -1 for empty stream.
                        while((cnt = audioInputStream.read(tempBuffer, 0, tempBuffer.length)) != -1){
                            if(cnt > 0){
                                // Write data to the internal buffer of the data line where it will be delivered to the speaker.
                                sourceDataLine.write(tempBuffer, 0, cnt); }
                        }
                        // Block and wait for internal buffer of the data line to empty.
                        sourceDataLine.drain();
                        sourceDataLine.close(); 
                    }
                    catch (Exception e) {} }
            } 
            catch (Exception e) {} }
    }

    // A method to initiates a call between two users.  
    public void callConnect() {
        //Initiate sockets to use for audio streaming
        try {
            receiveSocket = new DatagramSocket(receivePort);
            sendSocket = new DatagramSocket(sendPort);
        } 
        catch (Exception e){}

        Thread CaptureAudio = new Thread(new CaptureAudio(sendSocket, customerIP, receivePort));
        CaptureAudio.start();

        Thread PlayAudio = new Thread(new PlayAudio(receiveSocket));
        PlayAudio.start(); }

    // A method to disconnect the client from the current call
    public void callDisconnect() {
        try{
            // Stop the threads
            stopCapture = true;
            stopPlay = true;

            // Wait for threads to terminate
            try {
                Thread.sleep(200);
            } catch (Exception e) {}

            // Close the sockets
            try {
                sendSocket.close();
                receiveSocket.close();
            } 
            catch (Exception e) {}
        } 
        catch (Exception e){} }  
}