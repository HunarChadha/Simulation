#version 330 core
in vec3 worldPos;
out vec4 FragColor;

uniform vec3 cameraPos;

const float gridSpacing = 1.0;
const float majorGridSpacing = 10.0;
const float maxDistance = 100.0;

void main() {
    vec2 dv = fwidth(worldPos.xz);

    // Fine grid
    vec2 coord = worldPos.xz / gridSpacing;
    vec2 grid = abs(fract(coord - 0.5) - 0.5) / dv;
    float line = min(grid.x, grid.y);
    float fineGridAlpha = 1.0 - min(line, 1.0);

    // Major grid
    vec2 majorCoord = worldPos.xz / majorGridSpacing;
    vec2 majorGrid = abs(fract(majorCoord - 0.5) - 0.5) / (dv / majorGridSpacing);
    float majorLine = min(majorGrid.x, majorGrid.y);
    float majorGridAlpha = 1.0 - min(majorLine, 1.0);

    // X axis (Red), Z axis (Blue), Y axis origin marker (Green)
    float axisWeightX = 1.0 - min(abs(worldPos.z) / dv.y, 1.0);
    float axisWeightZ = 1.0 - min(abs(worldPos.x) / dv.x, 1.0);
    float axisWeightY = 1.0 - min(
    max(abs(worldPos.x) / (dv.x * 2.0), abs(worldPos.z) / (dv.y * 2.0)),
    1.0
    );

    // Fade
    float viewDist = length(worldPos.xz - cameraPos.xz);
    float fade = smoothstep(maxDistance, maxDistance * 0.4, viewDist);

    // Blend
    vec4 color = vec4(0.3, 0.3, 0.3, fineGridAlpha * 0.4);

    if (majorGridAlpha > 0.0)
    color = mix(color, vec4(0.45, 0.45, 0.45, 0.8), majorGridAlpha);
    if (axisWeightX > 0.0)
    color = mix(color, vec4(0.85, 0.25, 0.25, 1.0), axisWeightX);   // Red X
    if (axisWeightZ > 0.0)
    color = mix(color, vec4(0.25, 0.45, 0.85, 1.0), axisWeightZ);   // Blue Z
    if (axisWeightY > 0.0)
    color = mix(color, vec4(0.25, 0.85, 0.25, 1.0), axisWeightY);   // Green Y origin

    if (color.a * fade < 0.05) discard;

    FragColor = vec4(color.rgb, color.a * fade);
}