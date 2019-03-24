package ee.moog.mugloar.model;

public class Reputation {
    public int people;
    public int state;
    public int underworld;

    public String describe() {
        return "People: " + people + " State: " + state + " Mob: " + underworld;
    }
}
