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
     * Checks if the user can move to a certain location
     *
     * @param s the enum array that holds whether or not their point on a board
     * is empty, a red discs or a blue disc.
     * @param p1 the source point
     * @param p2 the destination point
     * @return whether they can move to a specific point
     */
    @Override
    public boolean checkMovement(Game.states[] s, int p1, int p2) {
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
        } else
            return false;
    }

    /**
     * Checks if the user can't move anymore
     *
     * @param s the enum array that holds whether or not their point on a board
     * is empty, a red discs or a blue disc.
     * @return if its blocked return the color of the block, if not: none
     */
    @Override
    public Game.states checkBlocked(Game.states[] s) {
        int r = 0, b = 0;
        int[] red = new int[6];
        int[] blue = new int[6];
        // Get where the red/blue discs are
        for (int i = 0; i < s.length; i++) {
            if (s[i] == Game.states.red)
                red[r++] = i;
            else if (s[i] == Game.states.blue)
                blue[b++] = i;
        }
        boolean blocked = true;
        // Check if red is blocked        
        for (int i = 0; i < red.length; i++) {
            if (!blocked(s, red[i])) {
                blocked = false;
                break;
            }       
        }
        if (blocked)
            return Game.states.red;
        blocked = true;
        // Check if blue is blocked
        for (int i = 0; i < blue.length; i++) {
            if (!blocked(s, blue[i])) {
                blocked = false;
                break;
            }       
        }
        if (blocked)
            return Game.states.blue;
        return Game.states.none;
    }
    /**
     * Checks if a piece is blocked from sliding anywhere
     * @param s the enum array that holds whether or not their point on a board
     * is empty, a red discs or a blue disc.
     * @param p the index of the disc we want to assess
     * @return if the piece is blocked from sliding
     */
    private boolean blocked(Game.states[] s, int p) {
        switch (p) {
            case 0:
                if (s[1] != Game.states.none && s[6] != Game.states.none)
                    return true;
                break;
            case 1:
                if (s[0] != Game.states.none && s[2] != Game.states.none && s[4] != Game.states.none)
                    return true;
                break;
            case 2:
                if (s[1] != Game.states.none && s[9] != Game.states.none)
                    return true;
                break;
            case 3:
                if (s[4] != Game.states.none && s[7] != Game.states.none)
                    return true;
                break;
            case 4:
                if (s[1] != Game.states.none && s[3] != Game.states.none && s[5] != Game.states.none)
                    return true;
                break;
            case 5:
                if (s[4] != Game.states.none && s[8] != Game.states.none)
                    return true;
                break;
            case 6:
                if (s[0] != Game.states.none && s[7] != Game.states.none && s[13] != Game.states.none)
                    return true;
                break;
            case 7:
                if (s[3] != Game.states.none && s[6] != Game.states.none && s[10] != Game.states.none)
                    return true;
                break;
            case 8:
                if (s[5] != Game.states.none && s[9] != Game.states.none && s[12] != Game.states.none)
                    return true;
                break;
            case 9:
                if (s[2] != Game.states.none && s[8] != Game.states.none && s[15] != Game.states.none)
                    return true;
                break;
            case 10:
                if (s[7] != Game.states.none && s[11] != Game.states.none)
                    return true;
                break;
            case 11:
                if (s[10] != Game.states.none && s[12] != Game.states.none && s[14] != Game.states.none)
                    return true;
                break;
            case 12:
                if (s[8] != Game.states.none && s[11] != Game.states.none)
                    return true;
                break;
            case 13:
                if (s[6] != Game.states.none && s[14] != Game.states.none)
                    return true;
                break;
            case 14:
                if (s[11] != Game.states.none && s[13] != Game.states.none && s[15] != Game.states.none)
                    return true;
                break;
            case 15:
                if (s[9] != Game.states.none && s[14] != Game.states.none)
                    return true;
                break;
        }
        return false;
    }

    /**
     * Checks if the there are mills on the board
     *
     * @param s the enum array that holds whether or not their point on a board
     * is empty, a red discs or a blue disc.
     * @return i
     */
    @Override
    public Game.states[] checkMills(Game.states[] s) {
        int j = 0;
        Game.states[] g = new Game.states[8];
        for (int i = 0; i < g.length; i++) {
            g[i] = Game.states.none;
        }
        for (int i = 0; i < 4; i++) {
            if (s[i + j] == s[i + j + 1] && s[i + j] == s[i + j + 2]
                    && s[i + j] != Game.states.none) {
                if (s[i + j] == Game.states.red)
                    g[i] = Game.states.red;
                else if (s[i + j] == Game.states.blue)
                    g[i] = Game.states.blue;
            }
            if (i < 1 || i > 1)
                j += 2;
            else
                j += 6;
        }
        if (s[0] != Game.states.none && s[6] != Game.states.none
                && s[13] != Game.states.none) {
            if (s[0] == s[6] && s[0] == s[13]) {
                if (s[0] == Game.states.red)
                    g[4] = Game.states.red;
                else if (s[0] == Game.states.blue)
                    g[4] = Game.states.blue;
            }
        }
        if (s[3] != Game.states.none
                && s[7] != Game.states.none && s[10] != Game.states.none) {
            if (s[3] == s[7] && s[3] == s[10]) {
                if (s[3] == Game.states.red)
                    g[5] = Game.states.red;
                else if (s[3] == Game.states.blue)
                    g[5] = Game.states.blue;
            }
        }
        if (s[5] != Game.states.none && s[8] != Game.states.none
                && s[12] != Game.states.none) {
            if (s[5] == s[8] && s[5] == s[12]) {
                if (s[5] == Game.states.red)
                    g[6] = Game.states.red;
                else if (s[5] == Game.states.blue)
                    g[6] = Game.states.blue;
            }
        }
        if (s[2] != Game.states.none
                && s[9] != Game.states.none && s[15] != Game.states.none) {
            if (s[2] == s[9] && s[2] == s[15]) {
                if (s[2] == Game.states.red)
                    g[7] = Game.states.red;
                else if (s[2] == Game.states.blue)
                    g[7] = Game.states.blue;
            }
        }
        return g;
    }
}
