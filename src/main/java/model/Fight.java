package model;

public class Fight {
    private volatile Player first;
    private volatile Player second;
    private volatile Player winner = null;
    private volatile Player loser = null;

    public void initialize(Player player1, Player player2) {
        first = player1;
        second = player2;
    }

    public int firstKick() {
        int damage = first.kick(second);
        if (second.getHp() <= 0) {
            winner = first;
            loser = second;
        }
        return damage;
    }

    public int secondKick() {
        int damage = second.kick(first);
        if (first.getHp() <= 0) {
            winner = second;
            loser = first;
        }
        return damage;
    }

    public Player getFirst() {
        return first;
    }

    public Player getSecond() {
        return second;
    }

    public Player getWinner() {
        return winner;
    }

    public Player getLoser() {
        return loser;
    }
}
