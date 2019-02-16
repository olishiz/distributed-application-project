/* DAD Assignment 2

By:
    Oliver Sim Choo Howe (0327159)
    Dalia Abdulkareem (0321666)
    Farhana Islam (0320853)  */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.Date;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;


public class agentGUI extends JFrame implements writeGUI {
        
    private JPanel contentPane;
    private JTextField textField;
    private JLabel lblClient1, lblClient2;
    private JButton btn1, btn2, btnAll;
    private JScrollPane scrollClient1;
    private JTextArea chatAreaC1, chatAreaC2;
    private JTextField tfIPC1;
    private JTextField tfIPC2;
    private JTextField AgentPort;


    agent agent1, agent2;
    agentServer agentServer;
    private JButton btnDisconnectC2;
    private JButton btnConnectC2;
    private JTextField tfPortC1;
    private JTextField tfPortC2;
    private JLabel lbSendTo;

    String date = new Date().toString();
    String dateWithoutTime = date.substring(0, 10);
    String time = date.substring(0, 16);
    BufferedWriter chatFile;
    String messageLog;
    audio1 ta = new audio1();
    audio2 ty = new audio2();

    boolean stopaudioCapture = false;
    ByteArrayOutputStream byteOutputStream;
    AudioFormat adFormat;
    TargetDataLine targetDataLine;
    AudioInputStream InputStream;
    SourceDataLine sourceLine;
    Graphics g;
    DatagramSocket clientSocket;
    DatagramSocket clientSocket2;

    private String username;

    public agentGUI(String username) throws IOException {
        
        this.username = username;
        setTitle("Agent: " + username);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 485, 510);
        setLocationRelativeTo(null);
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        setVisible(true);

        lblClient1 = new JLabel("Client 1");
        lblClient1.setBounds(97, 37, 61, 16);
        contentPane.add(lblClient1);

        scrollClient1 = new JScrollPane();
        scrollClient1.setBounds(12, 125, 444, 213);
        contentPane.add(scrollClient1);

        chatAreaC1 = new JTextArea();
        scrollClient1.setViewportView(chatAreaC1);
        chatAreaC1.setEditable(false);

        lblClient2 = new JLabel("Client 2");
        lblClient2.setBounds(330, 37, 61, 16);
        contentPane.add(lblClient2);

        // Agent types the message here 
        textField = new JTextField();
        textField.setBounds(12, 345, 444, 35);
        contentPane.add(textField);
        textField.setColumns(10);

        // Button to send to specific clients, 1 or 2
        lbSendTo = new JLabel("Send to:");
        lbSendTo.setBounds(235, 387, 61, 16);
        contentPane.add(lbSendTo);

        btn1 = new JButton("1");
        btn1.setBounds(286, 385, 50, 26);
        btn1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    try {
                            btnSendClient1();
                    } catch (IOException e1) {
                            e1.printStackTrace();
                    }
            }
        });
        contentPane.add(btn1);

        btn2 = new JButton("2");
        btn2.setBounds(343, 385, 50, 26);
        btn2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try { btnSendClient2(); } 
                catch (IOException e1) { e1.printStackTrace(); }
            }
        });
        contentPane.add(btn2);

        btnAll = new JButton("All");
        btnAll.setBounds(400, 385, 50, 26);
        btnAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try { btnSendAll(); } 
                catch (IOException e1) { e1.printStackTrace(); }
            }
        });
        contentPane.add(btnAll);

        // Client 1 Port Number
        JLabel lbPortC1 = new JLabel("Listen from Port: ");
        lbPortC1.setBounds(12, 9, 118, 16);
        contentPane.add(lbPortC1);

        tfPortC1 = new JTextField();
        tfPortC1.setBounds(115, 4, 61, 26);
        tfPortC1.setColumns(10);
        contentPane.add(tfPortC1);
        
        // Client 1 IP Address
        JLabel lbIPC1 = new JLabel("[C1] IP:");
        lbIPC1.setBounds(12, 62, 61, 16);
        contentPane.add(lbIPC1);

        tfIPC1 = new JTextField();
        tfIPC1.setColumns(10);
        tfIPC1.setBounds(60, 57, 130, 26); 
        contentPane.add(tfIPC1);

        JButton btnConnectC1 = new JButton("Connect");
        btnConnectC1.setBounds(12, 90, 99, 29);
//        btnConnectC1.setBorderPainted(false);
//        btnConnectC1.setForeground(Color.WHITE);
//        btnConnectC1.setBackground(new Color(166,206,58));
        btnConnectC1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { btnConnectClient1(); }
        });
        contentPane.add(btnConnectC1);

        JButton btnDisconnectC1 = new JButton("Disconnect");
        btnDisconnectC1.setBounds(120, 90, 99, 29);
//        btnDisconnectC1.setBorderPainted(false);
//        btnDisconnectC1.setForeground(Color.WHITE);
//        btnDisconnectC1.setBackground(new Color(209,40,47));
        btnDisconnectC1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {  btnDisconnect1(); }
        });
        contentPane.add(btnDisconnectC1);

        // Client 2 Port Number
        JLabel lbPortC2 = new JLabel("Listen From Port:");
        lbPortC2.setBounds(245, 9, 111, 16);
        contentPane.add(lbPortC2);
        
        tfPortC2 = new JTextField();
        tfPortC2.setColumns(10);
        tfPortC2.setBounds(350, 4, 61, 26);
        contentPane.add(tfPortC2);
        
        // Client 2 IP Address
        JLabel lbIPC2 = new JLabel("[C2] IP:");
        lbIPC2.setBounds(245, 62, 61, 16);
        contentPane.add(lbIPC2);

        tfIPC2 = new JTextField();
        tfIPC2.setColumns(10);
        tfIPC2.setBounds(295, 57, 130, 26);
        contentPane.add(tfIPC2);

        btnConnectC2 = new JButton("Connect");
        btnConnectC2.setBounds(245, 90, 99, 29);
//        btnConnectC2.setBorderPainted(false);
//        btnConnectC2.setForeground(Color.WHITE);
//        btnConnectC2.setBackground(new Color(166,206,58));
        btnConnectC2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){ btnConnectClient2(); }
        });
        contentPane.add(btnConnectC2);

        btnDisconnectC2 = new JButton("Disconnect");
        btnDisconnectC2.setBounds(355, 90, 99, 29);
//        btnDisconnectC2.setBorderPainted(false);
//        btnDisconnectC2.setForeground(Color.WHITE);
//        btnDisconnectC2.setBackground(new Color(209,40,47));
        btnDisconnectC2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){ btnDisconnect2(); }
        });
        contentPane.add(btnDisconnectC2);                		

        // Record voice message to send to client 1 or 2
        JLabel lblVoiceMessage = new JLabel("Voice Message:");
        lblVoiceMessage.setBounds(12, 425, 98, 16);
        contentPane.add(lblVoiceMessage);

        // Client 1 start and stop button
        JButton btnStart_1_Voice = new JButton("Start (1)");
        btnStart_1_Voice.setBounds(110, 420, 85, 29);
        btnStart_1_Voice.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){ ta.callConnect(); }
        });
        contentPane.add(btnStart_1_Voice);

        JButton btnStart_2_Voice = new JButton("Stop (1)");
        btnStart_2_Voice.setBounds(197, 420, 85, 29);
        btnStart_2_Voice.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){ ta.callDisconnect(); }
        });
        contentPane.add(btnStart_2_Voice);

        // Client 2 start and stop button
        JButton btnStart_5_Voice = new JButton("Start (2)");
        btnStart_5_Voice.setBounds(290, 420, 85, 29);
        btnStart_5_Voice.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){ ty.callConnect(); }
        });
        contentPane.add(btnStart_5_Voice);

        JButton btnStart_10_Voice = new JButton("Stop (2)");
        btnStart_10_Voice.setBounds(377, 420, 85, 29);
        btnStart_10_Voice.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){ ty.callDisconnect(); }
        });
        contentPane.add(btnStart_10_Voice);

        //Agent Port
        AgentPort = new JTextField();
        AgentPort.setBounds(93, 385, 61, 26);
        AgentPort.setColumns(10);
        contentPane.add(AgentPort);
        
        JLabel lblAgentsPort = new JLabel("Agent's Port: ");
        lblAgentsPort.setBounds(12, 387, 94, 16);
        contentPane.add(lblAgentsPort);
    }
	clientServer clientServer1;
        clientServer clientServer2;
	agent agent;
        private JButton btnStartall_Voice;
        private JButton btnStartall_Voice2;


	private void btnConnectClient1() {
                
            // Build connection to client 1
            clientServer1 = new clientServer(this, Integer.parseInt(AgentPort.getText()));
            clientServer1.start();
            JOptionPane.showMessageDialog(null, "Connection with Client is established"); }

	private void btnConnectClient2() {
            
            // Build connection to client 2
            clientServer2 = new clientServer(this, Integer.parseInt(AgentPort.getText()));
            clientServer2.start();
            JOptionPane.showMessageDialog(null, "Connection with Client is established"); }

	private void btnSendClient1() throws IOException {
            
            if(textField.getText().equals("")){ return; }
            agent1 = new agent(textField.getText(), tfIPC1.getText(),Integer.parseInt(tfPortC1.getText()));
            
            chatAreaC1.append("Agent: " + textField.getText() + "\n\r");
            chatFile = new BufferedWriter(new FileWriter("agentLog1.txt", true));
            
            messageLog = textField.getText();
            chatFile.write(time + "\t" + "Agent:" + "\t " + messageLog + "\n");
            System.out.println(messageLog);
            chatFile.close();
            textField.setText("");
            agent1.start(); }

	private void btnSendClient2() throws IOException {
            
            if(textField.getText().equals("")){ return; }
            agent2 = new agent(textField.getText(), tfIPC2.getText(),Integer.parseInt(tfPortC2.getText()));
            
            chatAreaC1.append("Agent: " + textField.getText() + "\n\r");
            chatFile = new BufferedWriter(new FileWriter("agentLog2.txt", true));
            
            messageLog = textField.getText();
            chatFile.write(time + "\t" + "Agent:" + "\t " + messageLog + "\n");
            System.out.println(messageLog);
            chatFile.close();
            textField.setText("");
            agent2.start(); }

	private void btnSendAll() throws IOException {
            
            if (textField.getText().equals("")) { return; }
            agent1 = new agent(textField.getText(), tfIPC1.getText(),Integer.parseInt(tfPortC1.getText()));
            agent2 = new agent(textField.getText(), tfIPC2.getText(),Integer.parseInt(tfPortC2.getText()));

            chatAreaC1.append("Agent: " + textField.getText() + "\n\r");
            chatFile = new BufferedWriter(new FileWriter("agentLog1.txt", true));

            chatFile = new BufferedWriter(new FileWriter("agentLog2.txt", true));

            messageLog = textField.getText();
            System.out.println(messageLog);
            chatFile.write(time + "\t" + "Agent:" + "\t " + messageLog + "\n");
            chatFile.close();
            textField.setText("");
            agent1.start();
            agent2.start();

	}

	private void btnDisconnect1() {

            int selectedOption = JOptionPane.showConfirmDialog(null, "Do you want to save file?", "Choose",
                            JOptionPane.YES_NO_OPTION);
            if (selectedOption == JOptionPane.YES_OPTION) {
                String date = new Date().toString();
                String dateWithoutTime = date.substring(0, 10);

                File file = new File("agentLog1.txt"); }
            
            agent1 = new agent("Agent has ended the conversation! ", tfIPC1.getText(),Integer.parseInt(tfPortC1.getText()));
            agent1.start();
            tfPortC1.setText(""); }

	private void btnDisconnect2() {

            int selectedOption = JOptionPane.showConfirmDialog(null, "Do you want to save file?", "Choose",
                            JOptionPane.YES_NO_OPTION);
            if (selectedOption == JOptionPane.YES_OPTION) {
                String date = new Date().toString();
                String dateWithoutTime = date.substring(0, 10);
                
                File file = new File("agentLog2.txt"); }
            
            agent2 = new agent("Agent has ended the conversation! ",tfIPC2.getText(), Integer.parseInt(tfPortC2.getText()));
            agent2.start();
            tfPortC2.setText(""); }
       
        private void captureAudio() {
            try {
                adFormat = getAudioFormat();
                DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, adFormat);
                targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
                targetDataLine.open(adFormat);
                targetDataLine.start();

                Thread captureThread = new Thread(new CaptureThread());
                captureThread.start(); } 
            catch (Exception e) {
                StackTraceElement stackEle[] = e.getStackTrace();
                for (StackTraceElement val : stackEle) {
                    System.out.println(val);
                }
                System.exit(0); }   
        }    

        private void playAudio() {
            try {
                byte audioData[] = byteOutputStream.toByteArray();
                InputStream byteInputStream = new ByteArrayInputStream(audioData);
                AudioFormat adFormat = getAudioFormat();
                InputStream = new AudioInputStream(byteInputStream, adFormat, audioData.length / adFormat.getFrameSize());
                DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, adFormat);
                sourceLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
                sourceLine.open(adFormat);
                sourceLine.start();
                Thread playThread = new Thread(new PlayThread());
                playThread.start(); } 
            catch (Exception e) {
                System.out.println(e);
                System.exit(0); }
        }

        private AudioFormat getAudioFormat() {
            float sampleRate = 16000.0F;
            int sampleInbits = 16;
            int channels = 1;
            boolean signed = true;
            boolean bigEndian = false;
            return new AudioFormat(sampleRate, sampleInbits, channels, signed, bigEndian); }

 class CaptureThread extends Thread {

    byte tempBuffer[] = new byte[10000];

    public void run() {

        byteOutputStream = new ByteArrayOutputStream();
        stopaudioCapture = false;
        try {
            clientSocket = new DatagramSocket(8786);
            InetAddress IPAddress = InetAddress.getByName("127.0.0.1");
            while (!stopaudioCapture) {
                int cnt = targetDataLine.read(tempBuffer, 0, tempBuffer.length);
                if (cnt > 0) {
                    DatagramPacket sendPacket = new DatagramPacket(tempBuffer, tempBuffer.length, IPAddress, 9786);
                    clientSocket.send(sendPacket);
                    byteOutputStream.write(tempBuffer, 0, cnt); }
            }
            byteOutputStream.close();
            byteOutputStream.toByteArray(); } 
        catch (Exception e) {
            System.out.println("CaptureThread::run()" + e);
            System.exit(0); }
    }
}

class PlayThread extends Thread {

    byte tempBuffer[] = new byte[10000];

    public void run() {
        try {
            int cnt;
            while ((cnt = InputStream.read(tempBuffer, 0, tempBuffer.length)) != -1) {
                if (cnt > 0) {
                   sourceLine.write(tempBuffer, 0, cnt); 
                }
            } } 
        catch (Exception e) {
            System.out.println(e);
            System.exit(0); }
    }
}

private void captureAudio2() {
    try {
        adFormat = getAudioFormat();
        DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, adFormat);
        targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
        targetDataLine.open(adFormat);
        targetDataLine.start();

        Thread captureThread2 = new Thread(new CaptureThread2());
        captureThread2.start(); } 
    catch (Exception e) {
        StackTraceElement stackEle[] = e.getStackTrace();
        for (StackTraceElement val : stackEle) {
            System.out.println(val); }
        System.exit(0); }
}

private void playAudio2() {
    try {
        byte audioData[] = byteOutputStream.toByteArray();
        InputStream byteInputStream = new ByteArrayInputStream(audioData);
        AudioFormat adFormat = getAudioFormat();
        InputStream = new AudioInputStream(byteInputStream, adFormat, audioData.length / adFormat.getFrameSize());
        DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, adFormat);
        sourceLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
        sourceLine.open(adFormat);
        sourceLine.start();
        Thread playThread2 = new Thread(new PlayThread2());
        playThread2.start(); } 
    catch (Exception e) {
        System.out.println(e);
        System.exit(0); }
}

class CaptureThread2 extends Thread {

    byte tempBuffer[] = new byte[10000];

    public void run() {

        byteOutputStream = new ByteArrayOutputStream();
        stopaudioCapture = false;
        
        try {
            clientSocket2 = new DatagramSocket(8787);
            InetAddress IPAddress = InetAddress.getByName("127.0.0.1");
            while (!stopaudioCapture) {
                int cnt = targetDataLine.read(tempBuffer, 0, tempBuffer.length);
                if (cnt > 0) {
                    DatagramPacket sendPacket = new DatagramPacket(tempBuffer, tempBuffer.length, IPAddress, 9787);
                    clientSocket2.send(sendPacket);
                    byteOutputStream.write(tempBuffer, 0, cnt); }
            }
            byteOutputStream.close();
            byteOutputStream.toByteArray(); } 
        catch (Exception e) {
            System.out.println("CaptureThread::run()" + e);
            System.exit(0); }
    }
}

class PlayThread2 extends Thread {

    byte tempBuffer[] = new byte[10000];

    public void run() {
        try {
            int cnt;
            while ((cnt = InputStream.read(tempBuffer, 0, tempBuffer.length)) != -1) {
                if (cnt > 0) {
                   sourceLine.write(tempBuffer, 0, cnt); } 
            }
        } 
        catch (Exception e) {
            System.out.println(e);
            System.exit(0); }
    }
}
    public void end_audio(){
        try {
            sourceLine.drain();
            sourceLine.close();
            InputStream.close();
            targetDataLine.drain();
            targetDataLine.close();
            InputStream.close(); } 
        catch (IOException e) {}
    }
    @Override
    public void writeGUI1(String s) {
        chatAreaC1.append(s + "\n\r"); }

    public void writeGUI2(String s) {
        chatAreaC2.append(s + "\n\r"); }
}