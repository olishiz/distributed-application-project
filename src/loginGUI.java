/* DAD Assignment 2

By:
    Oliver Sim Choo Howe (0327159)
    Dalia Abdulkareem (0321666)
    Farhana Islam (0320853)  */

import java.awt.*;
import javax.swing.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class loginGUI extends JFrame {

    private JLabel welcomeLabel, nameLabel, passwordLabel, loginAsLabel;
    private JTextField tfUsername;
    private JButton btnAgent, btnClient, btnAU;
    private JPasswordField tfPassword;

    public loginGUI() {

        getContentPane().setBackground(Color.WHITE);
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 385);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);

        welcomeLabel = new JLabel("");
        Image img = new ImageIcon(this.getClass().getResource("cvtLogo.jpg")).getImage().getScaledInstance(215, 165, Image.SCALE_SMOOTH);
        welcomeLabel.setIcon(new ImageIcon(img));
        welcomeLabel.setBounds(118, -20, 316, 211);
        getContentPane().add(welcomeLabel);

        JSeparator separator = new JSeparator();
        separator.setBounds(65, 176, 319, 16);
        getContentPane().add(separator);

        nameLabel = new JLabel("Username: ");
        nameLabel.setForeground(new Color(12, 72, 109));
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        nameLabel.setBounds(67, 193, 104, 16);
        getContentPane().add(nameLabel);

        tfUsername = new JTextField();
        tfUsername.setBounds(183, 188, 200, 26);
        tfUsername.setColumns(10);
        getContentPane().add(tfUsername);
        
        passwordLabel = new JLabel("Password: ");
        passwordLabel.setForeground(new Color(12, 72, 109));
        passwordLabel.setFont(new Font("Arial",Font.PLAIN, 18));
        passwordLabel.setBounds(72, 232, 101, 16);
        getContentPane().add(passwordLabel);

        tfPassword = new JPasswordField();
        tfPassword.setBounds(183, 226, 200, 26);
        getContentPane().add(tfPassword);

        //login as either agent or customer
        loginAsLabel = new JLabel("Login as: ");
        loginAsLabel.setForeground(new Color(12, 72, 109));
        loginAsLabel.setBounds(83, 272, 87, 26);
        loginAsLabel.setFont(new Font("Arial",Font.PLAIN, 18));
        getContentPane().add(loginAsLabel);

        btnAgent = new JButton("Agent");
//        btnAgent.setBorderPainted(false);
//        btnAgent.setBackground(new Color(169,215,168));
        btnAgent.setBounds(184, 270, 95, 29);
        btnAgent.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try { loginAsAgent(); } 
                catch (IOException e1) { e1.printStackTrace(); }
            }
        });
        getContentPane().add(btnAgent);

        btnClient = new JButton("Client");
        btnClient.setBounds(287, 270, 95, 29);
//        btnClient.setBorderPainted(false);
//        btnClient.setBackground(new Color(207,227,156));
        btnClient.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                try { loginAsClient(); } 
                catch (IOException e1) { e1.printStackTrace(); }
            }
        });
        getContentPane().add(btnClient);

        btnAU = new JButton("About Us");
        btnAU.setBounds(237, 305, 95, 29);
//        btnAU.setForeground(Color.white);
//        btnAU.setBorderPainted(false);
//        btnAU.setBackground(new Color(12, 72, 109));
        btnAU.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try { showMe(); } 
                catch (Exception ex) {
                Logger.getLogger(loginGUI.class.getName()).log(Level.SEVERE, null, ex); }
            }
        });
        getContentPane().add(btnAU);	
    }

    //READ HERE
    account account;
    agentGUI agent;
    int count = 0;
    int cCount = 0;
    int aCount = 0;

    public void loginAsAgent() throws IOException{
        if (account.readFromAgent(tfUsername.getText(), (new String(tfPassword.getPassword()))) == 0){
            dispose();
            new agentGUI(tfUsername.getText());
            JOptionPane.showMessageDialog(null, "Login successful!"); }
        else {
            JOptionPane.showMessageDialog(null, "Invalid username or password. Please try again");
            tfUsername.setText("");
            tfPassword.setText("");
            count++;
            if(count == 3){
                    JOptionPane.showMessageDialog(null, "No more login attempts.\nSystem will now terminate!");
                    System.exit(0);
                    dispose(); }
        }
    }

    clientGUI client;

    public void loginAsClient() throws IOException{
        if (account.readFromClient(tfUsername.getText(), (new String(tfPassword.getPassword()))) == 0){
            dispose();
            new clientGUI(tfUsername.getText());
            JOptionPane.showMessageDialog(null, "Login successful!"); }
        else {
            JOptionPane.showMessageDialog(null, "Invalid username or password. Please try again");
            tfUsername.setText("");
            tfPassword.setText("");
            count++;
            
            if(count == 3){
                JOptionPane.showMessageDialog(null, "No more login attempts.\nSystem will now terminate!");
                System.exit(0);
                dispose(); }
        }
    }

    public void showMe() throws Exception{

        String[] arguments = new String[] {"123"};
        new AUServer().main(arguments);
        String[] argument = new String[] {"123"};
        new AUClient().main(argument);
    }
}	