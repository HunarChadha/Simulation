package Simulation.UI;

import Simulation.Core.*;
import Simulation.UI.Mechanism.Mechanism;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Grid {
    float[] vertices = {-1000,0,-1000, 1000,0,-1000, 1000,0,1000, -1000,0,1000};
    int[] indices = {0, 1, 2, 0, 3, 2};

    ShaderProgram shaderProgram;
    Entity entity;
    Transformation transformation;
    Matrix4f projection;

    public Grid(int width, int height) throws  Exception{
        shaderProgram = new ShaderProgram();
        transformation = new Transformation();
        shaderProgram.createVertexShader(Utils.readFile("vertexShader\\Grid.vert"));
        shaderProgram.createFragShader(Utils.readFile("FragmentShader\\Grid.frag"));
        shaderProgram.link();
        shaderProgram.createUniforms("projection");
        shaderProgram.createUniforms("view");
        shaderProgram.createUniforms("ModelMatrix");
        shaderProgram.createUniforms("cameraPos");
        projection = transformation.setProjection(height, width);
        Mesh mesh = new Mesh(vertices, indices, null, null, null);
        entity = new Entity(new Vector3f(1, 1, 1), new Vector3f(0, 0,0), new Vector3f(0, 0, 0), mesh, 0.0f);
    }
    public void RenderGrid(long window, Camera camera){
        shaderProgram.bind();
        camera.MovePosition(window);
        camera.setRotation(window);
        shaderProgram.setUniforms("projection", projection);
        shaderProgram.setUniforms("ModelMatrix", transformation.setModelMatrix(entity.getTranslation(), entity.getScale(), entity.getRotation()));
        shaderProgram.setUniforms("view", transformation.calcViewMatrix(camera));
        shaderProgram.setUniforms("cameraPos", -camera.getPosition().x, -camera.getPosition().y, -camera.getPosition().z);
        entity.getMesh().Render();
        entity.getMesh().FreeArray();
        shaderProgram.unbind();
    }
}
