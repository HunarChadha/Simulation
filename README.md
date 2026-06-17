# Physics Simulator (Java + LWJGL)

An interactive 3D physics simulator built from scratch in Java. Type in launch angle, azimuth, and velocity through a custom-built on-screen input UI, and watch a 3D model fly the exact trajectory the kinematics equations predict — angle, time of flight, max height, and range, all computed live.

This is a personal/learning project. The renderer, the UI (input boxes, buttons, cursor blinking), and even the text rendering (a hand-parsed bitmap font atlas) are all hand-written rather than using an existing engine or GUI framework.

## Features

- **3D projectile motion simulation** — real kinematics equations (time of flight, max height, range, displacement) drive an animated 3D model along its actual trajectory
- **Custom on-screen input system** — click-to-select number boxes you type directly into (no GUI framework), with backspace support and a blinking cursor
- **Hand-built text rendering** — a custom bitmap font atlas parser and renderer, used for all in-app text and the results readout
- **Orbiting 3D camera** to view the simulated trajectory from any angle
- **Main menu / overview UI** rendered with custom OpenGL quads
- **Grid system** for spatial reference in the simulation view
- Automatically fills rest of the data
- Architecture in place for additional mechanics modules (pendulum, generic mechanism) — see Known Issues below

## Tech stack

- **Java**
- **LWJGL 3** — OpenGL/GLFW bindings
- **JOML** — vector/matrix math
- **GLSL** — custom vertex/fragment shaders for the main menu, grid, and 3D scene
- **Gradle** — build tool

## Project structure

```
main/
├── java/
│   └── Simulation/
│       ├── Core/        # Camera, mesh, shader program, texture, transformation, file-loading utils
│       ├── Mechanics/   # Projectile motion physics + 3D model loading
│       ├── UI/          # Window, menu, grid, text rendering, and UI-side input handling
│       │   ├── Mechanics/   # On-screen input UI for projectile motion, overview, pendulum (stub)
│       │   └── Mechanism/   # Generic mechanism UI (in progress)
│       └── Main.java
└── resources/
    ├── FragmentShader/  # GLSL fragment shaders
    ├── vertexShader/    # GLSL vertex shaders
    ├── textureAtlas_0.png + textureAtlas.fnt   # Bitmap font for text rendering
    └── Untitled.obj / Untitled.mtl             # 3D model used for the simulated projectile
```

## Prerequisites

- JDK 17 or later
- Gradle (or the included Gradle wrapper, if present)
- A GPU/driver with OpenGL support

## Setup & run

1. Clone or download the project.
2. Build and run with Gradle:
   ```
   ./gradlew build
   ./gradlew run
   ```
   Or open the project in IntelliJ IDEA / your IDE of choice, let Gradle sync, and run `Simulation.UI.Main`.

The app launches fullscreen.

## Controls

| Key / Input | Action |
|---|---|
| `W` `A` `S` `D` | Orbit/move the 3D camera |
| `Q` / `E` | Move camera down / up |
| Left Click on a number box | Select that input field |
| `0`–`9` | Type a digit into the selected field |
| `Backspace` | Delete the last digit |
| Left Click on "run" area | Calculate and launch the simulated projectile |
| `Enter` | Confirm / close a window |

## Known issues

- `Pendulum` and `Graph` modules exist as stubs in the menu architecture but aren't implemented yet — projectile motion is currently the only complete simulation
- `Mechanism` (generic mechanism UI) is in progress
- Limited input validation — entering empty values where a number is expected can throw an exception

## RoadMap
- Develop various more simulation including Electrodynamics, Thermodynamics, Optics etc
- Develop more visually attractive simulation like Tracking trajectory
- Develop a 3D  graph of trajectory
- Develop 3D vector rotation visualization usnig `Quaternions`

## Demo Run
<img width="1898" height="992" alt="Demo" src="https://github.com/user-attachments/assets/c922b45a-3460-42cb-a7ad-309d386b7580" />
