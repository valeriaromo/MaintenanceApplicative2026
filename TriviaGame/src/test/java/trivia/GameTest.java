package trivia;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    private Game game;
    private ByteArrayOutputStream output;

    @BeforeEach
    void setUp() {
        game = new Game();
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        System.setOut(System.out);
    }


    @Nested
    class PlayerManagement {

        @Test
        void addingAPlayerIncreasesPlayerCount() {
            game.add("Alice");
            assertEquals(1, game.howManyPlayers());
        }

        @Test
        void gameIsNotPlayableWithOnlyOnePlayer() {
            game.add("Alice");
            assertFalse(game.isPlayable());
        }

        @Test
        void gameIsPlayableWithTwoPlayers() {
            game.add("Alice");
            game.add("Bob");
            assertTrue(game.isPlayable());
        }

        @Test
        void cannotAddMoreThanMaxPlayers() {
            for (int i = 0; i < 7; i++) game.add("Player" + i);
            assertEquals(6, game.howManyPlayers());
        }

        @Test
        void addingPlayerPrintsName() {
            game.add("Alice");
            assertTrue(output.toString().contains("Alice was added"));
        }

        @Test
        void addingPlayerPrintsPlayerNumber() {
            game.add("Alice");
            game.add("Bob");
            assertTrue(output.toString().contains("They are player number 2"));
        }
    }


    @Nested
    class Movement {

        @BeforeEach
        void addPlayers() {
            game.add("Alice");
            game.add("Bob");
        }

        @Test
        void rollPrintsCurrentPlayer() {
            game.roll(3);
            assertTrue(output.toString().contains("Alice is the current player"));
        }

        @Test
        void rollPrintsRollValue() {
            game.roll(4);
            assertTrue(output.toString().contains("They have rolled a 4"));
        }

        @Test
        void playerMovesToCorrectPosition() {
            game.roll(3);
            assertTrue(output.toString().contains("Alice's new location is 3"));
        }

        @Test
        void boardWrapsAroundAt12() {
            game.roll(6);
            game.handleCorrectAnswer();
            game.roll(1);
            game.handleCorrectAnswer();
            game.roll(6);
            assertTrue(output.toString().contains("Alice's new location is 0"));
        }
    }


    @Nested
    class Categories {

        @BeforeEach
        void addPlayers() {
            game.add("Alice");
            game.add("Bob");
        }

        @Test
        void position0IsPop() {
            game.roll(4);
            assertTrue(output.toString().contains("The category is Pop"));
        }

        @Test
        void position1IsScience() {
            game.roll(1);
            assertTrue(output.toString().contains("The category is Science"));
        }

        @Test
        void position2IsSports() {
            game.roll(2);
            assertTrue(output.toString().contains("The category is Sports"));
        }

        @Test
        void position3IsRock() {
            game.roll(3);
            assertTrue(output.toString().contains("The category is Rock"));
        }

        @Test
        void categoryRepeatsEvery4Positions() {
            game.roll(4); // pos 4 → Pop
            assertTrue(output.toString().contains("The category is Pop"));
        }
    }


    @Nested
    class CorrectAnswer {

        @BeforeEach
        void addPlayers() {
            game.add("Alice");
            game.add("Bob");
        }

        @Test
        void correctAnswerAddsCoin() {
            game.roll(1);
            game.handleCorrectAnswer();
            assertTrue(output.toString().contains("Alice now has 1 Gold Coins."));
        }

        @Test
        void correctAnswerPrintsCorrect() {
            game.roll(1);
            game.handleCorrectAnswer();
            assertTrue(output.toString().contains("Answer was correct!!!!"));
        }

        @Test
        void gameNotOverBefore6Coins() {
            game.roll(1);
            boolean notOver = game.handleCorrectAnswer();
            assertTrue(notOver);
        }

        @Test
        void gameOverWhenPlayerReaches6Coins() {
            for (int i = 0; i < 5; i++) {
                game.roll(1);
                game.handleCorrectAnswer();
                game.roll(1);
                game.handleCorrectAnswer();
            }
            game.roll(1);
            boolean notOver = game.handleCorrectAnswer();
            assertFalse(notOver);
        }

        @Test
        void turnPassesToNextPlayerAfterCorrectAnswer() {
            game.roll(1);
            game.handleCorrectAnswer();
            game.roll(2);
            assertTrue(output.toString().contains("Bob is the current player"));
        }
    }


    @Nested
    class WrongAnswerAndPenaltyBox {

        @BeforeEach
        void addPlayers() {
            game.add("Alice");
            game.add("Bob");
        }

        @Test
        void wrongAnswerSendsPlayerToPenaltyBox() {
            game.roll(1);
            game.wrongAnswer();
            assertTrue(output.toString().contains("Alice was sent to the penalty box"));
        }

        @Test
        void wrongAnswerDoesNotEndGame() {
            game.roll(1);
            boolean notOver = game.wrongAnswer();
            assertTrue(notOver);
        }

        @Test
        void oddRollGetsPlayerOutOfPenaltyBox() {
            game.roll(1);
            game.wrongAnswer();
            game.roll(1);
            game.handleCorrectAnswer();
            game.roll(1);
            assertTrue(output.toString().contains("Alice is getting out of the penalty box"));
        }

        @Test
        void evenRollKeepsPlayerInPenaltyBox() {
            game.roll(1);
            game.wrongAnswer();
            game.roll(1);
            game.handleCorrectAnswer();
            game.roll(2);
            assertTrue(output.toString().contains("Alice is not getting out of the penalty box"));
        }

        @Test
        void playerInPenaltyBoxWithEvenRollSkipsTurn() {
            game.roll(1);
            game.wrongAnswer();
            game.roll(1);
            game.handleCorrectAnswer();
            game.roll(2);
            game.handleCorrectAnswer();
            assertFalse(output.toString().contains("Alice now has 1 Gold Coins."));
        }

        @Test
        void playerGettingOutOfPenaltyBoxCanEarnCoin() {
            game.roll(1);
            game.wrongAnswer();
            game.roll(1);
            game.handleCorrectAnswer();
            game.roll(1);
            game.handleCorrectAnswer();
            assertTrue(output.toString().contains("Alice now has 1 Gold Coins."));
        }
    }


    @Nested
    class TurnRotation {

        @Test
        void turnsRotateAmongAllPlayers() {
            game.add("Alice");
            game.add("Bob");
            game.add("Carol");

            game.roll(1); game.handleCorrectAnswer();
            game.roll(1); game.handleCorrectAnswer();
            game.roll(1); game.handleCorrectAnswer();
            game.roll(1);

            String out = output.toString();
            int aliceFirst  = out.indexOf("Alice is the current player");
            int bobTurn     = out.indexOf("Bob is the current player");
            int carolTurn   = out.indexOf("Carol is the current player");
            int aliceSecond = out.lastIndexOf("Alice is the current player");

            assertTrue(aliceFirst < bobTurn);
            assertTrue(bobTurn < carolTurn);
            assertTrue(carolTurn < aliceSecond);
        }

        @Test
        void turnsWrapBackToFirstPlayer() {
            game.add("Alice");
            game.add("Bob");

            game.roll(1); game.handleCorrectAnswer();
            game.roll(1); game.handleCorrectAnswer();
            game.roll(1);

            long aliceCount = output.toString()
                    .lines()
                    .filter(l -> l.equals("Alice is the current player"))
                    .count();
            assertEquals(2, aliceCount);
        }
    }
}