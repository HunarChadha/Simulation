package Simulation.UI.Mechanism;

import Simulation.Core.Camera;
import Simulation.Core.Entity;
import Simulation.UI.Mechanics.Overview;
import Simulation.UI.Text;
import org.joml.Vector2d;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Mechanism {
    Camera camera;
    private float direction = -1; // 0.0 = Closed, 1.0 = Fully Open
    private boolean mouseWasPressedLastFrame = false;
    boolean[] isClicked = new boolean[6];
    private final List<List<Entity>> texts;
    float var = 200;
    int code = 0;
    Overview overview;
    Text text;

    public Mechanism(Camera camera, Text text, Overview overview) {
        this.camera = camera;
        texts = new ArrayList<>();
        this.text = text;
        this.overview = overview;
        init();
    }
    public void init(){
        texts.add(text.getMechanics());
        texts.add(text.getElectro());
        texts.add(text.getQuantum());
        texts.add(text.getOptics());
        texts.add(text.getFluid());
        texts.add(text.getThermo());
        texts.add(text.getParticle());
        Arrays.fill(isClicked, false);
    }
    public void isClicked(long window, float startX, float endX, float startY, float endY, int code, boolean[] list, int code2) {
        Vector2d cameraPos = this.camera.getMousePos();
        boolean mouseIsPressedNow = camera.isButtonPressed(GLFW.GLFW_MOUSE_BUTTON_LEFT, window);

        if (cameraPos.x >= startX && cameraPos.x <= endX && cameraPos.y >= startY && cameraPos.y <= endY) {
            if (mouseIsPressedNow && !mouseWasPressedLastFrame) {
                if(code2 == 0){
                    direction = -direction;
                    list[code] = !list[code];
                    this.code = code;
                }
                if(code2 == 1){
                    list[code] = true;
                }
            }
        }
    }
    public void check(long window){
        isClicked(window, 1620, 1900, 130, 250, 1, isClicked, 0);
        isClicked(window, 1660, 1850, 60, 130, 0, isClicked, 0);
        isClicked(window, 1600, 1900, 250, 350, 2, isClicked, 0);
        isClicked(window, 1640, 1900, 350, 450, 3, isClicked, 0);
        isClicked(window, 1635, 1900, 450, 550, 4, isClicked, 0);
        isClicked(window, 1630, 1900, 550, 650, 5, isClicked, 0);

        if(isClicked[0]){
            isClicked(window, 1620, 1900, 130, 250, 0, overview.getOpen(), 1);
            isClicked(window, 1620, 1900, 260, 390, 1, overview.getOpen(), 1);
        }
        mouseWasPressedLastFrame = this.camera.isButtonPressed(GLFW.GLFW_MOUSE_BUTTON_LEFT, window);
    }
    public void applyPosition(List<Entity> entities, float direction, float speed, int code){
        for(Entity entity: entities){
            entity.getTranslation().y += (direction * speed);
            var += (direction * speed);
        }
    }
    public void Mechanism(){
        for(int k = code+1; k<texts.size(); k++){
            changeUI(((float) 28000 /(code+1)), 200, texts.get(k), isClicked);
        }
    }
    public void changeUI(float upperLimit, float lowerLimit, List<Entity> entities, boolean[] lists){
        float speed = 10;
        if(lists[code] && var < upperLimit){
            applyPosition(entities, direction, speed, code);
        }
        if(!lists[code] && var > lowerLimit){
            applyPosition(entities, direction, speed, code);
        }
    }

    public boolean[] getIsClicked() {
        return isClicked;
    }
}