package at.raphael.entity;

import jakarta.websocket.Session;

import java.util.List;

public class Chat {

    public String id;
    public List<Message> messageList;
    public List<Chatter> chatter;


    public Chat() {
    }

    public Chat(String id, List<Message> messageList, List<Chatter> chatter) {
        this.id = id;
        this.messageList = messageList;
        this.chatter = chatter;
    }
}
