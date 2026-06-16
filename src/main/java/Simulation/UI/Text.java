package Simulation.UI;

import Simulation.Core.Entity;
import Simulation.Core.ShaderProgram;
import Simulation.Core.Transformation;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class Text {
    List<Entity> TextEntity = new ArrayList<>();
    List<Entity> mechanics;
    List<Entity> electro;
    List<Entity> quantum;
    List<Entity> optics;
    List<Entity> Fluid;
    List<Entity> Thermo;
    List<Entity> entities = new ArrayList<>();
    List<Entity> particle;
    TextHandling handling;

    public Text(TextHandling handling) {
        this.handling = handling;
        init();
    }
    public void init(){
        Vector3f scale = new Vector3f(1f, 1f, 1f);
        List<Entity> entities1;
        mechanics = handling.RenderText("Mechanics", scale, new Vector3f(1700, 100, 0));
        TextEntity.addAll(mechanics);
        electro = handling.RenderText("Electrodynamics", scale, new Vector3f(1660, 200, 0));
        TextEntity.addAll(electro);
        quantum = handling.RenderText("Quantum Mechanics", scale, new Vector3f(1640, 300, 0));
        TextEntity.addAll(quantum);
        optics = handling.RenderText("Optics", scale, new Vector3f(1725, 400, 0));
        TextEntity.addAll(optics);
        Fluid = handling.RenderText("Fluid Mechanics", scale, new Vector3f(1675, 500, 0));
        TextEntity.addAll(Fluid);
        Thermo = handling.RenderText("Thermodynamics", scale, new Vector3f(1670, 600, 0));
        TextEntity.addAll(Thermo);
        particle = handling.RenderText("Particle Physics", scale, new Vector3f(1670, 700, 0));
        TextEntity.addAll(particle);
        entities1 = this.handling.RenderText("Render", new Vector3f(0.9f, 0.9f, 0.9f), new Vector3f(5, 3, 0));
        entities.addAll(entities1);
        entities1 = this.handling.RenderText("Graph", new Vector3f(0.9f, 0.9f, 0.9f), new Vector3f(100, 3, 0));
        Objects.requireNonNull(entities).addAll(entities1);

    }
    public void RenderMainMenuText(ShaderProgram shaderProgram, Transformation transformation, int TextTextureID){
        shaderProgram.bind();
        for(Entity entity: TextEntity){
            shaderProgram.setUniforms("hasTexture", entity.hasTexture());
            shaderProgram.setUniforms("ModelMatrix", transformation.setModelMatrix(entity.getTranslation(), entity.getScale(), entity.getRotation()));
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, TextTextureID);
            entity.getMesh().Render();
            entity.getMesh().FreeArray();
            glBindTexture(GL_TEXTURE_2D, 0);
        }
        shaderProgram.unbind();
    }
    public void Render_RenderText(ShaderProgram shaderProgram, Transformation transformation, int TextTextureID){
        shaderProgram.bind();
        for(Entity entity: entities){
            shaderProgram.setUniforms("hasTexture", entity.hasTexture());
            shaderProgram.setUniforms("ModelMatrix", transformation.setModelMatrix(entity.getTranslation(), entity.getScale(), entity.getRotation()));
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, TextTextureID);
            entity.getMesh().Render();
            entity.getMesh().FreeArray();
            glBindTexture(GL_TEXTURE_2D, 0);
        }
        shaderProgram.unbind();
    }
    public List<Entity> getTextEntity(){
        return this.TextEntity;
    }

    public List<Entity> getElectro() {
        return electro;
    }

    public List<Entity> getFluid() {
        return Fluid;
    }

    public List<Entity> getMechanics() {
        return mechanics;
    }

    public List<Entity> getOptics() {
        return optics;
    }

    public List<Entity> getParticle() {
        return particle;
    }

    public List<Entity> getQuantum() {
        return quantum;
    }

    public List<Entity> getThermo() {
        return Thermo;
    }

}
