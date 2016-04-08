package me3assignment1;

public interface IComputer {
    // Find a random spot for the AI to add a disc on the board legally.
    int place(Game.states[] s);
    // Find a random spot for the AI to remove a player piece after getting a
    //mill
    int remove(Game.states[] s);    
    // It finds a random, legal start and end point for the AI to slide/move to
    int[] move(Game.states[] s);
}
