package factory;

import model.Fight;
import model.Player;

public class FightFactory {

    public static Fight newFight(Player player1, Player player2) {
        Fight fight = new Fight();
        fight.initialize(player1, player2);
        return fight;
    }
}
