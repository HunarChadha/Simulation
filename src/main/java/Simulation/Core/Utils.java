package Simulation.Core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static String readFile(String filePath){
        String str;
        try {
            str = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException excp) {
            throw new RuntimeException("Error reading file [" + filePath + "]", excp);
        }
        return str;
    }
    public static List<String> ReadModel(String filename){
        List<String> ModelInfo = new ArrayList<>();
        try (BufferedReader bf = new BufferedReader(new FileReader(filename))){
            String line;
            while ((line = bf.readLine()) != null){
                ModelInfo.add(line);
            }
        }catch (Exception e){
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
