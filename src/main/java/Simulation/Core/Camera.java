package Simulation.Core;

import static org.lwjgl.glfw.GLFW.*;

import Simulation.UI.Window;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;

public class Camera {
    Vector3f Position;
    Vector3f rotation;
    public Vector3f cameraInc;
    Vector2d MousePos;
    Vector2d InitialMousePos;
    Vector3f CameraPos;
    Vector3f InitialPlayerPos;
    private static final boolean[] currentKeyState = new boolean[1024];
    private static final boolean[] previousKeyState = new boolean[1024];
    Vector3f EntityPos;
    private  boolean leftMouseClicked = false;
    private  boolean RightMouseClicked = false;
    Vector3f PLayerPos;
    boolean STARTGAME = true;

    public static boolean WindowOpen = true;
    public Camera(){
        Position = new Vector3f(0, 0, 0);
        rotation = new Vector3f(0, 0, 0);
        cameraInc = new Vector3f(0, 0, 0);
        MousePos = new Vector2d(0, 0);
        InitialMousePos = new Vector2d(0, 0);
        InitialPlayerPos = new Vector3f(0, 0, 0);
        CameraPos = new Vector3f(0, 0, 0);
        EntityPos = new Vector3f(-0.2f, 0, -2);
        PLayerPos = new Vector3f(0, 0, 0);
    }
    public  void update(long window) {
        for (int i = 0; i < currentKeyState.length; i++) {
            previousKeyState[i] = currentKeyState[i];
            if ((i >= 32 && i <= 348)) {
                currentKeyState[i] = (glfwGetKey(window, i) == GLFW_PRESS);
            } else {
                currentKeyState[i] = false;
            }
        }
    }
    public  boolean isKeyJustPressed(int keyCode) {
        if (keyCode < 32 || keyCode >= currentKeyState.length) {
            return false;
        }
        return currentKeyState[keyCode] && !previousKeyState[keyCode];
    }
    public  void init(long window) {
        glfwSetMouseButtonCallback(window, (windowHandle, button, action, mods) -> {
            if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS) {
                leftMouseClicked = true;
            }
        });
//        glfwSetMouseButtonCallback(window, (windowHandle, button, action, mods) -> {
//            if (button == GLFW_MOUSE_BUTTON_RIGHT && action == GLFW_PRESS) {
//                RightMouseClicked = true;
//            }
//        });
    }
    public  boolean isLeftMouseClicked() {
        if (leftMouseClicked) {
            leftMouseClicked = false; // Reset it immediately so it only triggers once
            return true;
        }
        return false;
    }
    public  boolean isRightMouseClicked() {
        if (RightMouseClicked) {
            RightMouseClicked = false; // Reset it immediately so it only triggers once
           return true;
        }
        return false;
    }

    public void setPosition(float OffsetX, float OffsetY, float OffsetZ, Vector3f Position, float SPEED){
        if(OffsetZ == 1){
            Position.x -= Math.sin(Math.toRadians(rotation.y)) * SPEED;
            Position.z += Math.cos(Math.toRadians(rotation.y)) * SPEED;

        }
        if(OffsetX == 1){
            Position.x += Math.sin(Math.toRadians(rotation.y - 90)) * SPEED;
            Position.z -= Math.cos(Math.toRadians(rotation.y - 90)) * SPEED;

        }
        if(OffsetX == - 1){
            Position.x += Math.sin(Math.toRadians(rotation.y + 90)) * SPEED;
            Position.z -= Math.cos(Math.toRadians(rotation.y + 90)) * SPEED;

        }
        if(OffsetZ == -1){
            Position.x += Math.sin(Math.toRadians(rotation.y)) * SPEED;
            Position.z -= Math.cos(Math.toRadians(rotation.y)) * SPEED;

        }
        if(OffsetY == -1){
            Position.y += -SPEED;
        }
        if(OffsetY == 1){
            Position.y += SPEED;
        }
    }
    public boolean isKeyPressed(int keyCode, long window){
        return glfwGetKey(window, keyCode) != GLFW_RELEASE;
    }
    public boolean isKeyReleased(int keyCode, long window){
        return glfwGetKey(window, keyCode) == GLFW_RELEASE;
    }
    public boolean isButtonPressed(int ButtonCode, long window){
        return glfwGetMouseButton(window, ButtonCode) != GLFW_RELEASE;
    }

    public void getGetMousePos(long window){
        GLFWCursorPosCallback cursorPosCallback;

        glfwSetCursorPosCallback(window, cursorPosCallback = GLFWCursorPosCallback.create((Window, xPos, yPos)->{
            MousePos.x =  xPos;
            MousePos.y = yPos;
        }));
    }
    public void setRotation(long window){
        getGetMousePos(window);
        if(isButtonPressed(GLFW_MOUSE_BUTTON_LEFT, window)){
            if(InitialMousePos.x != MousePos.x){
                if(MousePos.x > InitialMousePos.x){
                    rotation.y += 2;
                    InitialMousePos.x = MousePos.x;
                }
                if(MousePos.x < InitialMousePos.x){
                    rotation.y -= 2;
                    InitialMousePos.x = MousePos.x;
                }
            }
            if(InitialMousePos.y != MousePos.y){
                if(MousePos.y > InitialMousePos.y){
                    rotation.x += 1;
                    InitialMousePos.y = MousePos.y;
                }
                if(MousePos.y < InitialMousePos.y){
                    rotation.x -= 1;
                    InitialMousePos.y = MousePos.y;
                }
            }
            if(rotation.y > 360){
                rotation.y = 0;
            }
            if(rotation.x > 180){
                rotation.x = 180;
            }
            if(rotation.x < -180){
                rotation.x = -180;
            }
        }
    }
    public void MovePosition(long window){
        cameraInc.set(0, 0, 0);
        if(isKeyPressed(GLFW_KEY_W, window)){
            cameraInc.z = 1;
        }
        if(isKeyPressed(GLFW_KEY_S, window)){
            cameraInc.z = -1;
        }
        if(isKeyPressed(GLFW_KEY_A, window)){
            cameraInc.x = -1;
        }
        if(isKeyPressed(GLFW_KEY_D, window)){
            cameraInc.x =   1;
        }
        if(isKeyPressed(GLFW_KEY_ENTER, window)){
            WindowOpen = false;
        }
        if(isKeyPressed(GLFW_KEY_E, window)){
            cameraInc.y = -1;
        }
        if(isKeyPressed(GLFW_KEY_Q, window)){
            //STARTGAME = false;
            cameraInc.y = 1;
        }
        setPosition(cameraInc.x, cameraInc.y, cameraInc.z, this.Position, 0.005f);
    }
    public Vector3f getPosition(){
        return this.Position;
    }

    public Vector3f getRotation() {
        return rotation;
    }
    public Vector2d getMousePos(){
        return this.MousePos;
    }
}
