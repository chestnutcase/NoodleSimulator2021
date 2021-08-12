package org.lwjglb.game;

import org.lwjglb.engine.GameEngine;
import org.lwjglb.engine.IGameLogic;

import java.io.File;

public class Main {
 
    public static void main(String[] args) {
        try {
            // Extract textures
            File texturesFolder = new File("textures");
            if(!texturesFolder.exists()){
                System.err.println("Folder named textures not found! Please extract texture files in a folder named textures in the working directory first.");
                System.exit(-1);
                return;
            }
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