package at.raphael.entity;


public class Message {
    public String message;
    public String sendDateTime;
    public String sender;


    public Message() {
    }

    public Message(String message, String sendDateTime, String sender) {
        this.message = message;
        this.sendDateTime = sendDateTime;
        this.sender = sender;
    }
}
