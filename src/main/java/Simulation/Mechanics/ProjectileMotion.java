package Simulation.Mechanics;

import Simulation.Core.*;
import Simulation.UI.Mechanics.Utils;
import Simulation.UI.TextHandling;
import org.joml.*;
import org.lwjgl.glfw.GLFW;

import java.lang.Math;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.lwjgl.opengl.GL11C.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11C.glGetError;

public class ProjectileMotion {
    String[][] data;
    Entity[][] OnScreenData;
    final float cons = 20;
    final float FPS = 144;
    float angle_theta;
    float Rad_theta;
    Vector3f Position = new Vector3f(0, 0, 0);
    Vector3f VelocityVector;
    Simulation.UI.Mechanics.ProjectileMotion projectileMotionUI;
    float Rad_phi;
    double angle_phi;
    double magnitude;
    float x_velocity;
    float y_velocity;
    float z_velocity;
    List<Entity> entities;
    List<Entity> InformationEntities;
    boolean render;
    float acceraltion = (float) 9.8/cons;
    Entity entity;
    Float[] Information = new Float[6];

    public ProjectileMotion(Simulation.UI.Mechanics.ProjectileMotion projectileMotion){
        this.projectileMotionUI =  projectileMotion;
        data = this.projectileMotionUI.getBoxData();
        OnScreenData = this.projectileMotionUI.getData_Parameter();
        InformationEntities = this.projectileMotionUI.getInformation();
        init();
    }
    public void init(){
        Model model = new Model();
        Mesh mesh = model.LoadModel("D:\\Physics simulation\\physics.Simulation\\src\\main\\resources\\Untitled.obj", 1, null);
        entity = new Entity(new Vector3f(1f, 1f, 1f), new Vector3f(0, 0,0), new Vector3f(0 ,0, 0), mesh, 0.0f);
    }
    public void CheckRender(Camera camera, TextHandling handling){
        Vector2d cameraPos = camera.getMousePos();
        if(camera.isLeftMouseClicked()){
            if (cameraPos.x >= 0 && cameraPos.x <= 140 && cameraPos.y >= 0 && cameraPos.y <= 50) {
                ParseData_cal(handling);
                render = true;
                projectileMotionUI.InitInformation(Information);
            }
        }

    }
    public void ParseData_cal(TextHandling handling){
        float gravity  = 9.80665f;
        float var;
        float x_displacement;
        float z_displacement;
        angle_theta = Float.parseFloat(data[1][0]);
        angle_phi = Float.parseFloat(data[3][0]);
        magnitude = Float.parseFloat(data[4][0]);

        Rad_theta = (float) Math.toRadians(angle_theta);
        Rad_phi = (float) Math.toRadians(angle_phi);

        x_velocity = (float) Math.cos(Rad_theta) * (float) magnitude * (float) Math.cos(Rad_phi);
        y_velocity = (float) Math.sin(Rad_theta) * (float) magnitude;
        z_velocity = (float) Math.sin(Rad_phi) * (float) magnitude * (float) Math.cos(Rad_theta);

        float time_of_flight = (2 * y_velocity)/gravity;//Time of Flight
        Information[0] = time_of_flight;
        var = (y_velocity * y_velocity) / (2 * gravity);// Max Flight;
        Information[1] = var;

        x_displacement = x_velocity  *  time_of_flight;
        z_displacement = z_velocity * time_of_flight;
        float number = (x_displacement * x_displacement) + (z_displacement * z_displacement);
        var = (float) Math.sqrt(number);
        Information[2] = var;
        Information[3] = x_displacement;
        Information[4] = z_displacement;
        Information[5] = time_of_flight/2;

        FilledData(handling);
        VelocityVector = new Vector3f(x_velocity, y_velocity, z_velocity);
        Motion(FPS * cons);



    }
    public void FilledData(TextHandling handling){
        FilledIncompleteDATA(handling, 0, Rad_theta);
        FilledIncompleteDATA(handling, 2, Rad_phi);
        FilledIncompleteDATA(handling, 5, x_velocity);
        FilledIncompleteDATA(handling, 6, y_velocity);
        FilledIncompleteDATA(handling, 7, z_velocity);

    }
    public void FilledIncompleteDATA(TextHandling handling, int index, float value){
        if(Objects.equals(data[index][0], "")){
            String result = String.format("%.3f", value);
            entities = handling.RenderText(result, new Vector3f(0.79f, 0.79f, 0.79f), Utils.UnfilledData[index]);
            projectileMotionUI.setOnScreenData(index, entities);
        }
    }
    public void Motion(float con){
        VelocityVector.x = VelocityVector.x/con;
        VelocityVector.y = VelocityVector.y/con;
        VelocityVector.z = VelocityVector.z/con;
    }
    public void RenderBody(ShaderProgram shaderProgram, Transformation transformation, Camera camera,  TextHandling handling){

        shaderProgram.setUniforms("view", transformation.calcViewMatrix(camera));
        CheckRender(camera, handling);
        if(render){

            VelocityVector.y -= (float) (acceraltion/ (FPS * cons * 7.89));

            Position.x += VelocityVector.x;
            Position.y += VelocityVector.y;
            Position.z += VelocityVector.z;

            entity.setTranslation(Position);

            if (Position.y <= 0) {
                Position.y = 0;
                entity.setTranslation(Position);
                render = false;
            }
        }
        shaderProgram.setUniforms("ModelMatrix", transformation.setModelMatrix(entity.getTranslation(), entity.getScale(), entity.getRotation()));
        entity.getMesh().Render();
        entity.getMesh().FreeArray();
    }
    public Float[] getInformation(){
        return Information;
    }
}
