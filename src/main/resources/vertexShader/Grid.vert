#version 330 core
layout (location = 0) in vec3 inPos;

out vec3 worldPos;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 ModelMatrix;

void main() {
    worldPos = inPos;
    gl_Position = projection * view * ModelMatrix * vec4(inPos, 1.0);
}