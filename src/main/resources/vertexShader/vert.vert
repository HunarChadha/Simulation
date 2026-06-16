#version 330

layout(location = 0) in vec3 Position;
layout(location = 1) in vec2 texCoords;
layout(location = 3) in vec3 VertexNormals;

uniform mat4 projectionMatrix;
uniform mat4 ModelMatrix;
uniform mat4 view;
out vec3 aPos;

void main(){
    gl_Position = projectionMatrix * view * ModelMatrix *vec4(Position, 1.0);
}