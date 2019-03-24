package ee.moog.mugloar.model;

public class SolveResult {
    public boolean success;
    public int lives;
    public int gold;
    public int score;
    public int highScore;
    public int turn;
    public String message;

    public String describe() {
        return success ? "Success" : "Fail" + ": lives: " + lives + " gold: " + gold + " score: " + score;
    }
}
