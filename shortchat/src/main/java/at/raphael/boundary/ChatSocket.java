package at.raphael.boundary;


import at.raphael.entity.Chat;
import at.raphael.entity.Chatter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/chat/{username}/{chatId}")
@ApplicationScoped
public class ChatSocket {

    protected List<Chat> chats = new ArrayList<>();


    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username, @PathParam("chatId") String chatId) {
        Optional<Chat> c = chats.stream().filter(element -> Objects.equals(element.id, chatId)).findFirst();

        if(c.isPresent()){
            c.get().chatter.add(new Chatter(session, username));
        }else {
            Chat newChat = new Chat(chatId, new ArrayList<>(), new ArrayList<>());
            newChat.chatter.add(new Chatter(session, username));
            chats.add(newChat);
        }

        broadcast("User " + username + " joined", chatId);

    }

    @OnClose
    public void onClose(Session session, @PathParam("username") String username, @PathParam("chatId") String chatId) {
        Optional<Chat> c = chats.stream().filter(element -> element.id.equals(chatId)).findFirst();

        c.ifPresent(chat -> chat.chatter.removeIf(element -> element.name.equals(username)));

        broadcast("User " + username + " left", chatId);
    }


    @OnError
    public void onError(Session session, @PathParam("username") String username, @PathParam("chatId") String chatId, Throwable throwable) {
        Optional<Chat> c = chats.stream().filter(element -> element.id.equals(chatId)).findFirst();

        c.ifPresent(chat -> chat.chatter.removeIf(element -> element.name.equals(username)));

        broadcast("User " + username + " left on error: " + throwable, chatId);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("username") String username, @PathParam("chatId") String chatId) {
        broadcast(username+ ";" + message, chatId);
    }

    private void broadcast(String message, String chatId) {

        Optional<Chat> c = chats.stream().filter(chat -> chat.id.equals(chatId)).findFirst();

        if(c.isPresent()) {
            List<Session> sessions = c.get().chatter.stream().map(element -> element.session).toList();
            sessions.forEach(s -> {
                s.getAsyncRemote().sendObject(message, result -> {
                    if (result.getException() != null) {
                        System.out.println("Unable to send message: " + result.getException());
                    }
                });
            });
        }
    }
}
