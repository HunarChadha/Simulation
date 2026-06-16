package Simulation.Core;
import org.joml.Matrix4f;
import org.joml.Matrix4f.*;
import org.joml.Vector3f;
import org.joml.Vector3f.*;

import java.awt.*;

public class Transformation {
    int height;
    int width;
    private final float FOV = (float) Math.toRadians(60.f);
    float z_near = 0.01f;
    float z_Far = 1000.0f;

    public Transformation(){
    }
    public Matrix4f setProjection(int height, int width){
        Matrix4f projectionMatrix = new Matrix4f();
        projectionMatrix.identity();
        projectionMatrix.perspective(FOV, (float) width/height, z_near, z_Far);
        return projectionMatrix;

    }
    public Matrix4f setModelMatrix(Vector3f translation, Vector3f scale, Vector3f rotation){
        Matrix4f ModelMatrix = new Matrix4f();
        ModelMatrix.identity()
                .translate(translation)
                .scale(scale)
                .rotateX((float) Math.toRadians(rotation.x))
                .rotateY((float) Math.toRadians(rotation.y))
                .rotateZ((float) Math.toRadians(rotation.z));

        return ModelMatrix;
    }
    public Matrix4f calcViewMatrix(Camera camera){
        Vector3f Pos = camera.getPosition();
        Vector3f Ros = camera.getRotation();
        Matrix4f viewMatrix;
        viewMatrix = new Matrix4f();
        viewMatrix.identity();
        //.rotate((float) Math.toRadians(Ros.x), new Vector3f(1, 0, 0)).
        viewMatrix.
                rotate((float) Math.toRadians(Ros.y), new Vector3f(0, 1, 0));
                //rotate((float) Math.toRadians(Ros.z), new Vector3f(0, 0, 1));
        viewMatrix.translate(Pos.x, Pos.y, Pos.z);
        return viewMatrix;
    }

    public Matrix4f calcViewMatrixSimulation(Camera camera){
        Vector3f Pos = camera.getPosition();
        Vector3f Ros = camera.getRotation();
        Matrix4f viewMatrix;
        viewMatrix = new Matrix4f();
        viewMatrix.identity();
        //.rotate((float) Math.toRadians(Ros.x), new Vector3f(1, 0, 0)).
        viewMatrix.
                rotate((float) Math.toRadians(Ros.y), new Vector3f(0, 1, 0));
        //rotate((float) Math.toRadians(Ros.z), new Vector3f(0, 0, 1));
        viewMatrix.translate(Pos.x, Pos.y, Pos.z);
        return viewMatrix;
    }


    public Matrix4f setOrthoMatrix(float right, float bottom){
        Matrix4f orthoMatrix;
        orthoMatrix = new Matrix4f();
        orthoMatrix.identity();
        orthoMatrix.ortho(0.0f, right, bottom, 0, -1.0f, 1.0f);
        return orthoMatrix;
    }
}
