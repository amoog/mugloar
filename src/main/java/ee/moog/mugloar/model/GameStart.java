package ee.moog.mugloar.model;

public class GameStart {
    public String gameId;
    public int lives;
    public int gold;
    public int level;
    public int score;
    public int highScore;
    public int turn;

    public String describe() {
        return "lives: " + lives + " gold:" + gold + " level:" + level + " score:" + score;
    }
}
