package org.lwjglb.game;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import moe.chesnot.noodles.geometry.Noodle;
import moe.chesnot.noodles.geometry.NoodleCurveFactory;
import moe.chesnot.noodles.geometry.StraightNoodleCurveFactory;
import org.lwjglb.engine.*;
import org.lwjglb.engine.graph.*;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;


import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DummyGame implements IGameLogic {

    private static final float MOUSE_SENSITIVITY = 0.2f;

    private final Vector3f cameraInc;

    private final Renderer renderer;

    private final Camera camera;

    private Scene scene;

    private Hud hud;

    private float lightAngle;

    private static final float CAMERA_POS_STEP = 0.05f;

    private IGameItem[] gameItems;
    private TickableEntity[] tickableEntities;

    public DummyGame() {
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f(0.0f, 0.0f, 0.0f);
        camera.setPosition(-2.531f, 1.1f, 8.177f);
        camera.setRotation(8.400f, 42.06f, 0.0f);
        lightAngle = -90;
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);

        scene = new Scene();

        float skyBoxScale = 50.0f;
        NoodleCurveFactory<?> ncf = new StraightNoodleCurveFactory();
        Noodle noodle = new Noodle(ncf, 6, 0.5f);
        IGameItem[] gameItems = {noodle};
        this.gameItems = gameItems;
        this.tickableEntities = new TickableEntity[]{noodle};
        scene.setGameItems(gameItems);

        // Setup  SkyBox
        SkyBox skyBox = new SkyBox("/models/skybox.obj", "textures/skybox.png");
        skyBox.setScale(skyBoxScale);
        scene.setSkyBox(skyBox);

        // Setup Lights
        setupLights();

        // Create HUD
        hud = new Hud("NOODLESIM 2021 - PRESS H TO SHOW HELP IN CONSOLE");
    }

    private void setupLights() {
        SceneLight sceneLight = new SceneLight();
        scene.setSceneLight(sceneLight);

        // Ambient Light
        sceneLight.setAmbientLight(new Vector3f(0.5f, 0.5f, 0.5f));
        var pointLight = new PointLight(new Vector3f(1f, 1f, 1f), new Vector3f(0, 0, 0), 100.0f);
        PointLight.Attenuation att = new PointLight.Attenuation(0.1f, 0.1f, 1.0f);
        pointLight.setAttenuation(att);
        SpotLight[] spotlights = {
                new SpotLight(pointLight, new Vector3f(0, 0, 1), 25f)
        };
        sceneLight.setSpotLightList(spotlights);
        // Directional Light
        float lightIntensity = 1.0f;
        Vector3f lightPosition = new Vector3f(-1, 0, 0);
        sceneLight.setDirectionalLight(new DirectionalLight(new Vector3f(1, 1, 1), lightPosition, lightIntensity));
    }

    private final Map<Integer, Boolean> debounceKey = new HashMap<Integer, Boolean>();
    private boolean tickEntities = false;

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
        if (window.isKeyPressed(GLFW_KEY_L) && debounceKey.getOrDefault(GLFW_KEY_L, true)) {
            debounceKey.put(GLFW_KEY_L, false);
            for (IGameItem gameItem : gameItems) {
                if (gameItem.getClass().equals(Noodle.class)) {
                    ((Noodle) gameItem).toggleSweptSurfaceVisible();
                }
            }
        } else if (!window.isKeyPressed(GLFW_KEY_L)) {
            debounceKey.put(GLFW_KEY_L, true);
        }
        tickEntities = window.isKeyPressed(GLFW_KEY_RIGHT);
        if (window.isKeyPressed(GLFW_KEY_P) && debounceKey.getOrDefault(GLFW_KEY_P, true)) {
            debounceKey.put(GLFW_KEY_P, false);
            System.out.println("Camera position:" + camera.getPosition());
            System.out.println("Camera rotation:" + camera.getRotation());
        } else if (!window.isKeyPressed(GLFW_KEY_P)) {
            debounceKey.put(GLFW_KEY_P, true);
        }
        if (window.isKeyPressed(GLFW_KEY_R) && debounceKey.getOrDefault(GLFW_KEY_R, true)) {
            debounceKey.put(GLFW_KEY_R, false);
            screenshot(window);
        } else if (!window.isKeyPressed(GLFW_KEY_R)) {
            debounceKey.put(GLFW_KEY_R, true);
        }
        if (window.isKeyPressed(GLFW_KEY_T) && debounceKey.getOrDefault(GLFW_KEY_T, true)) {
            debounceKey.put(GLFW_KEY_T, false);
            for (IGameItem gameItem : gameItems) {
                if (gameItem.getClass().equals(Noodle.class)) {
                    ((Noodle) gameItem).changeTexture();
                }
            }
        } else if (!window.isKeyPressed(GLFW_KEY_T)) {
            debounceKey.put(GLFW_KEY_T, true);
        }
        if (window.isKeyPressed(GLFW_KEY_UP) && debounceKey.getOrDefault(GLFW_KEY_UP, true)) {
            debounceKey.put(GLFW_KEY_UP, false);
            for (IGameItem gameItem : gameItems) {
                if (gameItem.getClass().equals(Noodle.class)) {
                    ((Noodle) gameItem).increaseCrossSectionCount(1);
                }
            }
        } else if (!window.isKeyPressed(GLFW_KEY_UP)) {
            debounceKey.put(GLFW_KEY_UP, true);
        }
        if (window.isKeyPressed(GLFW_KEY_DOWN) && debounceKey.getOrDefault(GLFW_KEY_DOWN, true)) {
            debounceKey.put(GLFW_KEY_DOWN, false);
            for (IGameItem gameItem : gameItems) {
                if (gameItem.getClass().equals(Noodle.class)) {
                    ((Noodle) gameItem).increaseCrossSectionCount(-1);
                }
            }
        } else if (!window.isKeyPressed(GLFW_KEY_DOWN)) {
            debounceKey.put(GLFW_KEY_DOWN, true);
        }
        if (window.isKeyPressed(GLFW_KEY_C) && debounceKey.getOrDefault(GLFW_KEY_C, true)) {
            debounceKey.put(GLFW_KEY_C, false);
            for (IGameItem gameItem : gameItems) {
                if (gameItem.getClass().equals(Noodle.class)) {
                    ((Noodle) gameItem).changeCrossSection();
                }
            }
        } else if (!window.isKeyPressed(GLFW_KEY_C)) {
            debounceKey.put(GLFW_KEY_C, true);
        }
        if (window.isKeyPressed(GLFW_KEY_H) && debounceKey.getOrDefault(GLFW_KEY_H, true)) {
            debounceKey.put(GLFW_KEY_H, false);
            System.out.println("""
wasd shift space: camera movement
r: take screenshot
t: change texture
c: change cross section shape
l: toggle swept surface visibility
p: print camera position
up/down: increment/decrement cross section sides
right: step physics
""");
        } else if (!window.isKeyPressed(GLFW_KEY_H)) {
            debounceKey.put(GLFW_KEY_H, true);
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        // Update camera based on mouse            
        if (mouseInput.isRightButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);

            // Update HUD compass
            hud.rotateCompass(camera.getRotation().y);
        }

        // Update camera position
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);

        SceneLight sceneLight = scene.getSceneLight();
        Vector3f coneDirection = new Vector3f();
        camera.getRotation().normalize(coneDirection);
        sceneLight.getSpotLightList()[0].setConeDirection(coneDirection);
        sceneLight.getSpotLightList()[0].getPointLight().setPosition(camera.getPosition());

        // tick entities
        if (tickEntities) {
            for (TickableEntity entity : tickableEntities) {
                entity.tick(interval);
            }
            tickEntities = false;
        }
        // Update directional light direction, intensity and colour
        /*
        DirectionalLight directionalLight = sceneLight.getDirectionalLight();
        lightAngle += 1.1f;
        if (lightAngle > 90) {
            directionalLight.setIntensity(0);
            if (lightAngle >= 360) {
                lightAngle = -90;
            }
            sceneLight.getAmbientLight().set(0.3f, 0.3f, 0.4f);
        } else if (lightAngle <= -80 || lightAngle >= 80) {
            float factor = 1 - (float) (Math.abs(lightAngle) - 80) / 10.0f;
            sceneLight.getAmbientLight().set(factor, factor, factor);
            directionalLight.setIntensity(factor);
            directionalLight.getColor().y = Math.max(factor, 0.9f);
            directionalLight.getColor().z = Math.max(factor, 0.5f);
        } else {
            sceneLight.getAmbientLight().set(1, 1, 1);
            directionalLight.setIntensity(1);
            directionalLight.getColor().x = 1;
            directionalLight.getColor().y = 1;
            directionalLight.getColor().z = 1;
        }
        double angRad = Math.toRadians(lightAngle);
        directionalLight.getDirection().x = (float) Math.sin(angRad);
        directionalLight.getDirection().y = (float) Math.cos(angRad);
        */
    }

    @Override
    public void render(Window window) {
        hud.updateSize(window);
        renderer.render(window, camera, scene, hud);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        Map<IMesh, List<IGameItem>> mapMeshes = scene.getGameMeshes();
        for (IMesh mesh : mapMeshes.keySet()) {
            mesh.cleanUp();
        }
        hud.cleanup();
    }

    private void screenshot(Window window) {
        int WIDTH = window.getWidth();
        int HEIGHT = window.getHeight();
        //Creating an rbg array of total pixels
        int[] pixels = new int[WIDTH * HEIGHT];
        int bindex;
        // allocate space for RBG pixels
        ByteBuffer fb = ByteBuffer.allocateDirect(WIDTH * HEIGHT * 3);

        // grab a copy of the current frame contents as RGB
        glReadPixels(0, 0, WIDTH, HEIGHT, GL_RGB, GL_UNSIGNED_BYTE, fb);

        BufferedImage imageIn = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        // convert RGB data in ByteBuffer to integer array
        for (int i = 0; i < pixels.length; i++) {
            bindex = i * 3;
            pixels[i] =
                    ((fb.get(bindex) << 16)) +
                            ((fb.get(bindex + 1) << 8)) +
                            ((fb.get(bindex + 2) << 0));
        }
        //Allocate colored pixel to buffered Image
        imageIn.setRGB(0, 0, WIDTH, HEIGHT, pixels, 0, WIDTH);

        //Creating the transformation direction (horizontal)
        AffineTransform at = AffineTransform.getScaleInstance(1, -1);
        at.translate(0, -imageIn.getHeight(null));

        //Applying transformation
        AffineTransformOp opRotated = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        BufferedImage imageOut = opRotated.filter(imageIn, null);
        SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmmss");
        File file = new File("screenshots/" + df.format(new Date()) + ".png");
        try {//Try to screate image, else show exception.
            ImageIO.write(imageOut, "png", file);
        } catch (Exception e) {
            System.err.println("ScreenShot() exception: " + e);
        }
        System.out.println("Screenshot saved to " + file.getAbsolutePath());
    }
}
