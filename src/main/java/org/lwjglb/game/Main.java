package org.lwjglb.game;

import org.lwjglb.engine.GameEngine;
import org.lwjglb.engine.IGameLogic;
 
public class Main {
 
    public static void main(String[] args) {
        try {
            boolean vSync = true;
            IGameLogic gameLogic = new DummyGame();
            GameEngine gameEng = new GameEngine("Noodle Simulator 2021", 1024, 768, vSync, gameLogic);
            gameEng.run();
        } catch (Exception excp) {
            excp.printStackTrace();
            System.exit(-1);
        }
    }
}