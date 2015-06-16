package server;

import common.Const;
import common.Message;

import java.net.*;
import java.util.*;


public class Server {

    private List<Connection> connections =  Collections.synchronizedList(new ArrayList<Connection>());
    private ServerSocket server;
    private boolean toStop = false;
    private IPFrame frame;

    public void setStop() throws Exception{
        toStop = true;
        Socket s = new Socket("localhost", Const.Port);
    }

    public Server() throws Exception{
        server = new ServerSocket(Const.Port);
        System.out.println("Server is working");
        while (true) {
            System.out.println("Loop");
            if (toStop) break;
            Socket socket = server.accept();
            Connection con = new Connection(socket, connections);


            connections.add(con);
        }
        closeAll();
    }

    public void closeAll() {
        try {
            server.close();
            synchronized(connections) {
                Iterator<Connection> iter = connections.iterator();
                while(iter.hasNext()) {
                    ((Connection) iter.next()).close();
                }
            }
        } catch (Exception e) {
            System.err.println("Closing error");
        }
    }

    public boolean valuedName(String newName){
        for(Connection name: connections){
            if(name.equals(newName)){
                return false;
            }
        }
        return true;
    }


}
