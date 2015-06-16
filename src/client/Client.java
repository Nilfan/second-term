package client;

import common.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;



public class Client {
    private Socket socket;
    private ClientGUI guiFrame;
    private Resender resend;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String name;

    public Client(Socket s, ClientGUI frame, String name) {
        guiFrame = frame;
        this.name = name;
        try{
            socket = s;
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());

            sendMessage(new Message(name, ""));
            resend = new Resender(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    protected void sendMessage(Message message) throws IOException {
        out.writeObject(message);
    }

    protected void setMessage(Message message){
        guiFrame.setMessage(message);
    }

    protected void setErrorMessage(Message message){
        guiFrame.setErrorMessage(message);
    }

    protected Message receiveMessage() throws Exception{
        return (Message)in.readObject();
    }

    protected ObjectInputStream getInputStream(){
        return in;
    }

    protected void close() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (Exception e) {
            System.err.println("Ошибка закрытия !");
        }
    }

}
