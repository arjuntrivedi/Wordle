package project.wordle;

public class GameAlreadyOverException extends IllegalStateException {
    public GameAlreadyOverException(String message) {
        super(message);
    }
}
