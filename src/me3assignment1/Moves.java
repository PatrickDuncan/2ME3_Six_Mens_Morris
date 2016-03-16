package me3assignment1;

public class Moves implements IMoves {

    /**
     * Whether or not the modified board is legal based on the rules of the
     * game.
     *
     * @param s the enum array that holds whether or not there point on a board
     * is empty, a red discs or a blue disc.
     * @return whether or not the modified board is legal.
     */
    @Override
    public boolean modifyLegal(Game.states[] s) {
        int rCount, bCount;
        rCount = bCount = 0;
        for (Game.states state : s) {
            if (state == Game.states.blue) {
                ++bCount;
            } else if (state == Game.states.red) {
                ++rCount;
            }
            if (rCount > 6 || bCount > 6)
                return false;
        }
        return true;
    }

    public void Check(Game.states[] s) {
        int j = 0;
        for (int i = 0; i < 4; i++) {
            //if (s)
            if (s[i + j] == s[i + j + 1] && s[i + j] == s[i + j + 2]
                    && s[i + j] != Game.states.none)
                System.out.println("There is a Mill in row " + (i + 1) + ".");
            if (i < 1 || i > 1)
                j += 2;
            else
                j += 6;
        }
        if (s[0] != Game.states.none && s[3] != Game.states.none && s[5]
                != Game.states.none && s[2] != Game.states.none) {
            if (s[0] == s[6] && s[0] == s[13])
                System.out.println("There is a Mill in collumn 1.");
            if (s[3] == s[7] && s[3] == s[10])
                System.out.println("There is a Mill in collumn 2.");
            if (s[5] == s[8] && s[5] == s[12])
                System.out.println("There is a Mill in collumn 3.");
            if (s[2] == s[9] && s[2] == s[15])
                System.out.println("There is a Mill in collumn 4.");
        }
    }
}
