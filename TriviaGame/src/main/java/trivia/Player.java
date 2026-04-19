package trivia;

public class Player {

    private final String name;
    private int position = 0;
    private int coins = 0;
    private boolean inPenaltyBox = false;

    public Player(String name) {
        this.name = name;
    }

    public String name() { return name; }
    public int position() { return position; }
    public int coins() { return coins; }
    public boolean isInPenaltyBox() { return inPenaltyBox; }

    public void move(int roll, int boardSize) {
        position = (position + roll) % boardSize;
    }

    public void addCoin() {
        coins++;
    }

    public boolean hasWon(int winningCount) {
        return coins >= winningCount;
    }

    public void sendToPenaltyBox() {
        inPenaltyBox = true;
    }
}