package org.lwjglb.game;

import moe.chesnot.noodles.*;
import org.joml.Vector2f;
import org.joml.Vector3f;
import static org.lwjgl.glfw.GLFW.*;

import org.lwjglb.engine.*;
import org.lwjglb.engine.graph.*;

public class DummyGame implements IGameLogic {

    private static final float MOUSE_SENSITIVITY = 0.2f;

    private final Vector3f cameraInc;

    private final Renderer renderer;

    private final Camera camera;

    private IGameItem[] gameItems;

    private static final float CAMERA_POS_STEP = 0.05f;

    public DummyGame() {
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f();
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);
        Texture texture = new Texture("textures/pasta2.png");
        NoodleCurveFactory<?> ncf = new StraightNoodleCurveFactory();
        // Create the noodle
        Noodle noodle = new Noodle(ncf, texture, 6, 0.5f);
        Vector3f center = new Vector3f(0.0f, 0.0f, 0.5f);
        Vector3f normal = new Vector3f(0, 1f, 1f);
        PolygonCrossSection polygon = new PolygonCrossSection(center, 0.25f, normal,5,  texture);
        IGameItem gm = new GameItem(polygon);
        gameItems = new IGameItem[]{noodle, gm};
//        // Create the Mesh
//        float[] positions = new float[]{
//            // V0
//            -0.5f, 0.5f, 0.5f,
//            // V1
//            -0.5f, -0.5f, 0.5f,
//            // V2
//            0.5f, -0.5f, 0.5f,
//            // V3
//            0.5f, 0.5f, 0.5f,
//            // V4
//            -0.5f, 0.5f, -0.5f,
//            // V5
//            0.5f, 0.5f, -0.5f,
//            // V6
//            -0.5f, -0.5f, -0.5f,
//            // V7
//            0.5f, -0.5f, -0.5f,
//            // For text coords in top face
//            // V8: V4 repeated
//            -0.5f, 0.5f, -0.5f,
//            // V9: V5 repeated
//            0.5f, 0.5f, -0.5f,
//            // V10: V0 repeated
//            -0.5f, 0.5f, 0.5f,
//            // V11: V3 repeated
//            0.5f, 0.5f, 0.5f,
//            // For text coords in right face
//            // V12: V3 repeated
//            0.5f, 0.5f, 0.5f,
//            // V13: V2 repeated
//            0.5f, -0.5f, 0.5f,
//            // For text coords in left face
//            // V14: V0 repeated
//            -0.5f, 0.5f, 0.5f,
//            // V15: V1 repeated
//            -0.5f, -0.5f, 0.5f,
//            // For text coords in bottom face
//            // V16: V6 repeated
//            -0.5f, -0.5f, -0.5f,
//            // V17: V7 repeated
//            0.5f, -0.5f, -0.5f,
//            // V18: V1 repeated
//            -0.5f, -0.5f, 0.5f,
//            // V19: V2 repeated
//            0.5f, -0.5f, 0.5f,};
//        float[] textCoords = new float[]{
//            0.0f, 0.0f,
//            0.0f, 0.5f,
//            0.5f, 0.5f,
//            0.5f, 0.0f,
//            0.0f, 0.0f,
//            0.5f, 0.0f,
//            0.0f, 0.5f,
//            0.5f, 0.5f,
//            // For text coords in top face
//            0.0f, 0.5f,
//            0.5f, 0.5f,
//            0.0f, 1.0f,
//            0.5f, 1.0f,
//            // For text coords in right face
//            0.0f, 0.0f,
//            0.0f, 0.5f,
//            // For text coords in left face
//            0.5f, 0.0f,
//            0.5f, 0.5f,
//            // For text coords in bottom face
//            0.5f, 0.0f,
//            1.0f, 0.0f,
//            0.5f, 0.5f,
//            1.0f, 0.5f,};
//        int[] indices = new int[]{
//            // Front face
//            0, 1, 3, 3, 1, 2,
//            // Top Face
//            8, 10, 11, 9, 8, 11,
//            // Right face
//            12, 13, 7, 5, 12, 7,
//            // Left face
//            14, 15, 6, 4, 14, 6,
//            // Bottom face
//            16, 18, 19, 17, 16, 19,
//            // Back face
//            4, 6, 7, 5, 4, 7,};
//        Texture texture = new Texture("textures/grassblock.png");
//        Mesh mesh = new Mesh(positions, textCoords, indices, texture);
//        Vector3f center = new Vector3f(0.0f, 0.0f, 0.0f);
//        Vector3f normal = new Vector3f(0, 1f, 1f);
//        Polygon polygon = new Polygon(center, 0.25f, normal,5,  texture);
//        GameItem gameItem1 = new GameItem(mesh);
//        gameItem1.setScale(0.5f);
//        gameItem1.setPosition(0, 0, -2);
//        GameItem gameItem2 = new GameItem(mesh);
//        gameItem2.setScale(0.5f);
//        gameItem2.setPosition(0.5f, 0f, -2);
//        GameItem gameItem3 = new GameItem(mesh);
//        gameItem3.setScale(0.5f);
//        gameItem3.setPosition(0, 0, -2.5f);
//        GameItem gameItem4 = new GameItem(mesh);
//        gameItem4.setScale(0.5f);
//        gameItem4.setPosition(0.5f, 0, -2.5f);
//        GameItem gameItem5 = new GameItem(polygon);
//        gameItem5.setScale(0.5f);
//        gameItem5.setPosition(0, 0, -2);
//        gameItems = new GameItem[]{gameItem5};
////        gameItems = new GameItem[]{gameItem1, gameItem2, gameItem3, gameItem4};
    }

    @Override
    public void input(Window window, MouseInput mouseInput) {
        cameraInc.set(0, 0, 0);
        if (window.isKeyPressed(GLFW_KEY_W)) {
            cameraInc.z = -1;
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            cameraInc.z = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            cameraInc.x = -1;
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            cameraInc.x = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
            cameraInc.y = -1;
        } else if (window.isKeyPressed(GLFW_KEY_SPACE)) {
            cameraInc.y = 1;
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        // Update camera position
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);

        // Update camera based on mouse            
        if (mouseInput.isLeftButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        }
    }

    @Override
    public void render(Window window) {
        renderer.render(window, camera, gameItems);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        for (IGameItem gameItem : gameItems) {
            for(Renderable renderable : gameItem.getRenderables()){
                renderable.cleanUp();
            }
        }
    }

}
