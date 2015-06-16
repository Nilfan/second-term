package common;


import java.io.Serializable;

public class Message implements Serializable {
    private String author;
    private String text;
    private long time;

    public Message(String author, String text){
        this.author = author;
        this.text = text;
        this.time = System.currentTimeMillis();
    }

    public String getAuthor(){
        return author;
    }

    public String getText(){
        return text;
    }

}
