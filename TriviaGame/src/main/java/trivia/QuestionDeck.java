package trivia;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.Map;

public class QuestionDeck {

    private static final int QUESTIONS_PER_CATEGORY = 50;

    private final Map<Category, LinkedList<String>> questions = new EnumMap<>(Category.class);

    public QuestionDeck() {
        for (Category category : Category.values()) {
            LinkedList<String> deck = new LinkedList<>();
            for (int i = 0; i < QUESTIONS_PER_CATEGORY; i++) {
                deck.addLast(category + " Question " + i);
            }
            questions.put(category, deck);
        }
    }

    public void ask(Category category) {
        System.out.println(questions.get(category).removeFirst());
    }
}