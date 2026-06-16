package Simulation.Core;

import static org.lwjgl.opengl.GL30.*;
import java.nio.*;
import org.lwjgl.system.MemoryUtil;

public class Mesh {
    int length;
    int Vao;
    int posVbo;
    int texVbo;
    int indVbo;
    int NormalVBo;

    public Mesh(float[] vertices, int[] indices, float[] texCoords, float[]Normals, float[] Colors){
        FloatBuffer verticesBuffer = MemoryUtil.memAllocFloat(vertices.length);
        verticesBuffer.put(vertices).flip();
        this.length = indices.length;
        this.Vao = glGenVertexArrays();
        glBindVertexArray(this.Vao);
        this.posVbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, this.posVbo);
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        MemoryUtil.memFree(verticesBuffer);

        IntBuffer indicesBuffer = MemoryUtil.memAllocInt(indices.length);
        indicesBuffer.put(indices).flip();
        this.indVbo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.indVbo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
        MemoryUtil.memFree(indicesBuffer);

        if(texCoords != null){
            FloatBuffer textureBuffer = MemoryUtil.memAllocFloat(texCoords.length);
            textureBuffer.put(texCoords).flip();
            this.texVbo = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, this.texVbo);
            glBufferData(GL_ARRAY_BUFFER, textureBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
            MemoryUtil.memFree(textureBuffer);
        }

        if(Normals != null){
            FloatBuffer NormalsBuffer = MemoryUtil.memAllocFloat(Normals.length);
            NormalsBuffer.put(Normals).flip();
            this.NormalVBo = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, this.NormalVBo);
            glBufferData(GL_ARRAY_BUFFER, NormalsBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(3, 3, GL_FLOAT, false, 0, 0);
            MemoryUtil.memFree(NormalsBuffer);
        }

        if(Colors != null){
            FloatBuffer ColorsBuffer = MemoryUtil.memAllocFloat(Colors.length);
            ColorsBuffer.put(Colors).flip();
            int ColorsID = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, ColorsID);
            glBufferData(GL_ARRAY_BUFFER, ColorsBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);
            MemoryUtil.memFree(ColorsBuffer);
        }
    }
    public void Render(){
        glBindVertexArray(getVao());
        storeArrayData(0);
        storeArrayData(1);
        storeArrayData(2);
        storeArrayData(3);
        glDrawElements(GL_TRIANGLES, getLength(), GL_UNSIGNED_INT, 0);

    }
    public void FreeArray(){
        FreeArrayData(0);
        FreeArrayData(1);
        FreeArrayData(3);
        FreeArrayData(2);
    }
    public void cleanUp(){
        glBindVertexArray(0);
        glDisableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(this.indVbo);
        glDeleteBuffers(this.posVbo);
        glDeleteBuffers(this.texVbo);
        glDeleteVertexArrays(getVao());
    }
    public void storeArrayData(int index){
        glEnableVertexAttribArray(index);
    }
    public void FreeArrayData(int index){
        glDisableVertexAttribArray(index);
    }
    public int getVao(){
        return this.Vao;
    }
    public int getLength(){
        return this.length;
    }
}
