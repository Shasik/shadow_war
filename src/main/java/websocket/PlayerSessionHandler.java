package websocket;

import factory.FightFactory;
import database.User;
import model.Fight;
import model.Player;

import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.spi.JsonProvider;
import javax.websocket.Session;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class PlayerSessionHandler {
    private final static HashMap<Session, Fight> FIGHTHASHMAP = new HashMap<>();
    private final static Map<Session, Player> PLAYERS = new ConcurrentHashMap<>();

    private void sendToSession(Session session, JsonObject message) {
        try {
            session.getBasicRemote().sendText(message.toString());
        } catch (IOException ex) {
            Logger.getLogger(PlayerSessionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void handlerClose(Session session) {
        //В случае неожиданного завершения игры одним из игроков необходимо удалить его
        FIGHTHASHMAP.remove(session);
    }

    public void registerDuel(Session session, JsonObject message) {
        if (PLAYERS.containsKey(session)) {
            return;
        }

        //Также проверка по никнейму
        String nickname = message.getString("nickname");
        for (Map.Entry<Session, Player> pair : PLAYERS.entrySet()) {
            if (pair.getValue().getNickname().equals(nickname)) {
                return;
            }
        }

        //Игрока нет в списке. Добавляем
        PLAYERS.put(session, User.getUserByNickname(nickname));

        //если в списке 2 игрока, то инициализируем драку, если нет, то инициализируем экран текущему игроку
        if (PLAYERS.size() >= 2) {
            initFight(session);
        } else {
            initFirstPlayerScreen(session);
        }
    }

    private void initFirstPlayerScreen(Session session) {
        JsonProvider provider = JsonProvider.provider();

        Player player = PLAYERS.get(session);

        JsonObjectBuilder renderScreen = provider.createObjectBuilder()
                .add("action", "initScreen")
                .add("your_nickname", String.valueOf(player.getNickname()))
                .add("your_health", String.valueOf(player.getHp()))
                .add("your_damage", String.valueOf(player.getDamage()));

        sendToSession(session, renderScreen.build());
    }

    private void initFight(Session session) {
        Player firstPlayer = PLAYERS.get(session);

        firstPlayer.setSession(session);
        firstPlayer.setSessionId(session.getId());

        Player secondPlayer = null;
        Session secondSession = null;
        for (Map.Entry<Session, Player> pair : PLAYERS.entrySet()) {
            if (pair.getKey() != session) {
                secondSession = pair.getKey();
                secondPlayer = pair.getValue();
                secondPlayer.setSessionId(secondSession.getId());
                secondPlayer.setSession(secondSession);
                break;
            }
        }

        //Сама инициализация. Получаем Fight с двумя игроками и записываем их в MAP, затем отправляем пакет отрисовки экрана
        Fight fight = FightFactory.newFight(firstPlayer, secondPlayer);
        FIGHTHASHMAP.put(session, fight);
        FIGHTHASHMAP.put(secondSession, fight);
        PLAYERS.remove(session);
        PLAYERS.remove(secondSession);
        renderScreenOnFight(session, secondSession, fight);
    }

    private void renderScreenOnFight(Session session1, Session session2, Fight fight) {
        JsonProvider provider = JsonProvider.provider();

        Player player1 = fight.getFirst();
        Player player2 = fight.getSecond();

        //Изначальная отрисовка экрана. У игрока он всегда отображается слева, а враг справа
        JsonObjectBuilder renderScreen = provider.createObjectBuilder()
                .add("action", "fight")
                .add("your_nickname", player1.getNickname())
                .add("your_health", String.valueOf(player1.getHp()))
                .add("your_damage", String.valueOf(player1.getDamage()))
                .add("enemy_nickname", player2.getNickname())
                .add("enemy_health", String.valueOf(player2.getHp()))
                .add("enemy_damage", String.valueOf(player2.getDamage()))
                .add("step", "yes");

        JsonObjectBuilder renderScreen2 = provider.createObjectBuilder()
                .add("action", "fight")
                .add("your_nickname", player2.getNickname())
                .add("your_health", String.valueOf(player2.getHp()))
                .add("your_damage", String.valueOf(player2.getDamage()))
                .add("enemy_nickname", player1.getNickname())
                .add("enemy_health", String.valueOf(player1.getHp()))
                .add("enemy_damage", String.valueOf(player1.getDamage()))
                .add("step", "no");

        sendToSession(session1, renderScreen.build());
        sendToSession(session2, renderScreen2.build());
    }

    //Метод, который вызывается при каждом нажатии кнопки атаки во время битвы
    public void attack(Session session, JsonObject message) {
        Session enemy = null;
        int damage;

        Fight fight = FIGHTHASHMAP.get(session);

        //Вебсокет присылает пакет в котором указано, кто атаковал последним, на основании этого вычисляется "текущий" ударяющий
        if (message.getString("attack").equals("first")) {
            damage = fight.firstKick();
        } else {
            damage = fight.secondKick();
        }

        for (Map.Entry<Session, Fight> pair : FIGHTHASHMAP.entrySet()) {
            if (pair.getValue().equals(fight) && pair.getKey() != session) {
                enemy = pair.getKey();
                break;
            }
        }

        //Проверяем, окончилась ли драка
        Player winner = fight.getWinner();
        Player loser = fight.getLoser();

        /*  Если нет, то отправляем пакет для смены кнопки атаки и отрисоки новых значений ХП
            Если окончилась, то вызываем финиш */
        if (winner == null) {
            swapAttackButtonAndRefreshHealthPoints(session, enemy, damage, fight);
        } else {
            finishFight(winner, loser, damage);
        }
    }

    private void swapAttackButtonAndRefreshHealthPoints(Session current, Session enemy, int damage, Fight fight) {
        JsonProvider provider = JsonProvider.provider();

        JsonObjectBuilder renderScreen = provider.createObjectBuilder()
                .add("action", "attack")
                .add("your_health", String.valueOf(fight.getFirst().getHp()))
                .add("enemy_health", String.valueOf(fight.getSecond().getHp()))
                .add("damage", damage);


        JsonObjectBuilder renderScreen2 = provider.createObjectBuilder()
                .add("action", "attack")
                .add("your_health", String.valueOf(fight.getSecond().getHp()))
                .add("enemy_health", String.valueOf(fight.getFirst().getHp()))
                .add("damage", damage);


        //Меняем местами кнопки атаки, задаём свежие значения ХП и отправляем в вебсокет
        if (fight.getFirst().getSessionId().equals(current.getId())) {
            renderScreen.add("step", "no");
            renderScreen2.add("step", "yes");
            sendToSession(current, renderScreen.build());
            sendToSession(enemy, renderScreen2.build());
        } else {
            renderScreen.add("step", "yes");
            renderScreen2.add("step", "no");
            sendToSession(current, renderScreen2.build());
            sendToSession(enemy, renderScreen.build());
        }
    }

    //Отправляем в вебсокет Финиш пакет для отрисовки финальных значений и вносим изменения в БД
    private void finishFight(Player winner, Player loser, int damage) {
        JsonProvider provider = JsonProvider.provider();

        JsonObject redirectWinner = provider.createObjectBuilder()
                .add("action", "finishWin")
                .add("damage", damage)
                .build();
        JsonObject redirectLoser = provider.createObjectBuilder()
                .add("action", "finishLose")
                .add("damage", damage)
                .build();

        User.updateCharactersAfterFight(winner, true);
        User.updateCharactersAfterFight(loser, false);

        sendToSession(winner.getSession(), redirectWinner);
        sendToSession(loser.getSession(), redirectLoser);
    }
}
