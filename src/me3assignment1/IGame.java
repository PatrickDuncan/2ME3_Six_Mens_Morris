package me3assignment1;

public interface IGame {
    // Creates all the GUI objects.
    void objectCreate();
    // Set the properties of the GUI objects.
    void setUp();    
    // Set where the individual points are.
    void pointSetUp();
    //Sets up the game for the ability to add discs and track the state of the board.
    void discSetUp();
    // Adds functionality to the buttons.
    void buttonSetUp();
}
