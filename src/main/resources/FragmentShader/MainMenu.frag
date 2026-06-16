#version 330

out vec4 FragColor;
in vec2 outTexCoords;
uniform vec3 Color;
uniform sampler2D TexTexture;
uniform float hasTexture;

void main() {
    if(hasTexture == 1.0){
        FragColor = texture(TexTexture, outTexCoords);
    }
    if(hasTexture == 0.0){
        FragColor = vec4(Color, 1.0f);
    }
}