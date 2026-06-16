package Simulation.UI.Mechanics;

import Simulation.Core.Entity;
import Simulation.Core.ShaderProgram;
import Simulation.Core.Transformation;
import Simulation.UI.TextHandling;
import org.joml.Vector3f;
import static org.lwjgl.opengl.GL20.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Overview {
    TextHandling handling;
    boolean[] Open = new boolean[4];
    List<Entity> Menu = new ArrayList<>();
    public Overview(TextHandling handling){
        this.handling = handling;
        Arrays.fill(Open, false);
        init();
    }
    public void init(){
        Vector3f scale = new Vector3f(0.9f, 0.9f, 0.9f);
        List<Entity> entities = this.handling.RenderText("Projectile  Motion", scale, new Vector3f(1675, 175, 0));
        Menu.addAll(entities);
        entities = this.handling.RenderText("Pendulum", scale, new Vector3f(1710, 250, 0));
        Menu.addAll(entities);
        entities = this.handling.RenderText("Collision", scale, new Vector3f(1720, 325, 0));
        Menu.addAll(entities);
        entities = this.handling.RenderText("Pulley System", scale, new Vector3f(1700, 400, 0));
        Menu.addAll(entities);
        entities = this.handling.RenderText("Inclined Plane", scale, new Vector3f(1700, 475, 0));
        Menu.addAll(entities);
    }
    public void RenderText(ShaderProgram shaderProgram, Transformation transformation, int id, boolean isMenuOpen){
        if(isMenuOpen){
            shaderProgram.bind();
            shaderProgram.setUniforms("hasTexture", 1.0f);
            for(Entity entity: Menu){
                shaderProgram.setUniforms("ModelMatrix", transformation.setModelMatrix(entity.getTranslation(), entity.getScale(), entity.getRotation()));
                glActiveTexture(GL_TEXTURE0);
                glBindTexture(GL_TEXTURE_2D, id);
                entity.getMesh().Render();
                entity.getMesh().FreeArray();
            }
            shaderProgram.unbind();
        }
    }

    public boolean[] getOpen() {
        return Open;
    }
}
