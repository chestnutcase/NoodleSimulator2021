package org.lwjglb.game;

import org.lwjglb.engine.GameEngine;
import org.lwjglb.engine.IGameLogic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
 
    public static void main(String[] args) {
        try {
            // Extract textures
            File texturesFolder = new File("textures");
            if(!texturesFolder.exists()){
                texturesFolder.mkdir();
            }
            String[] textureFiles = {"1.png", "2.png", "3.png", "4.png", "5.png", "6.png", "skybox.png"};
            for(String texture : textureFiles){
                byte[] data = Main.class.getResourceAsStream("/textures/" + texture).readAllBytes();
                Files.write(Path.of("textures/" + texture), data);
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