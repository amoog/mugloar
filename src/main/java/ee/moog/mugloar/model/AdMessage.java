package ee.moog.mugloar.model;

public class AdMessage {
    public String adId;
    public String message;
    public int reward;
    public int expiresIn;
    public String probability;

    public boolean encrypted;

    public String describe() {
        return adId + ". reward: " + reward + " prob: " + probability;
    }
}
