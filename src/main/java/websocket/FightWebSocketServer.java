package websocket;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
@ServerEndpoint("/actions")
public class FightWebSocketServer {

    @Inject
    private
    PlayerSessionHandler playersHandler;

    @OnOpen
    public void open(Session session) {
    }

    @OnClose
    public void close(Session session) {
        playersHandler.handlerClose(session);
    }

    @OnError
    public void error(Throwable error) {
        Logger.getLogger(PlayerSessionHandler.class.getName()).log(Level.SEVERE, null, error);
    }

    @OnMessage
    public void handleMessage(String message, Session session) {
        try (JsonReader reader = Json.createReader(new StringReader(message))) {
            JsonObject jsonMessage = reader.readObject();

            if ("attack".equals(jsonMessage.getString("action"))) {
                playersHandler.attack(session, jsonMessage);
            }

            if ("registerDuel".equals(jsonMessage.getString("action"))) {
                playersHandler.registerDuel(session, jsonMessage);
            }
        }
    }
}
