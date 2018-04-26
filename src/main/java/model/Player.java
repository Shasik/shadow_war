package model;

import javax.websocket.Session;
import java.util.concurrent.ThreadLocalRandom;

public class Player {
    private int hp;
    private int damage;
    private int rating;
    private String sessionId;
    private Session session;
    private String nickname;

    public Player() {
    }

    public int kick(Player player) {
        int kickDamage = damage + ThreadLocalRandom.current().nextInt(-3, 4);
        player.setHp(player.getHp() - kickDamage);
        return kickDamage;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    public String toString() {
        return "Nickname = " + nickname + ". " + "Health Point = " + hp + ". " + "Damage = " + damage + ". " + "Session id = " + sessionId;
    }
}
