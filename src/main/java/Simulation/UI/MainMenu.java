package Simulation.UI;

import Simulation.Core.*;
import Simulation.Core.RenderManger;
import org.joml.Vector3f;

import java.awt.image.BufferedImage;
import java.beans.VetoableChangeListener;

public class MainMenu {
    float []vertices = {800, 0, 0, 2000, 0, 0, 2000, 1900, 0, 800, 1900, 0};
    float[] Button = {0, 0, 0, 1100, 0, 0, 1100, 35, 0, 0, 35, 0};
    int[] indices = {0,1,2, 0,3,2};
    float[] TexCoords ={0, 0, 1, 0, 1, 1, 0, 1};
    Vector3f color;
    Vector3f translation;
    Vector3f scale;
    Entity entity;
    Entity RenderButton;
    Texture texture;
    int id;


    public MainMenu(Vector3f color, Vector3f scale, Vector3f translation){
        this.color = color;
        this.translation = translation;
        this.scale = scale;
        setEntity();
        texture  = new Texture();
        BufferedImage image = texture.loadImage("D:\\Physics simulation\\physics.Simulation\\src\\main\\resources\\textureAtlas_0.png");
        id = texture.loadTexture(image);
    }
    public void setEntity(){
        Mesh mesh = new Mesh(vertices, indices, TexCoords, null, null);
        entity = new Entity(new Vector3f(1, 1, 1), new Vector3f(800, 0, -0.5f), new Vector3f(0, 0, 0), mesh, 0.0f);
        Mesh mesh1 = new Mesh(Button, indices, TexCoords, null, null);
        RenderButton = new Entity(new Vector3f(1, 1, 1), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), mesh1, 0.0f);
    }
    public void RenderMainMenu(ShaderProgram shaderProgram, Transformation transformation){
        shaderProgram.bind();
        shaderProgram.setUniforms("hasTexture", 0.0f);
        Render_RenderButton(shaderProgram, transformation);
        shaderProgram.setUniforms("ModelMatrix", transformation.setModelMatrix(entity.getTranslation(), entity.getScale(), entity.getRotation()));
        entity.getMesh().Render();
        entity.getMesh().FreeArray();
        shaderProgram.unbind();
    }
    public void Render_RenderButton(ShaderProgram shaderProgram, Transformation transformation){
        shaderProgram.setUniforms("ModelMatrix", transformation.setModelMatrix(RenderButton.getTranslation(), RenderButton.getScale(), RenderButton.getRotation()));
        RenderButton.getMesh().Render();
        RenderButton.getMesh().FreeArray();
    }

    public Entity getRenderButton() {
        return RenderButton;
    }

    public int getId() {
        return id;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }

    public Vector3f getColor() {
        return color;
    }

    public float[] getVertices() {
        return vertices;
    }

    public int[] getIndex() {
        return indices;
    }
}
