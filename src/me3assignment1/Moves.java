package me3assignment1;

public class Moves {
    /**
     * 
     * @param states
     * @return 
     */
    public static boolean ModifyLegal(Game.e[] states) {
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
