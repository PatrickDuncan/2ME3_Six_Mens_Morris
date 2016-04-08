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
        while (s[spot] != Game.states.red) {
            spot = (int) (Math.random() * 15);
        }
        return spot;
    }

    /**
     *
     * @param s
     * @return
     */
    @Override
    public int[] move(Game.states[] s) {
        Moves moves = new Moves();
        int[] startAndEnd = new int[2];
        boolean valid = false;
        int start, end;
        start = end = 0;
        
        
        while(!valid){
            start = (int) (Math.random() * 15);
            end = (int) (Math.random() * 15);
            valid = true;
            if (s[start] != Game.states.blue)
                valid = false;
            if (s[end] != Game.states.none)
                valid = false;
            if (!moves.checkMovement(s, start, end))
                valid = false;
            if (start == end)
                valid = false;
            
        }
//        while (s[start] != Game.states.blue) {
//            while (s[end] != Game.states.none && !moves.checkMovement(s, start, end)) {
//                end = (int) (Math.random() * 15);
//                while (start == end) {
//                    end = (int) (Math.random() * 15);
//                }
//            }
//            
//            
//            start = (int) (Math.random() * 15);
//
//        }
        startAndEnd[0] = start;
        startAndEnd[1] = end;
        return startAndEnd;
    }
}
