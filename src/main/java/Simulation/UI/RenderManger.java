package Simulation.UI;
import Simulation.Core.*;
import Simulation.UI.Mechanics.Overview;
import Simulation.UI.Mechanics.Pendulum;
import Simulation.UI.Mechanics.ProjectileMotion;
import Simulation.UI.Mechanism.Mechanism;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import static org.lwjgl.opengl.GL20.*;

import java.util.List;

public class RenderManger {
    MainMenu mainMenu;
    ShaderProgram shaderProgram;
    Grid grid;
    Transformation  transformation;
    Matrix4f orthoMatrix;
    Text  text;
    Mechanism mechanism;
    Overview overview;
    Camera camera;
    Texture texture;
    ProjectileMotion projectileMotion;
    Pendulum pendulum;
    public void createShaders() throws Exception{
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.readFile("vertexShader\\MainMenu.vert"));
        shaderProgram.createFragShader(Utils.readFile("FragmentShader\\MainMenu.frag"));
        shaderProgram.link();
        shaderProgram.createUniforms("ModelMatrix");
        shaderProgram.createUniforms("orthoMatrix");
        shaderProgram.createUniforms("Color");
        shaderProgram.createUniforms("TexTexture");
        shaderProgram.createUniforms("hasTexture");
    }
    public void init(int width, int height, float right, float bottom, Camera camera, ProjectileMotion projectileMotion, TextHandling handling) throws Exception{
        transformation = new Transformation();
        this.camera = camera;
        text = new Text(handling);
        texture = new Texture();
        grid = new Grid(width, height);
        orthoMatrix =  transformation.setOrthoMatrix(right, bottom);
        overview  = new Overview(handling);
        mainMenu = new MainMenu(new Vector3f(0.1725f, 0.1725f, 0.1725f), new Vector3f(1, 1, 1), new Vector3f(100, 100, 0));
        mechanism = new Mechanism(this.camera, text, overview);
        this.projectileMotion = projectileMotion;
        pendulum = new Pendulum();
    }

    public RenderManger(float right, float bottom, int width, int height, Camera camera, ProjectileMotion projectileMotion, TextHandling handling)throws Exception{
        createShaders();
        init(width, height, right, bottom, camera, projectileMotion, handling);
    }
    public void setShaders(){
        shaderProgram.bind();
        shaderProgram.setUniforms("orthoMatrix", orthoMatrix);
        shaderProgram.setUniforms("TexTexture", 0);
        shaderProgram.setUniforms("Color", mainMenu.getColor());
        shaderProgram.unbind();
    }
    public void RenderMainMenu(){
        text.Render_RenderText(shaderProgram, transformation, mainMenu.getId());
        if(!overview.getOpen()[0]){
            text.RenderMainMenuText(shaderProgram, transformation, mainMenu.getId());
            overview.RenderText(shaderProgram, transformation, mainMenu.getId(), mechanism.getIsClicked()[0]);
        }
    }

    public void Render(long window, Simulation.Mechanics.ProjectileMotion projectileMotion1){
        setShaders();
        grid.RenderGrid(window, camera);
        mechanism.check(window);
        mechanism.Mechanism();
        mainMenu.RenderMainMenu(shaderProgram, transformation);
        RenderMainMenu();
        projectileMotion.RenderText(shaderProgram, transformation, mainMenu.getId(), overview, camera, window, projectileMotion1);
        pendulum.Render(overview);

    }
}
