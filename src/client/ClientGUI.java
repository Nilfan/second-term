package client;

import common.Const;
import common.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;


public class ClientGUI extends JFrame{
    JTextArea messageArea;
    JTextField textField;
    Client client;
    ClientGUI frame;

    public ClientGUI(){
        frame = this;
        textField = new JTextField();
        textField.setEnabled(false);

        setTitle("Client Frame");
        setSize(Const.DEFAULT_WIDTH, Const.DEFAULT_HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        final JTextField nameField = new JTextField(16);
        final JTextField ipField = new JTextField(16);

        nameField.setText("");
        ipField.setText("***.***.***.***");
        ipField.setEnabled(false);

        /*frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                client.close();
                System.exit(0);
            }
        });
        */

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                client.close();
            }
        });

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new GridLayout(2, 2));
        northPanel.add(new JLabel("Username: ", SwingConstants.RIGHT));
        northPanel.add(nameField);
        northPanel.add(new JLabel("Server IP: ", SwingConstants.RIGHT));
        northPanel.add(ipField);

        nameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (!nameField.getText().equals("") & e.getKeyCode() == KeyEvent.VK_ENTER) {
                    ipField.setEnabled(true);
                    ipField.setText("");
                    nameField.setEnabled(false);
                }
            }
        });

        ipField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        String ip;
                        if (ipField.getText().equals("")) {
                            ip = "localhost";
                        } else {
                            ip = ipField.getText();
                        }
                        Socket socket = new Socket(ip, Const.Port);
                        client = new Client(socket, frame, nameField.getText());
                        textField.setEnabled(true);
                        ipField.setEnabled(false);
                    } catch (Exception exception) {
                        setErrorMessage(new Message("","Please, enter valued ip"));
                    }
                }
            }
        });

        add(northPanel, BorderLayout.NORTH);
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout());
        messageArea = new JTextArea();
        messageArea.setLineWrap(true);
        messageArea.setEditable(false);
        JScrollPane areaScroll = new JScrollPane(messageArea);
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        Message message = new Message(nameField.getText(),textField.getText());
                        client.sendMessage(message);
                        textField.setText("");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        southPanel.add(areaScroll, BorderLayout.CENTER);
        southPanel.add(textField, BorderLayout.SOUTH);
        add(southPanel);
    }


    public void setMessage(Message message) {
        String text = message.getText();
        String author = message.getAuthor();
        if (text.trim().length() != 0 && !text.equals("")) {
            messageArea.append(author.trim()+ ": " + text.trim() + "\n");
        }
    }

    public void setErrorMessage(Message message){
        String text = message.getText();
        if (text.trim().length() != 0 && !text.equals("")) {
            messageArea.append(text.trim() + "\n");
        }
    }
}
