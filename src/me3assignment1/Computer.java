package me3assignment1;

public class Computer implements IComputer {
    /**
     * @param s the enum array that holds whether or not their point on a board
     * is empty, a red discs or a blue disc.
     * @return a spot for the ai to place a disc
     */
    @Override
    public int place(Game.states[] s) {
        //Moves moves = new Moves();
        int spot = (int) (Math.random() * 15);
        while (s[spot] != Game.states.none) {
            spot = (int) (Math.random() * 15);
        }
        return spot;
    }
    /**
     * @param s the enum array that holds whether or not their point on a board
     * is empty, a red discs or a blue disc.
     * @return remove a red/player piece
     */
    @Override
    public int remove(Game.states[] s) {
        int spot = (int) (Math.random() * 15);
        while (s[spot] == Game.states.red) {
            spot = (int) (Math.random() * 15);
        }
        return spot;
    }
}
