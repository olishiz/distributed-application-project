/* DAD Assignment 2

By:
    Oliver Sim Choo Howe (0327159)
    Dalia Abdulkareem (0321666)
    Farhana Islam (0320853)  */

import java.io.IOException;

public class account {
    
    private String username;
    private String password;

    public account (){}

    //setter
    public void setUsername(String username){ this.username = username; }
    public void setPassword(String password){ this.password = password; }
    
    //getters
    public String getUsername(){ return username; }
    public String getPassword(){ return password; }

    //To read from the agent file
    public static int readFromAgent(String username, String password){

        String UsernameA1 = "Oliver";
        String PasswordA1 = "1111";

        String UsernameA2 = "Farhana";
        String PasswordA2 = "2222";

        String UsernameA3 = "Dalia";
        String PasswordA3 = "3333";

            int flag = 1;

                if(username.equals(UsernameA1)){
                    if(password.equals(PasswordA1)){
                                flag = 0; }
                }
                else if(username.equals(UsernameA2)){
                    if(password.equals(PasswordA2)){
                            flag = 0; }
                }
                else if(username.equals(UsernameA3)){
                    if(password.equals(PasswordA3)){
                            flag = 0; }
                }
            return flag;
    }
    
    //To read from client file
    public static int readFromClient(String username, String password) throws IOException{

        String UsernameC1 = "Messi";
        String PasswordC1 = "1111";

        String UsernameC2 = "Ronaldo";
        String PasswordC2 = "2222";

        String UsernameC3 = "Rooney";
        String PasswordC3 = "3333";

        String UsernameC4 = "Zlatan";
        String PasswordC4 = "4444";

            int flag = 1;

                if(username.equals(UsernameC1)){
                    if(password.equals(PasswordC1)){
                            flag = 0; }
                }
                else if(username.equals(UsernameC2)){
                    if(password.equals(PasswordC2)){
                            flag = 0; }
                }
                else if(username.equals(UsernameC3)){
                    if(password.equals(PasswordC3)){
                            flag = 0; }
                }
                else if(username.equals(UsernameC4)){
                    if(password.equals(PasswordC4)){
                            flag = 0; }
                }
            return flag;
    }
}