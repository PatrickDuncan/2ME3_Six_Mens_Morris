package me3assignment1;

public interface IMoves {
    boolean modifyLegal(Game.states[] states);
    boolean checkMovement(Game.states[] states, int p1, int p2);
    Game.states checkBlocked(Game.states[] s);
    Game.states[] checkMills(Game.states[] s);
}
