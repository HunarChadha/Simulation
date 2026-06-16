package Simulation.Core;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform3f;

public class ShaderProgram {

    int ProgramID;
    private int FragmentShader ;
    private int vertexShader;

    private final Map<String, Integer> uniforms;
    public ShaderProgram() throws Exception{
        ProgramID = glCreateProgram();
        uniforms = new HashMap<>();
        if(ProgramID == 0) {
            throw new Exception("Error ProgramID is 0");
        }
    }
    public void createUniforms(String UniformName) {
        int uniformLocation = glGetUniformLocation(ProgramID, UniformName);
        if (uniformLocation == -1) {
            System.out.println("WARNING: Uniform '" + UniformName + "' not found in shader!");
        }
        uniforms.put(UniformName, uniformLocation);
    }
    public void createFragShader(String ShaderType) throws Exception{
        FragmentShader = createShader(ShaderType, GL_FRAGMENT_SHADER);
    }
    public void createVertexShader(String shaderType)throws Exception{
        vertexShader = createShader(shaderType, GL_VERTEX_SHADER);
    }

    public void setUniforms(String UniformName, Matrix4f value){
        try(MemoryStack stack = MemoryStack.stackPush()){
            FloatBuffer fb = stack.mallocFloat(16);
            value.get(fb);
            glUniformMatrix4fv(uniforms.get(UniformName), false, fb);
        }
    }
    public void setUniforms(String UniformName, float value){
        glUniform1f(uniforms.get(UniformName), value);
    }
    public void setUniforms(String UniformName, Vector3f value){
        glUniform3f(uniforms.get(UniformName), value.x, value.y, value.z);
    }
    public void setUniforms(String UniformName, float x, float y, float z){
        glUniform3f(uniforms.get(UniformName), x, y, z);
    }

    public int createShader(String shaderType, int shaderCode)throws Exception{
        int shaderID = glCreateShader(shaderCode);
        if(shaderID == 0){
            throw new Exception("Cannot create shader");
        }
        glShaderSource(shaderID, shaderType);
        glCompileShader(shaderID);
        if(glGetShaderi(shaderID, GL_COMPILE_STATUS)==0){
            throw new RuntimeException("Error Compile shader" + glGetShaderInfoLog(shaderID));
        }
        glAttachShader(ProgramID, shaderID);
        return shaderID;
    }
    public void link() throws Exception{
        glLinkProgram(ProgramID);
        if (glGetProgrami(ProgramID, GL_LINK_STATUS) == 0){
            throw new Exception("Cannot link" + glGetProgramInfoLog(ProgramID, 1024));
        }
        if(vertexShader != 0){
            glDetachShader(ProgramID, vertexShader);
        }
        if(FragmentShader != 0){
            glDetachShader(ProgramID, FragmentShader);
        }
        glValidateProgram(ProgramID);
        if(glGetProgrami(ProgramID, GL_VALIDATE_STATUS) == 0){
            throw new Exception("Warning validate" + glGetProgramInfoLog(ProgramID, 1024));
        }
    }

    public void bind(){
        glUseProgram(ProgramID);
    }
    public void unbind(){
        glUseProgram(0);
    }
    public void cleanUp() {
        unbind();
        if (ProgramID != 0) {
            glDeleteProgram(ProgramID);
        }
    }
}
