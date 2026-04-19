package trivia;

public enum Category {
    POP, SCIENCE, SPORTS, ROCK;

    public static Category forPosition(int position) {
        return switch (position % 4) {
            case 0 -> POP;
            case 1 -> SCIENCE;
            case 2 -> SPORTS;
            default -> ROCK;
        };
    }

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}