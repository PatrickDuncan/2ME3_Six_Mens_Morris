package me3assignment1;

public interface IMoves {
    /**
     * Whether or not the modified board is legal based on the rules of the
     * game. The rules are: there can't be more than 6 discs of one colour on
     * the board.
     *
     * @param s the enum array that holds whether or not their point on a board
     * is empty, a red discs or a blue disc.
     * @return whether or not the modified board is legal.
     */
    boolean modifyLegal(Game.states[] s);
    /**
     * Checks if the user can move to a certain location. Checks if the
     * destination is empty and if the starting point is adjacent to it.
     *
     * @param s the enum array that holds whether or not their point on a board
     * is empty, a red discs or a blue disc.
     * @param p1 the source point
     * @param p2 the destination point
     * @return whether they can move to a specific point
     */
    boolean checkMovement(Game.states[] s, int p1, int p2);
    /**
     * Checks if the user can move anymore by checking the adjacency of all
     * red/blue discs to see if its blocked off completely.
     *
     * @param s the enum array that holds whether or not their point on a board
     * is empty, a red discs or a blue disc.
     * @return if its blocked return the color of the block, if not: none
     */
    Game.states checkBlocked(Game.states[] s);
    /**
     * Checks if the there are mills on the board by looking if there are 3
     * discs of the same colour in a row.
     *
     * @param s the enum array that holds whether or not their point on a board
     * is empty, a red discs or a blue disc.
     * @return i
     */
    Game.states[] checkMills(Game.states[] s);
}
