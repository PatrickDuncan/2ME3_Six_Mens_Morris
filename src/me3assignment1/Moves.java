package me3assignment1;

public class Moves implements IMoves {

    /**
     * Whether or not the modified board is legal based on the rules of the
     * game.
     *
     * @param s the enum array that holds whether or not their point on a board
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

    /**
     * Checks if the user can move anymore
     *
     * @param s the enum array that holds whether or not their point on a board
     * is empty, a red discs or a blue disc.
     * @param p1 the source point
     * @param p2 the destination point
     * @return whether they can move to a specific point
     */
    public boolean checkMovement(Game.states[] s, int p1, int p2) {
        System.out.println(p1 + " " + p2);
        if (s[p2] == Game.states.none) {
            switch (p1) {
                case 0:
                    if (p2 == 1 || p2 == 6)
                        return true;
                    break;
                case 1:
                    if (p2 == 0 || p2 == 2 || p2 == 4)
                        return true;
                    break;
                case 2:
                    if (p2 == 1 || p2 == 9)
                        return true;
                    break;
                case 3:
                    if (p2 == 4 || p2 == 7)
                        return true;
                    break;
                case 4:
                    if (p2 == 1 || p2 == 3 || p2 == 5)
                        return true;
                    break;
                case 5:
                    if (p2 == 4 || p2 == 8)
                        return true;
                    break;
                case 6:
                    if (p2 == 0 || p2 == 7 || p2 == 13)
                        return true;
                    break;
                case 7:
                    if (p2 == 3 || p2 == 6 || p2 == 10)
                        return true;
                    break;
                case 8:
                    if (p2 == 5 || p2 == 9 || p2 == 12)
                        return true;
                    break;
                case 9:
                    if (p2 == 2 || p2 == 8 || p2 == 15)
                        return true;
                    break;
                case 10:
                    if (p2 == 7 || p2 == 11)
                        return true;
                    break;
                case 11:
                    if (p2 == 10 || p2 == 12 || p2 == 14)
                        return true;
                    break;
                case 12:
                    if (p2 == 8 || p2 == 11)
                        return true;
                    break;
                case 13:
                    if (p2 == 6 || p2 == 14)
                        return true;
                    break;
                case 14:
                    if (p2 == 11 || p2 == 13 || p2 == 15)
                        return true;
                    break;
                case 15:
                    if (p2 == 9 || p2 == 14)
                        return true;
                    break;
            }
            return false;
        }
        else
            return false;
    }

    /**
     * Checks if the user can't move anymore
     *
     * @param s the enum array that holds whether or not their point on a board
     * is empty, a red discs or a blue disc.
     */
    public void checkAdjency(Game.states[] s) {
        for (int i = 0; i < s.length; i++) {

        }
    }

    /**
     * Checks if the there are Mills
     *
     * @param s the enum array that holds whether or not their point on a board
     * is empty, a red discs or a blue disc.
     */
    public void checkMills(Game.states[] s) {
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
