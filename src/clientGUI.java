/* DAD Assignment 2

By:
    Oliver Sim Choo Howe (0327159)
    Dalia Abdulkareem (0321666)
    Farhana Islam (0320853)  */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.*;
import javax.swing.*;

public class clientGUI extends JFrame implements writeGUI{

    boolean stopaudioCapture = false;
    ByteArrayOutputStream byteOutputStream;
    AudioFormat adFormat;
    TargetDataLine targetDataLine;
    AudioInputStream InputStream;
    SourceDataLine sourceLine;
    Graphics g;
    DatagramSocket serverSocket;
    TargetDataLine mic;
    SourceDataLine speaker;
    InetAddress IP = InetAddress.getByName("127.0.0.1");
    ByteArrayOutputStream BAOS;
    AudioFormat audioformat;
    AudioInputStream AIS;
    boolean calling;

    private JTextField textFieldIP,tfReceivePort, agentPort, textFieldClient;
    private JTextArea chatArea;
    private JButton btnConnect, btnDisconnect, btnSend;
    private JLabel portTo, portFrom, IPAddress;
    private JScrollPane scrollPane;

    private String username;

    String date = new Date().toString();
    String dateWithoutTime = date.substring(0, 10);
    String time = date.substring(0,16);
    BufferedWriter chatFile;
    String messageLog;
    audio1 t = new audio1();
    audio2 t2 = new audio2();

    // Randomise client's port number
    int clientPort = ThreadLocalRandom.current().nextInt(1025, 49150 + 1);

    public clientGUI(String username) throws IOException{

        boolean stopaudioCapture = false;
        ByteArrayOutputStream byteOutputStream;
        AudioFormat adFormat;
        TargetDataLine targetDataLine;
        AudioInputStream InputStream;
        SourceDataLine sourceLine;
        Graphics g;

        this.username = username;

        setTitle("Client: " + username);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 465, 365);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);
        setVisible(true);

        // Label and Textfield for Client's Port Number
        tfReceivePort = new JTextField();
        tfReceivePort.setBounds(78, 7, 58, 26);
        getContentPane().add(tfReceivePort);
        tfReceivePort.setColumns(10);
        tfReceivePort.setText(Integer.toString(clientPort));
        tfReceivePort.setEditable(false);
        
        portTo = new JLabel("Client Port : ");
        portTo.setBounds(12, 12, 78, 16);
        getContentPane().add(portTo);
        
        // Label and Textfield to receive IP Address
        textFieldIP = new JTextField();
        textFieldIP.setBounds(34, 37, 90, 26);
        getContentPane().add(textFieldIP);
        textFieldIP.setColumns(10);
        
        IPAddress = new JLabel("IP : ");
        IPAddress.setBounds(12, 40, 38, 16);
        getContentPane().add(IPAddress);
        
        // Label and TextField for Agent's Port Number
        agentPort = new JTextField();
        agentPort.setBounds(330, 7, 61, 26);
        getContentPane().add(agentPort);
        agentPort.setColumns(10);
        
        portFrom = new JLabel("Agent Port : ");
        portFrom.setBounds(230, 12, 92, 16);
        getContentPane().add(portFrom);

        // To Create a connection
        btnConnect = new JButton("Connect");
        btnConnect.setBounds(12, 65, 99, 29);
//        btnConnect.setBorderPainted(false);
//        btnConnect.setForeground(Color.WHITE);
//        btnConnect.setBackground(new Color(166,206,58));
        
        btnConnect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try { btnConnectPerform(); } 
                catch (UnknownHostException ex) {
                    Logger.getLogger(clientGUI.class.getName()).log(Level.SEVERE, null, ex); }
            }
        });
        getContentPane().add(btnConnect);

        // To disconnect a connection and save message in log file
        btnDisconnect = new JButton("Disconnect");
        btnDisconnect.setBounds(120, 65, 99, 29);
//        btnDisconnect.setBorderPainted(false);
//        btnDisconnect.setForeground(Color.WHITE);
//        btnDisconnect.setBackground(new Color(209,40,47));
        
        btnDisconnect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try { btnDisconnectPerform(); } 
                catch (UnknownHostException ex) {
                    Logger.getLogger(clientGUI.class.getName()).log(Level.SEVERE, null, ex); }
            }
        });
        getContentPane().add(btnDisconnect);
        
        // Button to send messages to either client 1 or 2 or all
        btnSend = new JButton("Send");
        btnSend.setBounds(370, 288, 66, 29);
        btnSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try { btnSentPerform(); } 
                catch (IOException e1) { e1.printStackTrace(); }
            }
        });
        getContentPane().add(btnSend);

        textFieldClient = new JTextField();
        textFieldClient.setBounds(12, 290, 355, 26);
        
        // If enter button is pressed, the action below is performed
        textFieldClient.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER){
                    try { btnSentPerform(); } 
                    catch (IOException e1) { e1.printStackTrace(); }
                }
            }
        });
        getContentPane().add(textFieldClient);
        textFieldClient.setColumns(10);
        
        scrollPane = new JScrollPane();
        scrollPane.setBounds(12, 105, 425, 175);
        getContentPane().add(scrollPane);

        chatArea = new JTextArea();
        scrollPane.setViewportView(chatArea);
        chatArea.setEditable(false);

        // To view message sent and receive
        textFieldClient.requestFocusInWindow();

        label = new JLabel("Voice Message : ");
        label.setBounds(220, 35, 115, 16);
        getContentPane().add(label);

        
        btnStart_Voice = new JButton("Channel 1");
        btnStart_Voice.setBounds(330, 37, 105, 29);
        btnStart_Voice.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Thread thread = new Thread() {
                
                    public void run() { t.callConnect(); }
            };
                thread.start(); }
        });
        getContentPane().add(btnStart_Voice);

        btnStart_Voice2 = new JButton("Channel 2");
        btnStart_Voice2.setBounds(330, 69, 105, 29);
        btnStart_Voice2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Thread thread2 = new Thread() {
                
                    public void run() { t2.callConnect(); }
            };
                thread2.start(); }
        });
        getContentPane().add(btnStart_Voice2);
    }

    //Create a Client and Client Server object	
    agentServer server;
    client client;
    private JLabel label;
    private JButton btnStart_Voice, btnStart_Voice2;

    // Actions performed by buttons
    private void btnConnectPerform() throws UnknownHostException {

        server = new agentServer(this, clientPort);
        client = new client("" +username + " has just joined the chat!", textFieldIP.getText(), Integer.parseInt(agentPort.getText()));
        client.start();
        server.start();
        JOptionPane.showMessageDialog(null, "Connection with Agent is established"); }

    private void btnDisconnectPerform() throws UnknownHostException{
        
        int selectedOption = JOptionPane.showConfirmDialog(null, "Do you want to save file?", "Choose", JOptionPane.YES_NO_OPTION); 
        if (selectedOption == JOptionPane.YES_OPTION) {
                String date = new Date().toString();
                String dateWithoutTime = date.substring(0, 10);                       
                File file = new File("clientLog.txt"); }
        
        client = new client("" +username + " has just left the chat!",textFieldIP.getText(), Integer.parseInt(agentPort.getText()));
        client.start();
        tfReceivePort.setText("");
        textFieldClient.setText("");
        chatArea.setText("");
        JOptionPane.showMessageDialog(null, "System will now terminate.");
        System.exit(0); }

    private void btnSentPerform() throws IOException{
        if(textFieldClient.getText().equals("")){ return; }
        
        //Create a client 
        client = new client("" +username + ": " + textFieldClient.getText(),textFieldIP.getText(), Integer.parseInt(agentPort.getText()));
        chatArea.append("" +username + ": " + textFieldClient.getText() + "\n\r");
        chatFile = new BufferedWriter(new FileWriter("clientLog.txt", true));
        messageLog = textFieldClient.getText();
        chatFile.write(time + "\t" + "" +username + ": " + "\t " + messageLog+"\n");
        chatFile.close();
        textFieldClient.setText("");
        client.start(); }

    @Override
    public void writeGUI1(String s) {
        chatArea.append(s + textFieldClient.getText() + "\n\r"); }
    
    public void writeGUI2(String s) {}
}
