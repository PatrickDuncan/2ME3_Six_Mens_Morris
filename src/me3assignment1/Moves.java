package me3assignment1;

public class Moves implements IMoves{
    /**
     * Whether or not the modified board is legal based on the rules of the game.
     * @param states the enum array that holds whether or not there point on a
     * board is empty, a red discs or a blue disc.
     * @return whether or not the modified board is legal.
     */
    public boolean ModifyLegal(Game.e[] states) {
        int rCount, bCount;
        rCount = bCount = 0;
        for (Game.e state : states) {
            if (state == Game.e.blue) {
                ++bCount;
            }
            else if (state == Game.e.red) {
                ++rCount;
            }
            if (rCount > 6 || bCount > 6) return false;
        }
        return true;
    }
}
