package at.raphael.entity;


import jakarta.websocket.Session;

public class Chatter {

    public Session session;
    public String name;

    public Chatter(Session session, String name) {
        this.session = session;
        this.name = name;
    }

    public Chatter() {
    }
}
