package Simulation.Core;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static String readFile(String filePath){
        String normalizedPath = filePath.startsWith("/") ? filePath : "/" + filePath;
        try (InputStream is = Utils.class.getResourceAsStream(normalizedPath)) {
            if (is == null) {
                throw new RuntimeException("Resource not found on classpath: " + filePath);
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException excp) {
            throw new RuntimeException("Error reading file [" + filePath + "]", excp);
        }
    }
    public static List<String> ReadModel(String filename){
        List<String> ModelInfo = new ArrayList<>();
        String normalizedPath = filename.startsWith("/") ? filename : "/" + filename;
        InputStream is = Utils.class.getResourceAsStream(normalizedPath);
        if (is == null) {
            System.out.println("Resource not found on classpath: " + filename);
            return ModelInfo;
        }
        try (BufferedReader bf = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))){
            String line;
            while ((line = bf.readLine()) != null){
                ModelInfo.add(line);
            }
        } catch (IOException e){
            System.out.println("Cannot find file");
            e.printStackTrace();
        }
        return ModelInfo;
    }

    public static void AddElement(Entity[][] entities, int index, Entity element){
        int n = 0;
        while (n != -1){
            if(entities[index][n] == null){
                entities[index][n] = element;
                n = -1;
            }
            else {
                n += 1;
            }
        }
    }
    public static void RemoveElement(Entity[][] entities, int index){
        Entity[] array = entities[index];
        for (int i = array.length - 1; i >= 0; i--) {
            if (array[i] != null) {
                array[i] = null;
                break;
            }
        }
    }
    public static String[] ChangeDataType(Float[] data){
        String[] ChangeType = new String[data.length];
        for(int i = 0; i<data.length; i++){
            String result = String.format("%.3f", data[i]);
            ChangeType[i] = result;
        }
        return ChangeType;
    }
}
