package client;


import common.Message;


import java.io.ObjectInputStream;

public class Resender {
    private ObjectInputStream in;
    boolean stoped;
    private Client client;

    public Resender(Client c) throws Exception {
        stoped = false;
        client = c;
        in = c.getInputStream();
        Thread thread = new Thread() {

            @Override
            public void run() {
                while (!stoped) {
                    try {
                        Message message = (Message)in.readObject();
                        if (!message.getAuthor().equals("")) {
                            client.setMessage(message);
                        } else {
                            client.setErrorMessage(message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }



    public void stopResender(){
        stoped = true;
    }
}
