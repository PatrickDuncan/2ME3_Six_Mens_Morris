package me3assignment1;

public interface IComputer {
    // Find a spot for the ai to place a disc
    int place(Game.states[] s);
    // Find a spot for the ai to remove a player's disc
    int remove(Game.states[] s);    
    // Find a spot for the ai to move a player's disc
    int[] move(Game.states[] s);
}
