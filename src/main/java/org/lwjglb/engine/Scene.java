package org.lwjglb.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjglb.engine.graph.IMesh;
import org.lwjglb.engine.graph.Mesh;

public class Scene {

    public static Map<IMesh, List<IGameItem>> meshMap;
    
    private SkyBox skyBox;
    
    private SceneLight sceneLight;

    public Scene() {
        meshMap = new HashMap();
    }
    
    public Map<IMesh, List<IGameItem>> getGameMeshes() {
        return meshMap;
    }

    public void setGameItems(IGameItem[] gameItems) {
        int numGameItems = gameItems != null ? gameItems.length : 0;
        for (int i=0; i<numGameItems; i++) {
            IGameItem gameItem = gameItems[i];
            Mesh mesh = gameItem.getMesh();
            List<IGameItem> list = meshMap.get(mesh);
            if ( list == null ) {
                list = new ArrayList<>();
                meshMap.put(mesh, list);
            }
            list.add(gameItem);
        }
    }

    public SkyBox getSkyBox() {
        return skyBox;
    }

    public void setSkyBox(SkyBox skyBox) {
        this.skyBox = skyBox;
    }

    public SceneLight getSceneLight() {
        return sceneLight;
    }

    public void setSceneLight(SceneLight sceneLight) {
        this.sceneLight = sceneLight;
    }
    
}
