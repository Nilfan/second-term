package server;

import common.Message;

import java.io.*;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;




public class Connection {
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Socket socket;
    private String name = "";
    private List<Connection> connections;


    public Connection(Socket s, final List<Connection> connections) throws Exception {
        System.out.println("Creating of connection");
        this.socket = s;
        this.connections = connections;
        System.out.println("Trying soon");
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            System.out.println("Waiting for name");
            name = ((Message) in.readObject()).getAuthor();
            System.out.println("Name is here");

            synchronized (connections) {
                Iterator<Connection> iter = connections.iterator();
                while (iter.hasNext()) {
                    Message message = new Message("", iter.next().getConnectionName() + " is in chat");
                    sendMessage(message);
                }
            }
            synchronized (connections) {
                Iterator<Connection> iter = connections.iterator();
                while (iter.hasNext()) {
                    Message message = new Message("", "<<" + name + " cames now>>");
                    ((Connection) iter.next()).sendMessage(message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            close();
        }

        Thread thread = new Thread(){

            @Override
            public void run() {
                try {
                    System.out.println("Starting of thread");


                    while (true) {
                        Message m = (Message)in.readObject();
                        if (m.getText().equals("exit")) break;
                        synchronized (connections) {
                            Iterator<Connection> iter = connections.iterator();
                            while (iter.hasNext()) {
                                ((Connection) iter.next()).sendMessage(m);
                            }
                        }
                    }
                    synchronized (connections) {
                        Iterator<Connection> iter = connections.iterator();
                        while (iter.hasNext()) {
                            Message message = new Message("","<<" + name + " has left>>" );
                            ((Connection) iter.next()).sendMessage(message);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    close();
                }
            }
        };
        thread.start();


    }

    public void sendMessage(Message message) throws IOException {
        out.writeObject(message);
    }

    public String getConnectionName(){
        return name;
    }



    public void close() {
        try {
            in.close();
            out.close();
            socket.close();
            connections.remove(this);
        } catch (Exception e) {
            System.err.println("Ошибка закрытия соединений.");
        }
    }
}