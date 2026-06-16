package Simulation.Mechanics;

import Simulation.Core.Mesh;
import Simulation.Core.Utils;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import java.util.ArrayList;
import java.util.List;
public class Model {

    float[] verticesI = {-1.0f, 1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f};
    int[] indec = {0, 1, 2, 0, 3, 2};

    public Model() {

    }
    public Mesh LoadModel(String filename, int code, float[] Colors) {
        List<String> lines = Utils.ReadModel(filename);
        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> TextureCoords = new ArrayList<>();
        List<Vector3f> Normals = new ArrayList<>();
        List<Vector3i> faces = new ArrayList<>();

        for (String line : lines) {
            String[] tokens = line.split("\\s+");
            switch (tokens[0]) {
                case "v":
                    Vector3f vertexVector = new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3])
                    );
                    vertices.add(vertexVector);
                    break;
                case "vt":
                    Vector2f textureCoords = new Vector2f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2])
                    );
                    TextureCoords.add(textureCoords);
                    break;
                case "vn":
                    Vector3f NormalsVex = new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3])
                    );
                    Normals.add(NormalsVex);
                    break;
                case "f":
                    processFaces(tokens[1], faces);
                    processFaces(tokens[2], faces);
                    processFaces(tokens[3], faces);
                    break;
                default:
                    break;
            }
        }
        List<Integer> indices = new ArrayList<>();
        float[] verticesArr = new float[vertices.size() * 3];
        int i = 0;
        for(Vector3f pos: vertices){
            verticesArr[i * 3] = pos.x;
            verticesArr[i * 3 + 1] = pos.y;
            verticesArr[i * 3 + 2] = pos.z;
            i++;
        }
        float [] texCoordsArr = new float[vertices.size() * 2];
        float [] NormalsArr = new float[vertices.size() * 3];

        for(Vector3i Face : faces){
            processVertex(Face.x, Face.y, Face.z, TextureCoords, Normals, indices, texCoordsArr, NormalsArr);
        }
        int[] indicesArr = indices.stream().mapToInt((Integer v) -> v).toArray();
        if(code == 1){
            return new Mesh(verticesArr, indicesArr,null, null, null);
        }
        if(code == 0){
            return new Mesh(verticesArr, indicesArr, null, null, Colors);
        }
        return null;
    }
    public void processVertex(int pos, int texCoords, int normals, List<Vector2f> texList, List<Vector3f> NormalsList, List<Integer> indices,
                              float[] texArr, float[] normalsArr){
        indices.add(pos);
        if(texCoords >= 0){
            Vector2f textureVec = texList.get(texCoords);
            texArr[pos * 2] =textureVec.x;
            texArr[pos * 2 + 1] = 1 -  textureVec.y;
        }
        if(normals >= 0){
            Vector3f normalVec = NormalsList.get(normals);
            normalsArr[pos * 3] = normalVec.x;
            normalsArr[pos * 3 + 1] = normalVec.y;
            normalsArr[pos * 3 + 2] = normalVec.z;
        }
    }
    private void processFaces(String tokens, List<Vector3i> faces){
        String[] line = tokens.split("/");
        int length = line.length;
        int pos, Coords = -1, normals  = -1;
        pos = Integer.parseInt(line[0]) - 1;
        if(length > 1){
            String textureCoords = line[1];
            Coords = textureCoords.length() > 0 ? Integer.parseInt(textureCoords) - 1 : - 1;
            if(length > 2){
                normals = Integer.parseInt(line[2]) - 1;
            }
        }
        Vector3i facesVec = new Vector3i(pos, Coords, normals);
        faces.add(facesVec);
    }
}
