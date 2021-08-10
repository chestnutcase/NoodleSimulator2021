package org.lwjglb.engine.graph;

public interface Renderable {
    /**
     * Allocates resources, such as vertex array objects and vertex buffer objects, needed to render the object
     */
    void allocate();

    /**
     * Performs the OpenGL draw calls
     */
    void render();

    /**
     * Frees resources such as vertex array objects
     */
    void cleanUp();
}
