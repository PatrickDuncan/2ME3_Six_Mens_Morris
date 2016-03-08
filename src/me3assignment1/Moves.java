package me3assignment1;

public class Moves implements IMoves{
    /**
     * Whether or not the modified board is legal based on the rules of the game.
     * @param s the enum array that holds whether or not there point on a
     * board is empty, a red discs or a blue disc.
     * @return whether or not the modified board is legal.
     */
    @Override
    public boolean modifyLegal(Game.states[] s) {
        int rCount, bCount;
        rCount = bCount = 0;
        for (Game.states state : s) {
            if (state == Game.states.blue) {
                ++bCount;
            }
            else if (state == Game.states.red) {
                ++rCount;
            }
            if (rCount > 6 || bCount > 6) return false;
        }
        return true;
    }
}
