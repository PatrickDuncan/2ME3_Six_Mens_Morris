package me3assignment1;

public interface IMoves {
    // Whether or not the modified board is legal based on the rules of the game.
    boolean modifyLegal(Game.states[] states);
    // Checks if the user can move to a certain location
    boolean checkMovement(Game.states[] states, int p1, int p2);
    // Checks if the user can't move anymore
    Game.states checkBlocked(Game.states[] s);
    // Checks if the there are mills on the board
    Game.states[] checkMills(Game.states[] s);
}
