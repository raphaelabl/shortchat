package at.raphael.boundary;

import at.raphael.entity.Chat;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import java.util.Optional;

@Path("util")
public class UtilResource {

    @Inject
    ChatSocket chatSocket;

    @GET
    @Path("checkChatUse/{chatId}")
    @Produces("application/json")
    public Response checkChatExists(@PathParam("chatId") String chatId) {
        Optional<Chat> chat = chatSocket.chats.stream().filter(c -> c.id.equals(chatId)).findFirst();

        if (chat.isPresent()) {
            return Response.ok(true).build();
        } else {
            return Response.ok(false).build();
        }
    }

}
