package client;


import javax.swing.*;

public class MainClient {
    public static void main(String[] args){
        ClientGUI frame = new ClientGUI();
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setVisible(true);
    }
}
