package Simulation.UI.Mechanics;

import Simulation.Core.*;
import Simulation.UI.TextHandling;
import Simulation.UI.Window;
import org.joml.Matrix4f;
import org.joml.Vector2d;
import org.joml.Vector3f;
import static org.lwjgl.glfw.GLFW.*;


import static org.lwjgl.opengl.GL20.*;

import java.util.ArrayList;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class ProjectileMotion {
    List<Entity> data;
    int numPressed = -1;
    TextHandling handling;
    Vector3f[] Number_Position = Utils.Numbers_Position;
    Vector3f[] saved;
    Vector3f[] cursor_Position = Utils.cursor_Position;
    float[] vertices = {1500, 200, 0, 1800, 200, 0, 1800, 400, 0, 1500, 400, 0};
    int[] indices = {0, 1, 2, 0, 3, 2};
    float[] TexCoords = {0, 0, 1, 0, 1, 1, 0, 1};
    private int boxID = -1;
    int NumPressedTimesNow = -1;
    int NumPressedTimesPrev = -1;
    private boolean mouseWasPressedLastFrame = false;
    List<Entity> cursor = new ArrayList<>();
    List<List<Entity>> Numbers = new ArrayList<>();
    Entity[][] data_Parameter = new Entity[8][20];
    List<Entity> boxEntities = new ArrayList<>();
    String[][] BoxData = new String[8][1];
    boolean RenderInfo = false;
    List<Entity> Information = new ArrayList<>();
    List<Entity> InformationData = new ArrayList<>();

    public ProjectileMotion(TextHandling handling) {
        this.handling = handling;
        data = new ArrayList<>();
        saved = Number_Position.clone();
        init();
        setBoxEntity();
        GenerateInformation();
    }

    public void mechanism(Camera camera, float startX, float endX, float startY, float endY, long window, int ID) {
        Vector2d cameraPos = camera.getMousePos();
        boolean mouseIsPressedNow = camera.isButtonPressed(GLFW_MOUSE_BUTTON_LEFT, window);

        if (cameraPos.x >= startX && cameraPos.x <= endX && cameraPos.y >= startY && cameraPos.y <= endY) {
            if (mouseIsPressedNow && !mouseWasPressedLastFrame) {
                boxID = ID;
            }
        }
    }
    public void IfRenderInformation(Camera camera){
        Vector2d cameraPos = camera.getMousePos();
        if(cameraPos.x >= 8 && cameraPos.x <= 160 && cameraPos.y >= 90 && cameraPos.y <= 150){
            if(camera.isLeftMouseClicked()){
                RenderInfo = !RenderInfo;
            }
        }
    }

    public void Check(Camera camera, long window) {
        mechanism(camera, 1690, 1800, 130, 180, window, 0);
        mechanism(camera, 1790, 1900, 130, 180, window, 1);
        mechanism(camera, 1690, 1800, 200, 270, window, 2);
        mechanism(camera, 1790, 1900, 200, 270, window, 3);
        mechanism(camera, 1690, 1800, 380, 430, window, 4);
        mechanism(camera, 1790, 1900, 380, 430, window, 5);
        mechanism(camera, 1690, 1800, 460, 500, window, 6);
        mechanism(camera, 1790, 1900, 460, 500, window, 7);
        mouseWasPressedLastFrame = camera.isButtonPressed(GLFW_MOUSE_BUTTON_LEFT, window);
    }

    public void RenderCursor(ShaderProgram shaderProgram, Transformation transformation, int TextureID, long window, Camera camera) {
        int sec = LocalTime.now().getSecond();
        if (sec % 2 == 0) {
            if (boxID != -1) {
                shaderProgram.setUniforms("hasTexture", 1.0f);
                for (Entity entity : cursor) {
                    entity.setTranslation(cursor_Position[boxID]);
                    shaderProgram.setUniforms("ModelMatrix", transformation.setModelMatrix(entity.getTranslation(), entity.getScale(), entity.getRotation()));
                    glActiveTexture(GL_TEXTURE0);
                    glBindTexture(GL_TEXTURE_2D, TextureID);
                    entity.getMesh().Render();
                    entity.getMesh().FreeArray();
                }
            }
        }
    }

    public void RemoveNumbers(Camera camera) {
         if(camera.isKeyJustPressed(GLFW_KEY_BACKSPACE)) {
            Number_Position[boxID] = saved[boxID];
            Simulation.Core.Utils.RemoveElement(data_Parameter, boxID);
            DeleteData();
        }
    }
    public void DeleteData(){
        String data = BoxData[boxID][0];
        if(data != null){
            BoxData[boxID][0] = data.substring(0, data.length() - 1);
        }
    }
    public void GenerateBoxData(){
        String s = BoxData[boxID][0];
        String num = "";
        if(numPressed == 10){
            num = ".";
        }
        else {
            num = String.valueOf(numPressed);
        }
        String data = s + num;
        BoxData[boxID][0] = data;
    }

    public void generateRenderNumberList(Camera camera) {
        CheckedClicked(camera);
        RemoveNumbers(camera);
        if (NumPressedTimesPrev != NumPressedTimesNow) {
            Vector3f pos = new Vector3f(Number_Position[boxID].x + 12, Number_Position[boxID].y, Number_Position[boxID].z);
            List<Entity> entities = Numbers.get(numPressed);
            Entity masterEntity = entities.getFirst();
            Entity spawnedEntity = new Entity(masterEntity);
            spawnedEntity.setTranslation(pos);
            Simulation.Core.Utils.AddElement(data_Parameter, boxID, spawnedEntity);
            Number_Position[boxID] = pos;
            NumPressedTimesPrev = NumPressedTimesNow;
            GenerateBoxData();
        }
    }

    public void CheckedClicked(Camera camera) {
        for (int i = 0; i <= 9; i++) {
            if (camera.isKeyJustPressed(GLFW_KEY_0 + i)) {
                numPressed = i;
                NumPressedTimesNow++;
                break;
            }
        }
        if (camera.isKeyJustPressed(GLFW_KEY_PERIOD)) {
            NumPressedTimesNow++;
            numPressed = 10;
        }
    }
    public void GenerateInformation(){
        List<Entity> entities;
        Vector3f scale = new Vector3f(0.8f, 0.8f, 0.8f);
        entities = handling.RenderText("Time of Flight :", scale, new Vector3f(5, 150, 0));
        Information.addAll(entities);
        entities = handling.RenderText("Maximum Height :", scale, new Vector3f(5, 190, 0));
        Information.addAll(entities);
        entities = handling.RenderText("Range :", scale, new Vector3f(5, 240, 0));
        Information.addAll(entities);
        entities = handling.RenderText("x displacement :", scale, new Vector3f(5, 280, 0));
        Information.addAll(entities);
        entities = handling.RenderText("z displacement :", scale, new Vector3f(5, 320, 0));
        Information.addAll(entities);
        entities = handling.RenderText("Time of Max Height :", scale, new Vector3f(5, 360, 0));
        Information.addAll(entities);
    }


    public void RenderNumbers(ShaderProgram shaderProgram, Transformation transformation, int TextureID) {
        if (numPressed != -1) {
            shaderProgram.setUniforms("hasTexture", 1.0f);
            for(int i = 0; i<data_Parameter.length; i++){
                for (Entity entity : data_Parameter[i]) {
                    if(entity != null){
                        shaderProgram.setUniforms("ModelMatrix", transformation.setModelMatrix(entity.getTranslation(), entity.getScale(), entity.getRotation()));
                        glActiveTexture(GL_TEXTURE0);
                        glBindTexture(GL_TEXTURE_2D, TextureID);
                        entity.getMesh().Render();
                        entity.getMesh().FreeArray();
                    }
                }
            }
        }
    }


    public void Numbers() {
        List<Entity> entities;
        Vector3f scale = new Vector3f(0.8f, 0.8f, 0.8f);
        entities = this.handling.RenderText("0", scale, new Vector3f(1700, 145, 0));
        Numbers.add(entities);
        entities = this.handling.RenderText("1", scale, new Vector3f(1700, 145, 0));
        Numbers.add(entities);
        entities = this.handling.RenderText("2", scale, new Vector3f(1700, 145, 0));
        Numbers.add(entities);
        entities = this.handling.RenderText("3", scale, new Vector3f(1700, 145, 0));
        Numbers.add(entities);
        entities = this.handling.RenderText("4", scale, new Vector3f(1700, 145, 0));
        Numbers.add(entities);
        entities = this.handling.RenderText("5", scale, new Vector3f(1700, 145, 0));
        Numbers.add(entities);
        entities = this.handling.RenderText("6", scale, new Vector3f(1700, 145, 0));
        Numbers.add(entities);
        entities = this.handling.RenderText("7", scale, new Vector3f(1700, 145, 0));
        Numbers.add(entities);
        entities = this.handling.RenderText("8", scale, new Vector3f(1700, 145, 0));
        Numbers.add(entities);
        entities = this.handling.RenderText("9", scale, new Vector3f(1700, 145, 0));
        Numbers.add(entities);
        entities = this.handling.RenderText(".", scale, new Vector3f(1700, 145, 0));
        for (int i = 0; i < BoxData.length; i++) {
            BoxData[i][0] = "";
        }
        Numbers.add(entities);

    }

    public void init() {
        List<Entity> entities;
        entities = this.handling.RenderText("Angle of Projection", new Vector3f(1.2f, 1.2f, 1.2f), new Vector3f(1625, 50, 0));
        data.addAll(entities);
        entities = this.handling.RenderText("B/W X & Y From X", new Vector3f(0.9f, 0.9f, 0.9f), new Vector3f(1650, 100, 0));
        data.addAll(entities);
        entities = this.handling.RenderText("B/W X & Z From X", new Vector3f(0.9f, 0.9f, 0.9f), new Vector3f(1650, 200, 0));
        data.addAll(entities);
        entities = this.handling.RenderText("Radians", new Vector3f(0.7f, 0.7f, 0.7f), new Vector3f(1620, 150, 0));
        data.addAll(entities);
        entities = this.handling.RenderText("Degrees", new Vector3f(0.7f, 0.7f, 0.7f), new Vector3f(1760, 150, 0));
        data.addAll(entities);
        entities = this.handling.RenderText("Radians", new Vector3f(0.7f, 0.7f, 0.7f), new Vector3f(1620, 250, 0));
        data.addAll(entities);
        entities = this.handling.RenderText("Degrees", new Vector3f(0.7f, 0.7f, 0.7f), new Vector3f(1760, 250, 0));
        data.addAll(entities);
        entities = this.handling.RenderText("|", new Vector3f(1f, 1f, 1f), new Vector3f(1700, 145, 0));
        cursor.addAll(entities);
        entities = this.handling.RenderText("Velocity", new Vector3f(1.2f, 1.2f, 1.2f), new Vector3f(1690, 350, 0));
        data.addAll(entities);
        entities = this.handling.RenderText("M/S", new Vector3f(1.2f, 1.2f, 1.2f), new Vector3f(1825, 350, 0));
        data.addAll(entities);
        entities = this.handling.RenderText("Magnitude", new Vector3f(0.7f, 0.7f, 0.7f), new Vector3f(1605, 400, 0));
        data.addAll(entities);
        entities = this.handling.RenderText("X-Axis", new Vector3f(0.7f, 0.7f, 0.7f), new Vector3f(1765, 400, 0));
        data.addAll(entities);
        entities = this.handling.RenderText("Y-Axis", new Vector3f(0.7f, 0.7f, 0.7f), new Vector3f(1620, 470, 0));
        data.addAll(entities);
        entities = this.handling.RenderText("Z-Axis", new Vector3f(0.7f, 0.7f, 0.7f), new Vector3f(1765, 470, 0));
        data.addAll(entities);
//        entities = this.handling.RenderText("Gravity", new Vector3f(1.4f, 1.4f, 1.4f), new Vector3f(1700, 560, 0));
//        data.addAll(entities);
//        entities = this.handling.RenderText("Gravity", new Vector3f(0.7f, 0.7f, 0.7f), new Vector3f(1670, 590, 0));
//        data.addAll(entities);
        entities = handling.RenderText("Information", new Vector3f(1.4f, 1.4f, 1.4f), new Vector3f(10, 100, 0));
        data.addAll(entities);
        Numbers();
    }


    public void setBoxEntity() {
        Mesh mesh = new Mesh(vertices, indices, TexCoords, null, null);
        Vector3f rotation = new Vector3f(0, 0, 0);
        Entity entity0 = new Entity(new Vector3f(0.18f, 0.18f, 0.18f), new Vector3f(1425, 110, 0), rotation, mesh, 0.0f); // id 0
        Entity entity1 = new Entity(new Vector3f(0.18f, 0.18f, 0.18f), new Vector3f(1570, 110, 0), rotation, mesh, 0.0f); // id 1
        Entity entity2 = new Entity(new Vector3f(0.18f, 0.18f, 0.18f), new Vector3f(1425, 210, 0), rotation, mesh, 0.0f); // id 2
        Entity entity3 = new Entity(new Vector3f(0.18f, 0.18f, 0.18f), new Vector3f(1570, 210, 0), rotation, mesh, 0.0f); // id 3
        Entity entity4 = new Entity(new Vector3f(0.18f, 0.18f, 0.18f), new Vector3f(1430, 360, 0), rotation, mesh, 0.0f); // id 4
        Entity entity5 = new Entity(new Vector3f(0.18f, 0.18f, 0.18f), new Vector3f(1565, 360, 0), rotation, mesh, 0.0f); // id 5
        Entity entity6 = new Entity(new Vector3f(0.18f, 0.18f, 0.18f), new Vector3f(1430, 430, 0), rotation, mesh, 0.0f); // id 6
        Entity entity7 = new Entity(new Vector3f(0.18f, 0.18f, 0.18f), new Vector3f(1565, 430, 0), rotation, mesh, 0.0f); // id 7
        boxEntities.add(entity5);
        boxEntities.add(entity0);
        boxEntities.add(entity1);
        boxEntities.add(entity2);
        boxEntities.add(entity3);
        boxEntities.add(entity4);
        boxEntities.add(entity6);
        boxEntities.add(entity7);
    }

    public void RenderText(ShaderProgram shaderProgram, Transformation transformation, int TextTextureID, Overview overview, Camera camera, long window, Simulation.Mechanics.ProjectileMotion projectileMotion) {
        if (overview.getOpen()[0]) {
            IfRenderInformation(camera);
            generateRenderNumberList(camera);
            shaderProgram.bind();
            RenderInformation(shaderProgram, transformation, TextTextureID, projectileMotion);
            RenderInputFields(shaderProgram, transformation);
            RenderCursor(shaderProgram, transformation, TextTextureID, window, camera);
            RenderNumbers(shaderProgram, transformation, TextTextureID);
            Check(camera, window);
            shaderProgram.setUniforms("hasTexture", 1.0f);
            for (Entity entity : data) {
                shaderProgram.setUniforms("ModelMatrix", transformation.setModelMatrix(entity.getTranslation(), entity.getScale(), entity.getRotation()));
                glActiveTexture(GL_TEXTURE0);
                glBindTexture(GL_TEXTURE_2D, TextTextureID);
                entity.getMesh().Render();
                entity.getMesh().FreeArray();
            }
            shaderProgram.unbind();
        }
    }
    public void InitInformation(Float[] information){
        String[] data = Simulation.Core.Utils.ChangeDataType(information);
        List<Entity> entities;
        Vector3f scale = new Vector3f(0.8f, 0.8f, 0.8f);
        for(int k = 0; k<data.length; k++){
            entities = handling.RenderText(data[k], scale, Utils.Information[k]);
            InformationData.addAll(entities);
        }
    }
    public void RenderInformation(ShaderProgram shaderProgram, Transformation  transformation, int TextureID, Simulation.Mechanics.ProjectileMotion projectileMotion){
        if(RenderInfo){
            shaderProgram.setUniforms("hasTexture", 1.0f);
            RenderInformationData(shaderProgram, transformation, TextureID);
            for(Entity entity: Information){
                glActiveTexture(GL_TEXTURE0);
                glBindTexture(GL_TEXTURE_2D, TextureID);
                shaderProgram.setUniforms("ModelMatrix", transformation.setModelMatrix(entity.getTranslation(), entity.getScale(), entity.getRotation()));
                entity.getMesh().Render();
                entity.getMesh().FreeArray();
            }
        }
    }
    public void RenderInformationData(ShaderProgram shaderProgram, Transformation transformation, int TextureID){
        if(InformationData.getFirst() != null){
            shaderProgram.setUniforms("hasTexture", 1.0f);
            for(Entity entity: InformationData) {
                glActiveTexture(GL_TEXTURE0);
                glBindTexture(GL_TEXTURE_2D, TextureID);
                shaderProgram.setUniforms("ModelMatrix", transformation.setModelMatrix(entity.getTranslation(), entity.getScale(), entity.getRotation()));
                entity.getMesh().Render();
                entity.getMesh().FreeArray();
            }
        }
    }

    public void RenderInputFields(ShaderProgram shaderProgram, Transformation transformation) {
        shaderProgram.setUniforms("hasTexture", 0.0f);
        shaderProgram.setUniforms("Color", new Vector3f(0.3294f, 0.3294f, 0.3294f));
        for (Entity entity : boxEntities) {
            shaderProgram.setUniforms("ModelMatrix", transformation.setModelMatrix(entity.getTranslation(), entity.getScale(), entity.getRotation()));
            entity.getMesh().Render();
            entity.getMesh().FreeArray();
        }
    }
    public String[][] getBoxData(){
        return BoxData;
    }
    public Entity[][] getData_Parameter(){
        return data_Parameter;
    }
    public void setOnScreenData(int index, List<Entity> entities){
        for(Entity entity: entities){
            Simulation.Core.Utils.AddElement(data_Parameter, index, entity);
        }
    }
    public List<Entity> getInformation(){
        return Information;
    }
}


