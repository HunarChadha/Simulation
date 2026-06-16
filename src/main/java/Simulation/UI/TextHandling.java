package Simulation.UI;

import Simulation.Core.Entity;
import Simulation.Core.Mesh;
import org.joml.Vector3f;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class TextHandling {
    List<Integer> ASCII = new ArrayList<>();
    List<Float> x = new ArrayList<>();
    List<Float> y = new ArrayList<>();
    List<Float> offsetX = new ArrayList<>();
    List<Float> xAdvance = new ArrayList<>();
    List<Float> offsetY = new ArrayList<>();
    List<Float> width = new ArrayList<>();
    List<Float> height = new ArrayList<>();
    List<String> data = new ArrayList<>();

    int[] indices = {
            0,1,2,
            0,3,2
            // Second Triangle
    };

    public TextHandling(){
        ParseList("D:\\Physics simulation\\physics.Simulation\\src\\main\\resources\\textureAtlas.fnt");
        init();
    }
    public void init(){
        ParseIDS(data, 1, ASCII);
        ParseData(data, 2, x);
        ParseData(data, 3, y);
        ParseData(data, 4, width);
        ParseData(data, 5, height);
        ParseData(data, 6, offsetX);
        ParseData(data, 7, offsetY);
        ParseData(data, 8, xAdvance);

    }
    public int getAsciiCode(char chr){
        return (int) chr;
    }
    public float[] generateTexCoords(int code){
        int index = ASCII.indexOf(code);
        float[] texCoords = new float[8];
        float cons = (float) 1 /256;
        float x_coord = x.get(index);
        float y_coord = y.get(index);
        texCoords[0] = x_coord * cons;
        texCoords[1] = y_coord * cons;
        texCoords[2] = (x_coord + width.get(index)) * cons;
        texCoords[3] = y_coord * cons;
        texCoords[4] = (x_coord + width.get(index)) * cons;
        texCoords[5] = (y_coord + height.get(index)) * cons;
        texCoords[6] = x_coord * cons;
        texCoords[7] = (y_coord + height.get(index)) * cons;
        return texCoords;
    }
    public List<Entity> RenderText(String Text, Vector3f scale, Vector3f position){
        String[] text = Text.split("");
        List<Integer> ids = new ArrayList<>();
        List<Entity> entities = new ArrayList<>();
        for (String s : text) {
            int code = getAsciiCode(s.charAt(0));
            ids.add(code);
        }
        float breath;
        float length = 0;
        float offy;

        for(int i = 0; i<ids.size(); i++){
            Mesh mesh = new Mesh(generateBox(ids.get(i)), indices, generateTexCoords(ids.get(i)), null, null);
            breath = offsetX.get(ASCII.indexOf(ids.get(i)));
            offy = offsetY.get(ASCII.indexOf(ids.get(i)));
            if(i > 0){
                length += xAdvance.get(ASCII.indexOf(ids.get(i-1)));
                Entity entity = new Entity(scale, new Vector3f(position.x + ((length + breath) * scale.x), position.y + (offy * scale.x), position.z), new Vector3f(0, 0, 0), mesh, 1.0f);
                entities.add(entity);
            }
            else{
                entities.add(new Entity(scale, new Vector3f(position.x + (breath * scale.x), position.y + (offy * scale.x), position.z), new Vector3f(0, 0, 0), mesh, 1.0f));
            }
        }
        return entities;
    }
    public float[] generateBox(int code){
        int index = ASCII.indexOf(code);
        float[] vertices = new float[12];
        float breath = width.get(index);
        float length = height.get(index);
        vertices[0] = 0;
        vertices[1] = 0;
        vertices[2] = 0;
        vertices[3] = breath;
        vertices[4] = 0;
        vertices[5] = 0;
        vertices[6] = breath;
        vertices[7] = length;
        vertices[8] = 0;
        vertices[9] = 0;
        vertices[10] = length;
        vertices[11] = 0;
        return vertices;
    }
    public void ParseData(List<String> data, int index, List<Float> storeData){
        for(int i = index; i<data.size(); i+=11){
            int equalPos = data.get(i).indexOf("=");
            String id = data.get(i).substring(equalPos + 1);
            float var = Float.parseFloat(id);
            storeData.add(var);
        }
    }
    public void ParseIDS(List<String> data, int index, List<Integer> storeData){
        for(int i = index; i<data.size(); i+=11){
            int equalPos = data.get(i).indexOf("=");
            String id = data.get(i).substring(equalPos + 1);
            int var = Integer.parseInt(id);
            storeData.add(var);
        }
    }
    public void ParseList(String filePath) {
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            lines.forEach(line -> {
                String[] piece = line.split(" ");
                for(int i = 0; i<piece.length; i++){
                    if(piece[i].length() > 2){
                        data.add(piece[i]);
                    }
                }
            });

        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
