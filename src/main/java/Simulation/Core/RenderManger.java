package Simulation.Core;
import Simulation.Core.Utils;
import Simulation.Mechanics.ProjectileMotion;
import Simulation.UI.TextHandling;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.Objects;

public class RenderManger {
    ShaderProgram shaderProgram;
    Transformation transformation;
    Matrix4f projection;
    ProjectileMotion projectileMotion;
    public RenderManger(int height, int width, Simulation.UI.Mechanics.ProjectileMotion projectileMotionUI, ProjectileMotion projectileMotion) throws Exception{
        transformation =  new Transformation();
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.readFile("D:\\Physics simulation\\physics.Simulation\\src\\main\\resources\\vertexShader\\vert.vert"));
        shaderProgram.createFragShader(Utils.readFile("D:\\Physics simulation\\physics.Simulation\\src\\main\\resources\\FragmentShader\\Frag.frag"));
        shaderProgram.link();
        shaderProgram.createUniforms("projectionMatrix");
        shaderProgram.createUniforms("view");
        shaderProgram.createUniforms("ModelMatrix");
        projection = transformation.setProjection(height, width);
        this.projectileMotion = projectileMotion;
    }
    public void Render(Camera camera, TextHandling handling, float FPS){
        shaderProgram.bind();
        shaderProgram.setUniforms("projectionMatrix", projection);
        projectileMotion.RenderBody(shaderProgram, transformation, camera, handling);
        shaderProgram.unbind();
    }
}
