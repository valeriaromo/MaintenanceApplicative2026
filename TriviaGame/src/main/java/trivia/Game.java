package trivia;

import java.util.ArrayList;
import java.util.List;

public class Game implements IGame {

    private static final int BOARD_SIZE = 12;
    private static final int WINNING_COIN_COUNT = 6;
    private static final int MAX_PLAYERS = 6;

    private final List<Player> players = new ArrayList<>();
    private final QuestionDeck questionDeck = new QuestionDeck();

    private int currentPlayerIndex = 0;
    private boolean isGettingOutOfPenaltyBox;

    @Override
    public boolean add(String playerName) {
        if (players.size() >= MAX_PLAYERS) return false;
        players.add(new Player(playerName));
        System.out.println(playerName + " was added");
        System.out.println("They are player number " + players.size());
        return true;
    }

    public boolean isPlayable() {
        return players.size() >= 2;
    }

    public int howManyPlayers() {
        return players.size();
    }

    @Override
    public void roll(int roll) {
        Player player = currentPlayer();
        System.out.println(player.name() + " is the current player");
        System.out.println("They have rolled a " + roll);

        if (player.isInPenaltyBox()) {
            if (roll % 2 != 0) {
                isGettingOutOfPenaltyBox = true;
                System.out.println(player.name() + " is getting out of the penalty box");
                moveAndAsk(player, roll);
            } else {
                System.out.println(player.name() + " is not getting out of the penalty box");
                isGettingOutOfPenaltyBox = false;
            }
        } else {
            moveAndAsk(player, roll);
        }
    }

    private void moveAndAsk(Player player, int roll) {
        player.move(roll, BOARD_SIZE);
        System.out.println(player.name() + "'s new location is " + player.position());
        System.out.println("The category is " + currentCategory());
        questionDeck.ask(currentCategory());
    }

    private Category currentCategory() {
        return Category.forPosition(currentPlayer().position());
    }

    @Override
    public boolean handleCorrectAnswer() {
        Player player = currentPlayer();
        if (player.isInPenaltyBox() && !isGettingOutOfPenaltyBox) {
            advancePlayer();
            return true;
        }
        System.out.println("Answer was correct!!!!");
        player.addCoin();
        System.out.println(player.name() + " now has " + player.coins() + " Gold Coins.");
        boolean gameNotOver = !player.hasWon(WINNING_COIN_COUNT);
        advancePlayer();
        return gameNotOver;
    }

    @Override
    public boolean wrongAnswer() {
        Player player = currentPlayer();
        System.out.println("Question was incorrectly answered");
        System.out.println(player.name() + " was sent to the penalty box");
        player.sendToPenaltyBox();
        advancePlayer();
        return true;
    }

    private Player currentPlayer() {
        return players.get(currentPlayerIndex);
    }

    private void advancePlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }
}