#version 330

layout(location = 0) in vec3 Position;
layout(location = 1) in vec2 texCoords;

out vec2 outTexCoords;

uniform mat4 ModelMatrix;
uniform mat4 orthoMatrix;

void main() {
    gl_Position = orthoMatrix * ModelMatrix *  vec4(Position , 1.0);
    outTexCoords = texCoords;
}