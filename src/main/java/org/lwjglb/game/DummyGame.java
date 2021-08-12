package org.lwjglb.game;

import moe.chesnot.noodles.*;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjglb.engine.*;
import org.lwjglb.engine.graph.*;

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

    private IGameItem[] gameItems;

    private TickableEntity[] tickableEntities;

    private static final float CAMERA_POS_STEP = 0.05f;

    public DummyGame() {
        renderer = new Renderer();
        camera = new Camera();
        camera.setPosition(8.544f, 0.5E-1f, 2.52f);
        camera.setRotation(2, -8.44E+1f, 0.0f);
        cameraInc = new Vector3f();
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);
        Texture texture = new Texture("textures/pasta2.png");
//        NoodleCurveFactory<?> ncf = new BeizerNoodleCurveFactory(3);
        NoodleCurveFactory<?> ncf = new StraightNoodleCurveFactory();
//        NoodleCurveFactory<?> ncf = new QuadraticBeizerNoodleCurveFactory(0.2f);
        // Create the noodle
        Noodle noodle = new Noodle(ncf, 6, 0.5f);
        gameItems = new IGameItem[]{noodle};
        tickableEntities = new TickableEntity[]{noodle};
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

        // tick entitiesm
        if (tickEntities) {
            for (TickableEntity entity : tickableEntities) {
                entity.tick(interval);
            }
            tickEntities = false;
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
            for (Renderable renderable : gameItem.getRenderables()) {
                renderable.cleanUp();
            }
        }
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
