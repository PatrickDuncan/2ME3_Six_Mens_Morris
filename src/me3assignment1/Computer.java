/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me3assignment1;

/**
 *
 * @author Damian
 */
public class Computer {

    public int place(Game.states[] s) {
        //Moves moves = new Moves();
        int spot = (int) (Math.random() * 15);
        while (s[spot] != Game.states.none) {
            spot = (int) (Math.random() * 15);
        }
        return spot;
    }

    public int computerRemove(Game.states[] s, Game.states b) {
        int spot = (int) (Math.random() * 15);
        while (s[spot] == b) {
            spot = (int) (Math.random() * 15);
        }
        return spot;
    }
}
