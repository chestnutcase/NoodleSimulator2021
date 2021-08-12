package org.lwjglb.engine.graph;

import org.lwjglb.engine.IGameItem;

import java.util.List;
import java.util.function.Consumer;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public interface IMesh {
    Material getMaterial();

    void setMaterial(Material material);

    default void endRender() {
        // Restore state
        glBindVertexArray(0);

        glBindTexture(GL_TEXTURE_2D, 0);
    }

    void render();

    void renderList(List<IGameItem> gameItems, Consumer<IGameItem> consumer);

    void cleanUp();

    void deleteBuffers();
}
